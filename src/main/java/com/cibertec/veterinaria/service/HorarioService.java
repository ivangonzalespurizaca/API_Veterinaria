package com.cibertec.veterinaria.service;

import com.cibertec.veterinaria.dto.HorarioInfoDTO;
import com.cibertec.veterinaria.dto.HorarioRegisterDTO;
import com.cibertec.veterinaria.dto.HorarioUpdateDTO;

import java.util.List;

public interface HorarioService {
    HorarioInfoDTO registrarHorario(HorarioRegisterDTO dto);
    List<HorarioInfoDTO> listarDisponiblesPorVeterinario(Long id);
    List<HorarioInfoDTO> listarTodoPorVeterinario(Long id);
    HorarioInfoDTO actualizarHorario(Long id, HorarioUpdateDTO dto);
    HorarioInfoDTO desactivarHorario(Long id);
    HorarioInfoDTO activarHorario(Long id);
}