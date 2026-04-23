package com.cibertec.veterinaria.service.impl;

import com.cibertec.veterinaria.dto.AgendaVetDTO;
import com.cibertec.veterinaria.dto.CitaInfoDTO;
import com.cibertec.veterinaria.dto.CitaRegisterDTO;
import com.cibertec.veterinaria.dto.SlotDTO;
import com.cibertec.veterinaria.entity.Cita;
import com.cibertec.veterinaria.entity.HorarioAtencion;
import com.cibertec.veterinaria.entity.Mascota;
import com.cibertec.veterinaria.entity.Veterinario;
import com.cibertec.veterinaria.entity.enums.TipoDiaSemana;
import com.cibertec.veterinaria.entity.enums.TipoEstadoCita;
import com.cibertec.veterinaria.mapper.CitaMapper;
import com.cibertec.veterinaria.repository.CitaRepository;
import com.cibertec.veterinaria.repository.HorarioAtencionRepository;
import com.cibertec.veterinaria.repository.MascotaRepository;
import com.cibertec.veterinaria.repository.VeterinarioRepository;
import com.cibertec.veterinaria.service.CitaService;
import com.cibertec.veterinaria.service.LogSistemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Service
public class CitaServiceImpl implements CitaService {

    private final CitaRepository citaRepository;
    private final MascotaRepository mascotaRepository;
    private final VeterinarioRepository veterinarioRepository;
    private final HorarioAtencionRepository horarioRepository;
    private final CitaMapper citaMapper;
    private final LogSistemaService logService;

    public List<SlotDTO> obtenerHorasDisponibles(Long idVeterinario, LocalDate fecha) {
        // 1. Obtener el día de la semana y LIMPIAR tildes
        String nombreDia = fecha.getDayOfWeek()
                .getDisplayName(TextStyle.FULL, new Locale("es", "ES"))
                .toUpperCase();

        // Eliminar tildes (convierte MIÉRCOLES en MIERCOLES)
        nombreDia = Normalizer.normalize(nombreDia, Normalizer.Form.NFD);
        nombreDia = nombreDia.replaceAll("\\p{InCombiningDiacriticalMarks}", "");

        TipoDiaSemana diaBusqueda = TipoDiaSemana.valueOf(nombreDia);

        // 2. Buscar el horario de atención del veterinario para ese día
        HorarioAtencion horario = horarioRepository
                .findByVeterinarioIdVeterinarioAndDiaSemanaAndActivoTrue(idVeterinario, diaBusqueda)
                .orElseThrow(() -> new RuntimeException("El veterinario no atiende este día"));

        // 3. Obtener las horas que ya están reservadas en la DB
        List<LocalTime> horasOcupadas = citaRepository.findHorasOcupadas(idVeterinario, fecha);

        // 4. Generar los slots desde hora_inicio hasta hora_fin usando la duracion_minutos
        List<SlotDTO> slots = new ArrayList<>();
        LocalTime horaActual = horario.getHoraInicio();

        while (horaActual.isBefore(horario.getHoraFin())) {
            boolean estaOcupado = horasOcupadas.contains(horaActual);

            slots.add(SlotDTO.builder()
                    .hora(horaActual)
                    .disponible(!estaOcupado)
                    .build());

            // Avanzar el reloj según la duración del turno (ej. 30 min)
            horaActual = horaActual.plusMinutes(horario.getDuracionMinutos());
        }

        return slots;
    }

    @Transactional
    @Override
    public CitaInfoDTO registrarCita(CitaRegisterDTO dto) {
        // 1. Obtener entidades y validar existencia
        Mascota mascota = mascotaRepository.findById(dto.getIdMascota())
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        Veterinario veterinario = veterinarioRepository.findById(dto.getIdVeterinario())
                .orElseThrow(() -> new RuntimeException("Veterinario no encontrado"));

        LocalDateTime momentoCita = LocalDateTime.of(dto.getFecha(), dto.getHora());

        // 2. Validar que la fecha sea válida (Presente o Futuro)
        if (momentoCita.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("No se pueden programar citas para una fecha u hora que ya pasó");
        }

        // 3. REGLA DE NEGOCIO: Máximo 2 citas PENDIENTES por mascota
        long citasPendientes = citaRepository.countByMascotaIdMascotaAndEstado(
                dto.getIdMascota(),
                TipoEstadoCita.PENDIENTE
        );

        if (citasPendientes >= 2) {
            throw new RuntimeException("La mascota '" + mascota.getNombreMascota() +
                    "' ya alcanzó el límite de 2 citas pendientes. Debe asistir o cancelar una antes de programar más.");
        }

        // 4. VALIDACIÓN DE AGENDA: Disponibilidad del Veterinario
        // (Doble check de seguridad para evitar solapamiento en el mismo horario)
        List<LocalTime> ocupadas = citaRepository.findHorasOcupadas(veterinario.getIdVeterinario(), dto.getFecha());
        if (ocupadas.contains(dto.getHora())) {
            throw new RuntimeException("El horario " + dto.getHora() + " ya está reservado para este veterinario.");
        }

        // 5. Creación y Persistencia
        Cita cita = citaMapper.toEntity(dto);
        cita.setMascota(mascota);
        cita.setVeterinario(veterinario);
        cita.setEstado(TipoEstadoCita.PENDIENTE);
        // cita.setFechaCreacion(LocalDateTime.now()); // Si tienes este campo en la entidad

        Cita citaGuardada = citaRepository.save(cita);

        logService.registrarLog(
                mascota.getCliente().getIdUsuario(),
                "CITA",
                "REGISTRO",
                "Nueva cita programada para la mascota " + mascota.getNombreMascota() + " el día " + dto.getFecha()
        );

        return citaMapper.toInfoDTO(citaGuardada);
    }

    @Override
    public CitaInfoDTO obtenerPorId(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + id));
        return citaMapper.toInfoDTO(cita);
    }

    @Override
    public List<CitaInfoDTO> listarTodas() {
        return citaRepository.findAllByOrderByFechaAscHoraAsc()
                .stream()
                .map(citaMapper::toInfoDTO)
                .toList();
    }

    @Override
    public List<CitaInfoDTO> buscarTodoPorCriterio(String criterio) {
        if (criterio == null || criterio.isBlank()) return listarTodas();

        return citaRepository.buscarTodoPorCriterio(criterio)
                .stream()
                .map(citaMapper::toInfoDTO)
                .toList();
    }

    @Override
    public List<CitaInfoDTO> filtrarTodoPorEstado(TipoEstadoCita estado) {
        return citaRepository.findByEstadoOrderByFechaAscHoraAsc(estado)
                .stream()
                .map(citaMapper::toInfoDTO)
                .toList();
    }

    @Override
    public List<CitaInfoDTO> listarPorVeterinario(String idUsuarioVet) {
        return citaRepository.findByVeterinarioUsuarioIdUsuarioOrderByFechaAscHoraAsc(idUsuarioVet)
                .stream()
                .map(citaMapper::toInfoDTO)
                .toList();
    }

    @Override
    public List<CitaInfoDTO> listarCitasDeHoy(String idUsuarioVet) {
        LocalDate hoy = LocalDate.now();
        return citaRepository.findByVeterinarioUsuarioIdUsuarioAndFechaAndEstadoNotOrderByHoraAsc(
                        idUsuarioVet, hoy, TipoEstadoCita.CANCELADA)
                .stream()
                .map(citaMapper::toInfoDTO)
                .toList();
    }

    @Override
    public List<CitaInfoDTO> buscarCitasPropiasPorCriterio(String idUsuarioVet, String criterio) {
        if (criterio == null || criterio.isBlank()) return listarPorVeterinario(idUsuarioVet);

        return citaRepository.buscarPropiasPorCriterio(idUsuarioVet, criterio)
                .stream()
                .map(citaMapper::toInfoDTO)
                .toList();
    }

    @Override
    public List<CitaInfoDTO> listarPorCliente(String idUsuarioCliente) {
        return citaRepository.findByMascotaClienteIdUsuarioOrderByFechaAscHoraAsc(idUsuarioCliente)
                .stream()
                .map(citaMapper::toInfoDTO)
                .toList();
    }

    @Override
    public List<CitaInfoDTO> filtrarPorVeterinarioYEstado(String idUsuarioVet, TipoEstadoCita estado) {
        return citaRepository.findByVeterinarioUsuarioIdUsuarioAndEstadoOrderByFechaAscHoraAsc(idUsuarioVet, estado)
                .stream()
                .map(citaMapper::toInfoDTO)
                .toList();
    }

    @Override
    public List<CitaInfoDTO> filtrarPorClienteYEstado(String idUsuarioCliente, TipoEstadoCita estado) {
        return citaRepository.findByMascotaClienteIdUsuarioAndEstadoOrderByFechaAscHoraAsc(idUsuarioCliente, estado)
                .stream()
                .map(citaMapper::toInfoDTO)
                .toList();
    }

    @Transactional
    @Override
    public CitaInfoDTO cambiarEstado(Long id, TipoEstadoCita nuevoEstado) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        // Regla: No se puede modificar una cita CANCELADA o COMPLETADA
        if (cita.getEstado() == TipoEstadoCita.CANCELADA || cita.getEstado() == TipoEstadoCita.COMPLETADA) {
            throw new RuntimeException("No se puede cambiar el estado de una cita finalizada o cancelada");
        }

        cita.setEstado(nuevoEstado);
        return citaMapper.toInfoDTO(citaRepository.save(cita));
    }

    @Transactional
    @Override
    public CitaInfoDTO cancelarCita(Long id, String motivo) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        if (cita.getEstado() == TipoEstadoCita.COMPLETADA) {
            throw new RuntimeException("No se puede cancelar una cita que ya ha sido completada");
        }

        String motivoLimpio = motivo;
        if (motivo.contains("{")) {
            // Esto quita las llaves, comillas y los saltos de línea (\r\n)
            motivoLimpio = motivo.replaceAll("[\\{\\}\"\\r\\n]", "")
                    .replace("motivo:", "")
                    .trim();
        }


        cita.setEstado(TipoEstadoCita.CANCELADA);
        cita.setMotivo(cita.getMotivo() + " | MOTIVO CANCELACIÓN: " + motivoLimpio);
        Cita guardada = citaRepository.save(cita);

        logService.registrarLog(
                cita.getVeterinario().getUsuario().getIdUsuario(),
                "CITA",
                "CANCELACION",
                "Cita #" + id + " cancelada. Motivo: " + motivoLimpio
        );

        return citaMapper.toInfoDTO(citaRepository.save(guardada));
    }

    @Transactional
    @Override
    public CitaInfoDTO reprogramarCita(Long id, LocalDate nuevaFecha, LocalTime nuevaHora) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        LocalDateTime momentoNuevo = LocalDateTime.of(nuevaFecha, nuevaHora);
        if (momentoNuevo.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("No se puede reprogramar una cita para una fecha u hora que ya pasó");
        }

        List<LocalTime> ocupadas = citaRepository.findHorasOcupadas(cita.getVeterinario().getIdVeterinario(), nuevaFecha);
        if (ocupadas.contains(nuevaHora)) {
            throw new RuntimeException("El nuevo horario seleccionado ya está ocupado");
        }

        LocalDate fechaAntigua = cita.getFecha();
        LocalTime horaAntigua = cita.getHora();

        cita.setFecha(nuevaFecha);
        cita.setHora(nuevaHora);
        cita.setEstado(TipoEstadoCita.PENDIENTE);
        Cita guardada = citaRepository.save(cita);

        logService.registrarLog(
                cita.getVeterinario().getUsuario().getIdUsuario(),
                "CITA",
                "REPROGRAMACION",
                "Cita #" + id + " movida de " + fechaAntigua + " " + horaAntigua + " a " + nuevaFecha + " " + nuevaHora
        );

        return citaMapper.toInfoDTO(guardada);
    }

    @Override
    public List<AgendaVetDTO> obtenerAgendaDelDia(String idVeterinario, LocalDate fecha) {
        String nombreDia = Normalizer.normalize(
                fecha.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase(),
                Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}", "");

        TipoDiaSemana diaBusqueda = TipoDiaSemana.valueOf(nombreDia);

        // 2. Horario de atención
        HorarioAtencion horario = horarioRepository
                .findByVeterinarioUsuarioIdUsuarioAndDiaSemanaAndActivoTrue(idVeterinario, diaBusqueda)
                .orElseThrow(() -> new RuntimeException("El veterinario no atiende los días " + nombreDia));

        // 3. Citas del día
        List<Cita> citasDelDia = citaRepository.findByVeterinarioUsuarioIdUsuarioAndFechaAndEstadoNot(
                idVeterinario, fecha, TipoEstadoCita.CANCELADA);

        // 4. Construir la agenda
        List<AgendaVetDTO> agenda = new ArrayList<>();
        LocalTime horaActual = horario.getHoraInicio();

        while (horaActual.isBefore(horario.getHoraFin())) {
            // Buscamos la cita usando el método de apoyo
            Cita citaEncontrada = buscarCitaEnHora(citasDelDia, horaActual);

            if (citaEncontrada != null) {
                agenda.add(new AgendaVetDTO(
                        horaActual,
                        false,
                        citaEncontrada.getMascota().getNombreMascota(),
                        citaEncontrada.getEstado()
                ));
            } else {
                agenda.add(new AgendaVetDTO(horaActual, true, "DISPONIBLE", null));
            }

            horaActual = horaActual.plusMinutes(horario.getDuracionMinutos());
        }

        return agenda;
    }

    private Cita buscarCitaEnHora(List<Cita> citas, LocalTime hora) {
        return citas.stream()
                .filter(c -> c.getHora().equals(hora))
                .findFirst()
                .orElse(null);
    }


}