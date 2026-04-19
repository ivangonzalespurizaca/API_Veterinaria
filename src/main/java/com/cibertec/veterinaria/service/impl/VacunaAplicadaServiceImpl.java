package com.cibertec.veterinaria.service.impl;

import com.cibertec.veterinaria.dto.VacunaAplicadaInfoDTO;
import com.cibertec.veterinaria.entity.VacunaAplicada;
import com.cibertec.veterinaria.entity.enums.TipoEstadoVacuna;
import com.cibertec.veterinaria.mapper.VacunaAplicadaMapper;
import com.cibertec.veterinaria.repository.VacunaAplicadaRepository;
import com.cibertec.veterinaria.service.LogSistemaService;
import com.cibertec.veterinaria.service.VacunaAplicadaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VacunaAplicadaServiceImpl implements VacunaAplicadaService {

    private final VacunaAplicadaRepository vacunaAplicadaRepository;
    private final VacunaAplicadaMapper vacunaAplicadaMapper;
    private final LogSistemaService logService;

    @Override
    @Transactional(readOnly = true)
    public List<VacunaAplicadaInfoDTO> listarCarnetPorMascota(Long idMascota) {
        return vacunaAplicadaRepository.findByMascotaIdMascotaOrderByProximaDosisAsc(idMascota)
                .stream()
                .map(vacunaAplicadaMapper::toInfoDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VacunaAplicadaInfoDTO> listarPendientesPorMascota(Long idMascota) {
        return vacunaAplicadaRepository.findByMascotaIdMascotaOrderByProximaDosisAsc(idMascota)
                .stream()
                .filter(v -> v.getEstado() == TipoEstadoVacuna.PROGRAMADA)
                .map(vacunaAplicadaMapper::toInfoDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public VacunaAplicadaInfoDTO reprogramarDosis(Long idAplicacion, LocalDate nuevaFecha) {
        VacunaAplicada vacuna = vacunaAplicadaRepository.findById(idAplicacion)
                .orElseThrow(() -> new RuntimeException("Registro de vacuna no encontrado"));

        if (vacuna.getEstado() != TipoEstadoVacuna.PROGRAMADA) {
            throw new RuntimeException("Solo se pueden reprogramar vacunas en estado PROGRAMADA");
        }

        if (nuevaFecha.isBefore(LocalDate.now())) {
            throw new RuntimeException("No se puede reprogramar un refuerzo para una fecha que ya pasó");
        }

        LocalDate fechaAnterior = vacuna.getProximaDosis();
        vacuna.setProximaDosis(nuevaFecha);
        VacunaAplicada guardada = vacunaAplicadaRepository.save(vacuna);

        logService.registrarLog(
                obtenerIdUsuarioSeguro(vacuna),
                "VACUNA_APLICADA",
                "REPROGRAMACION",
                "Se movió la fecha de refuerzo de " + vacuna.getVacuna().getNombreVacuna() +
                        " de " + fechaAnterior + " a " + nuevaFecha + " para la mascota: " + vacuna.getMascota().getNombreMascota()
        );

        return vacunaAplicadaMapper.toInfoDTO(guardada);
    }

    @Override
    @Transactional
    public VacunaAplicadaInfoDTO confirmarAplicacion(Long idAplicacion, LocalDate fechaReal) {
        VacunaAplicada vacuna = vacunaAplicadaRepository.findById(idAplicacion)
                .orElseThrow(() -> new RuntimeException("Registro de vacuna no encontrado"));

        if (vacuna.getEstado() == TipoEstadoVacuna.APLICADA) {
            throw new RuntimeException("Esta vacuna ya ha sido marcada como APLICADA anteriormente");
        }

        if (fechaReal.isAfter(LocalDate.now())) {
            throw new RuntimeException("La fecha de aplicación no puede ser una fecha futura");
        }

        vacuna.setEstado(TipoEstadoVacuna.APLICADA);
        vacuna.setFechaAplicacion(fechaReal);
        VacunaAplicada guardada = vacunaAplicadaRepository.save(vacuna);

        // 4. Auditoría
        logService.registrarLog(
                obtenerIdUsuarioSeguro(vacuna),
                "VACUNA_APLICADA",
                "CONFIRMACION",
                "Se confirmó la aplicación de: " + vacuna.getVacuna().getNombreVacuna() +
                        " (Mascota: " + vacuna.getMascota().getNombreMascota() + ") en la fecha: " + fechaReal
        );

        return vacunaAplicadaMapper.toInfoDTO(guardada);
    }

    private String obtenerIdUsuarioSeguro(VacunaAplicada vacuna) {
        if (vacuna.getCita() != null &&
                vacuna.getCita().getVeterinario() != null &&
                vacuna.getCita().getVeterinario().getUsuario() != null) {

            return vacuna.getCita().getVeterinario().getUsuario().getIdUsuario();
        }
        return null;
    }
}