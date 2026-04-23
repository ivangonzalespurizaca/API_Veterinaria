package com.cibertec.veterinaria.service;

import com.cibertec.veterinaria.dto.VacunaAplicadaDTO;
import com.cibertec.veterinaria.dto.VacunaAplicadaInfoDTO;
import com.cibertec.veterinaria.dto.VacunaAplicadaProgramadaDTO;
import com.cibertec.veterinaria.dto.VacunaConfirmadaDTO;

import java.time.LocalDate;
import java.util.List;

public interface VacunaAplicadaService {
    // Para ver el historial y lo pendiente en la consulta
    List<VacunaAplicadaInfoDTO> listarCarnetPorMascota(Long idMascota);

    // ESCENARIO A: El veterinario programa una dosis futura (sin aplicar nada hoy)
    VacunaAplicadaInfoDTO programarDosisFutura(Long idMascota, VacunaAplicadaProgramadaDTO dto);

    // ESCENARIO B: El veterinario ve una programada y le da "Aplicar Ahora"
    VacunaAplicadaInfoDTO aplicarVacunaProgramada(VacunaConfirmadaDTO dto, Long idConsulta);

    // ESCENARIO C: El veterinario pone una vacuna que NO estaba programada (Directo)
    VacunaAplicadaInfoDTO aplicarVacunaInmediata(Long idMascota, VacunaAplicadaDTO dto, Long idConsulta);

}
