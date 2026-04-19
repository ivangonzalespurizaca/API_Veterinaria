package com.cibertec.veterinaria.util;

import com.cibertec.veterinaria.entity.*;
import com.cibertec.veterinaria.entity.enums.*;
import com.cibertec.veterinaria.repository.*;
import com.google.firebase.auth.FirebaseAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class DatabaseLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final VeterinarioRepository veterinarioRepository;
    private final MascotaRepository mascotaRepository;
    private final VacunaRepository vacunaRepository;
    private final HorarioAtencionRepository horarioRepository;
    private final CitaRepository citaRepository;
    private final ConsultaRepository consultaRepository;
    private final TratamientoRepository tratamientoRepository;
    private static final String DEFAULT_USER_IMAGE = "https://res.cloudinary.com/dfid8iuf3/image/upload/v1775790000/1077114_vegvlp.png";

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.count() > 0) {
            System.out.println("=> La base de datos ya contiene datos. Saltando carga inicial.");
            return;
        }

        System.out.println("=> Iniciando carga de datos de prueba en 'veterinaria_app'...");

        // 1. USUARIOS (Admin, Veterinario, Cliente)
        Usuario admin = Usuario.builder()
                .idUsuario("tWLp4yEDpBYOitYsCSYNVh3PaOf1")
                .nombres("Oscar Ivan").apellidos("Gonzales Purizaca")
                .email("gonzalespurizacaoscarivan@gmail.com").dni("11111111").celular("936523654")
                .fotoUrl(DEFAULT_USER_IMAGE)
                .rol(TipoRol.ADMINISTRADOR).genero(TipoGenero.MASCULINO).build();

        Usuario userVet = Usuario.builder()
                .idUsuario("sRlKc2BwW7ZyPqS2Dw620oRglZi2")
                .nombres("Carlos").apellidos("Perez")
                .email("clinsantar@gmail.com").dni("22222222").celular("999985854")
                .fotoUrl(DEFAULT_USER_IMAGE)
                .rol(TipoRol.VETERINARIO).genero(TipoGenero.MASCULINO).build();

        Usuario cliente = Usuario.builder()
                .idUsuario("qcmBCeSv5cg0eoce1T22qynbVXt2")
                .nombres("Juan").apellidos("Sanchez")
                .email("ivangonzalespurizaca@gmail.com").dni("33333333").celular("987654321")
                .fotoUrl(DEFAULT_USER_IMAGE)
                .rol(TipoRol.CLIENTE).genero(TipoGenero.MASCULINO).build();

        usuarioRepository.saveAll(List.of(admin, userVet, cliente));

        asignarRolFirebase(admin.getIdUsuario(), "ADMINISTRADOR");
        asignarRolFirebase(userVet.getIdUsuario(), "VETERINARIO");
        asignarRolFirebase(cliente.getIdUsuario(), "CLIENTE");

        // 2. VETERINARIO (Detalle profesional)
        Veterinario veterinario = Veterinario.builder()
                .usuario(userVet)
                .numColegiatura("CMVP-9988")
                .especialidad(TipoEspecialidad.MEDICINA_GENERAL).build();
        veterinarioRepository.save(veterinario);

        // 3. MASCOTAS
        Mascota firulais = Mascota.builder()
                .cliente(cliente).nombreMascota("Firulais")
                .especie(TipoEspecie.CANINO).raza("Golden Retriever")
                .sexo(TipoSexoMascota.MACHO).fechaNacimiento(LocalDate.of(2021, 5, 10))
                .fotoUrl("https://res.cloudinary.com/dfid8iuf3/image/upload/q_auto/f_auto/v1775845460/huella_sltxjt.png")
                .pesoActual(new BigDecimal("25.50")).build();

        Mascota michi = Mascota.builder()
                .cliente(cliente).nombreMascota("Michi")
                .especie(TipoEspecie.FELINO).raza("Siamés")
                .sexo(TipoSexoMascota.HEMBRA).fechaNacimiento(LocalDate.of(2022, 1, 15))
                .fotoUrl("https://res.cloudinary.com/dfid8iuf3/image/upload/q_auto/f_auto/v1775845460/huella_sltxjt.png")
                .pesoActual(new BigDecimal("4.20")).build();

        mascotaRepository.saveAll(List.of(firulais, michi));

        // 4. VACUNAS (Catálogo)
        Vacuna v1 = Vacuna.builder().nombreVacuna("Quíntuple Canina").descripcion("Protege contra Parvovirus, Distemper...").build();
        Vacuna v2 = Vacuna.builder().nombreVacuna("Triple Felina").descripcion("Protege contra Calicivirus, Rinotraqueitis...").build();
        vacunaRepository.saveAll(List.of(v1, v2));

        // 5. HORARIO DE ATENCIÓN
        HorarioAtencion horarioLunes = HorarioAtencion.builder()
                .veterinario(veterinario).diaSemana(TipoDiaSemana.LUNES)
                .horaInicio(LocalTime.of(8, 0)).horaFin(LocalTime.of(13, 0))
                .duracionMinutos(30).build();
        horarioRepository.save(horarioLunes);

        // 6. CITA Y CONSULTA (Flujo completo)
        Cita citaAtendida = Cita.builder()
                .mascota(firulais).veterinario(veterinario)
                .fecha(LocalDate.now().minusDays(1))
                .hora(LocalTime.of(10, 0))
                .motivo("Chequeo general y fiebre")
                .estado(TipoEstadoCita.COMPLETADA).build();
        citaRepository.save(citaAtendida);

        Consulta consulta = Consulta.builder()
                .cita(citaAtendida).pesoActual(new BigDecimal("26.00"))
                .temperatura(new BigDecimal("39.50"))
                .diagnostico("Infección leve de garganta")
                .recomendaciones("Mantener hidratado y reposo").build();
        consultaRepository.save(consulta);

        // 7. TRATAMIENTO
        TratamientoMedicamento trat = TratamientoMedicamento.builder()
                .consulta(consulta).nombreMedicamento("Amoxicilina")
                .dosis("250mg").frecuencia("Cada 12 horas").duracionDias(7).build();
        tratamientoRepository.save(trat);

        System.out.println("=> Carga de datos finalizada con éxito.");
    }

    private void asignarRolFirebase(String uid, String rol) {
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("rol", rol);
            FirebaseAuth.getInstance().setCustomUserClaims(uid, claims);
            System.out.println("   [Firebase] Rol " + rol + " asignado al UID: " + uid);
        } catch (Exception e) {
            System.err.println("   [Error] No se pudo asignar rol en Firebase para " + uid + ": " + e.getMessage());
        }
    }
}