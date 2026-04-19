package com.cibertec.veterinaria.service;

import com.cibertec.veterinaria.dto.VacunaAplicadaInfoDTO;

import java.time.LocalDate;
import java.util.List;

public interface VacunaAplicadaService {
    List<VacunaAplicadaInfoDTO> listarCarnetPorMascota(Long idMascota);

    List<VacunaAplicadaInfoDTO> listarPendientesPorMascota(Long idMascota);

    VacunaAplicadaInfoDTO reprogramarDosis(Long idAplicacion, LocalDate nuevaFecha);

    VacunaAplicadaInfoDTO confirmarAplicacion(Long idAplicacion, LocalDate fechaReal);
}
