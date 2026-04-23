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
import java.util.ArrayList;
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
    private final VacunaAplicadaRepository vacunaAplicadaRepository;
    private final HorarioAtencionRepository horarioRepository;
    private final CitaRepository citaRepository;
    private final ConsultaRepository consultaRepository;
    private final TratamientoRepository tratamientoRepository;
    private static final String DEFAULT_USER_IMAGE = "https://res.cloudinary.com/dfid8iuf3/image/upload/v1775790000/1077114_vegvlp.png";

    @Override
    public void run(String... args) throws Exception {

        // 1. USUARIOS (Con roles y sincronización Firebase)
        if (usuarioRepository.count() > 0) {
            System.out.println("=> La base de datos ya contiene datos. Saltando carga inicial.");
            return;
        }

        System.out.println("=> Iniciando carga de datos de prueba en 'veterinaria_app'...");

        Usuario admin = Usuario.builder()
                .idUsuario("pizlIsN80CVtBTX1asD0bFLwIg83")
                .nombres("Carlos").apellidos("Perez")
                .email("clinsantar@gmail.com").dni("22222222").celular("999985854")
                .fotoUrl(DEFAULT_USER_IMAGE)
                .rol(TipoRol.ADMINISTRADOR).genero(TipoGenero.MASCULINO).build();

        Usuario userVet = Usuario.builder()
                .idUsuario("4QJvg14NAEg6jNqoPL75cLBd2hf1")
                .nombres("Oscar").apellidos("Gonzales Purizaca")
                .email("ivangonzalespurizaca@gmail.com").dni("71769112").celular("955197741")
                .fotoUrl(DEFAULT_USER_IMAGE)
                .rol(TipoRol.VETERINARIO).genero(TipoGenero.MASCULINO).build();

        Usuario cliente = Usuario.builder()
                .idUsuario("6JUWCOXMeJb4Y8dqAi8sM8Gazkr2")
                .nombres("Jhon").apellidos("Ramos")
                .email("jhonramos3456@gmail.com").dni("33333333").celular("987654321")
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

        Mascota luna = Mascota.builder()
                .cliente(cliente).nombreMascota("Luna")
                .especie(TipoEspecie.CANINO).raza("Beagle")
                .sexo(TipoSexoMascota.HEMBRA).fechaNacimiento(LocalDate.of(2020, 8, 20))
                .fotoUrl("https://res.cloudinary.com/dfid8iuf3/image/upload/q_auto/f_auto/v1775845460/huella_sltxjt.png")
                .pesoActual(new BigDecimal("12.30")).build();

        Mascota simba = Mascota.builder()
                .cliente(cliente).nombreMascota("Simba")
                .especie(TipoEspecie.FELINO).raza("Persa")
                .sexo(TipoSexoMascota.MACHO).fechaNacimiento(LocalDate.of(2019, 11, 05))
                .fotoUrl("https://res.cloudinary.com/dfid8iuf3/image/upload/q_auto/f_auto/v1775845460/huella_sltxjt.png")
                .pesoActual(new BigDecimal("5.10")).build();

        Mascota rocky = Mascota.builder()
                .cliente(cliente).nombreMascota("Rocky")
                .especie(TipoEspecie.CANINO).raza("Bulldog Francés")
                .sexo(TipoSexoMascota.MACHO).fechaNacimiento(LocalDate.of(2022, 6, 12))
                .fotoUrl("https://res.cloudinary.com/dfid8iuf3/image/upload/q_auto/f_auto/v1775845460/huella_sltxjt.png")
                .pesoActual(new BigDecimal("10.80")).build();

        Mascota nala = Mascota.builder()
                .cliente(cliente).nombreMascota("Nala")
                .especie(TipoEspecie.FELINO).raza("Bengala")
                .sexo(TipoSexoMascota.HEMBRA).fechaNacimiento(LocalDate.of(2021, 3, 25))
                .fotoUrl("https://res.cloudinary.com/dfid8iuf3/image/upload/q_auto/f_auto/v1775845460/huella_sltxjt.png")
                .pesoActual(new BigDecimal("4.50")).build();

        Mascota tobi = Mascota.builder()
                .cliente(cliente).nombreMascota("Tobi")
                .especie(TipoEspecie.CANINO).raza("Poodle")
                .sexo(TipoSexoMascota.MACHO).fechaNacimiento(LocalDate.of(2018, 12, 30))
                .fotoUrl("https://res.cloudinary.com/dfid8iuf3/image/upload/q_auto/f_auto/v1775845460/huella_sltxjt.png")
                .pesoActual(new BigDecimal("7.20")).build();

        Mascota mika = Mascota.builder()
                .cliente(cliente).nombreMascota("Mika")
                .especie(TipoEspecie.CANINO).raza("Siberian Husky")
                .sexo(TipoSexoMascota.HEMBRA).fechaNacimiento(LocalDate.of(2023, 2, 14))
                .fotoUrl("https://res.cloudinary.com/dfid8iuf3/image/upload/q_auto/f_auto/v1775845460/huella_sltxjt.png")
                .pesoActual(new BigDecimal("18.40")).build();


        List<Mascota> misMascotas = List.of(firulais, michi, luna, simba, rocky, nala, tobi, mika);
        mascotaRepository.saveAll(misMascotas);

        // 4. VACUNAS (Catálogo)
        Vacuna v1 = Vacuna.builder().nombreVacuna("Quíntuple Canina").descripcion("Protege contra Parvovirus, Distemper, Hepatitis, Leptospirosis y Parainfluenza.").build();
        Vacuna v2 = Vacuna.builder().nombreVacuna("Triple Felina").descripcion("Protege contra Calicivirus, Rinotraqueitis y Panleucopenia felina.").build();
        Vacuna v3 = Vacuna.builder().nombreVacuna("Antirrábica").descripcion("Vacuna obligatoria para la prevención del virus de la rabia en perros y gatos.").build();
        Vacuna v4 = Vacuna.builder().nombreVacuna("KC (Bordetella)").descripcion("Protege contra la tos de las perreras (Bordetella bronchiseptica).").build();
        Vacuna v5 = Vacuna.builder().nombreVacuna("Giardia").descripcion("Prevención de la giardiasis canina causada por el parásito Giardia lamblia.").build();
        Vacuna v6 = Vacuna.builder().nombreVacuna("Leucemia Felina").descripcion("Protección contra el virus de la leucemia felina (FeLV).").build();
        Vacuna v7 = Vacuna.builder().nombreVacuna("Parvovirus Monovalente").descripcion("Refuerzo específico para la prevención de parvovirosis severa en cachorros.").build();
        Vacuna v8 = Vacuna.builder().nombreVacuna("Distemper Monovalente").descripcion("Protección específica contra el virus del moquillo canino.").build();

        List<Vacuna> vacunasCat = List.of(v1, v2, v3, v4, v5, v6, v7, v8);
        vacunaRepository.saveAll(vacunasCat);

        // 5. HORARIOS DE ATENCIÓN (Lunes a Sábado - Máximo 8 horas por día)
        List<HorarioAtencion> horarios = new ArrayList<>();

        // Lunes a Viernes: Turno corrido de 8:00 AM a 4:00 PM (8 horas)
        for (TipoDiaSemana dia : List.of(TipoDiaSemana.LUNES, TipoDiaSemana.MARTES,
                TipoDiaSemana.MIERCOLES, TipoDiaSemana.JUEVES,
                TipoDiaSemana.VIERNES)) {
            horarios.add(HorarioAtencion.builder()
                    .veterinario(veterinario)
                    .diaSemana(dia)
                    .horaInicio(LocalTime.of(8, 0))
                    .horaFin(LocalTime.of(16, 0))
                    .duracionMinutos(30)
                    .build());
        }

        // Sábado: Turno reducido de 8:00 AM a 1:00 PM (5 horas)
        horarios.add(HorarioAtencion.builder()
                .veterinario(veterinario)
                .diaSemana(TipoDiaSemana.SABADO)
                .horaInicio(LocalTime.of(8, 0))
                .horaFin(LocalTime.of(13, 0))
                .duracionMinutos(30)
                .build());

        horarioRepository.saveAll(horarios);

        List<Cita> citasManuales = new ArrayList<>();
        LocalDate hoy = LocalDate.of(2026, 4, 23);

        // --- 6 CITAS COMPLETADAS (Historial) ---
        Cita c1 = Cita.builder().mascota(firulais).veterinario(veterinario)
                .fecha(hoy.minusDays(10)).hora(LocalTime.of(8, 0))
                .motivo("Infección estomacal").estado(TipoEstadoCita.COMPLETADA).build();

        Cita c2 = Cita.builder().mascota(michi).veterinario(veterinario)
                .fecha(hoy.minusDays(8)).hora(LocalTime.of(9, 0))
                .motivo("Vacunación Triple Felina").estado(TipoEstadoCita.COMPLETADA).build();

        Cita c3 = Cita.builder().mascota(luna).veterinario(veterinario)
                .fecha(hoy.minusDays(7)).hora(LocalTime.of(10, 0))
                .motivo("Refuerzo Quíntuple").estado(TipoEstadoCita.COMPLETADA).build();

        Cita c4 = Cita.builder().mascota(simba).veterinario(veterinario)
                .fecha(hoy.minusDays(6)).hora(LocalTime.of(11, 0))
                .motivo("Vacuna Antirrábica").estado(TipoEstadoCita.COMPLETADA).build();

        Cita c5 = Cita.builder().mascota(rocky).veterinario(veterinario)
                .fecha(hoy.minusDays(5)).hora(LocalTime.of(12, 0))
                .motivo("Vacuna KC Bordetella").estado(TipoEstadoCita.COMPLETADA).build();

        Cita c6 = Cita.builder().mascota(nala).veterinario(veterinario)
                .fecha(hoy.minusDays(4)).hora(LocalTime.of(15, 0))
                .motivo("Vacuna Leucemia Felina").estado(TipoEstadoCita.COMPLETADA).build();

        // --- 5 CITAS CONFIRMADAS (Día de la presentación: 29 de Abril) ---
        Cita c7 = Cita.builder().mascota(tobi).veterinario(veterinario)
                .fecha(LocalDate.of(2026, 4, 29)).hora(LocalTime.of(8, 0))
                .motivo("Control de alergia").estado(TipoEstadoCita.CONFIRMADA).build();

        Cita c8 = Cita.builder().mascota(mika).veterinario(veterinario)
                .fecha(LocalDate.of(2026, 4, 29)).hora(LocalTime.of(8, 30))
                .motivo("Chequeo cachorro").estado(TipoEstadoCita.CONFIRMADA).build();

        Cita c9 = Cita.builder().mascota(firulais).veterinario(veterinario)
                .fecha(LocalDate.of(2026, 4, 29)).hora(LocalTime.of(9, 30))
                .motivo("Revisión de puntos").estado(TipoEstadoCita.CONFIRMADA).build();

        Cita c10 = Cita.builder().mascota(michi).veterinario(veterinario)
                .fecha(LocalDate.of(2026, 4, 29)).hora(LocalTime.of(10, 0))
                .motivo("Limpieza dental").estado(TipoEstadoCita.CONFIRMADA).build();

        Cita c11 = Cita.builder().mascota(luna).veterinario(veterinario)
                .fecha(LocalDate.of(2026, 4, 29)).hora(LocalTime.of(10, 30))
                .motivo("Ecografía control").estado(TipoEstadoCita.CONFIRMADA).build();

        citasManuales.addAll(List.of(c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11));

        // --- 5 CITAS PENDIENTES ---
        for(int i = 0; i < 5; i++) {
            citasManuales.add(Cita.builder()
                    .mascota(misMascotas.get(i)).veterinario(veterinario)
                    .fecha(hoy.plusDays(15 + i)).hora(LocalTime.of(14, 0))
                    .motivo("Cita pendiente de revisión").estado(TipoEstadoCita.PENDIENTE).build());
        }
        citaRepository.saveAll(citasManuales);

        // 7. CONSULTAS, TRATAMIENTOS Y VACUNAS
        // Consulta con Tratamiento para Firulais
        Consulta consTrat = Consulta.builder()
                .cita(c1)
                .pesoActual(new BigDecimal("25.0"))
                .temperatura(new BigDecimal("39.2"))
                .diagnostico("Gastroenteritis bacteriana")
                .recomendaciones("Antibióticos por 5 días")
                .build();
        consultaRepository.save(consTrat);

        tratamientoRepository.save(TratamientoMedicamento.builder()
                .consulta(consTrat)
                .nombreMedicamento("Enrofloxacina")
                .dosis("2.5ml")
                .frecuencia("Cada 24h")
                .build());

        // Consultas y Vacunas Aplicadas (C2 a C6)
        List<Cita> citasVacunas = List.of(c2, c3, c4, c5, c6);
        List<Vacuna> vacunasParaAplicar = List.of(v2, v1, v3, v4, v6);

        for (int i = 0; i < citasVacunas.size(); i++) {
            Cita citaV = citasVacunas.get(i);
            Consulta consV = Consulta.builder()
                    .cita(citaV)
                    .pesoActual(citaV.getMascota().getPesoActual())
                    .temperatura(new BigDecimal("38.4"))
                    .diagnostico("Aplicación de vacuna preventiva")
                    .build();
            consultaRepository.save(consV);

            vacunaAplicadaRepository.save(VacunaAplicada.builder()
                    .mascota(citaV.getMascota())
                    .vacuna(vacunasParaAplicar.get(i))
                    .consulta(consV)
                    .estado(TipoEstadoVacuna.APLICADA)
                    .fechaAplicacion(citaV.getFecha()) // Cambiado de getFechaCita() a getFecha()
                    .nroDosis(1)
                    .observaciones("Dosis inicial completada")
                    .build());
        }

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