package com.cibertec.veterinaria.service;

import com.cibertec.veterinaria.dto.ConsultaInfoDTO;
import com.cibertec.veterinaria.dto.ConsultaRegisterDTO;

import java.util.List;

public interface ConsultaService {
    ConsultaInfoDTO registrarAtencionCompleta(ConsultaRegisterDTO dto);
    ConsultaInfoDTO obtenerPorId(Long idConsulta);
    List<ConsultaInfoDTO> listarPorMascota(Long idMascota);
    List<ConsultaInfoDTO> listarPorVeterinario(String idUsuarioVet);
    List<ConsultaInfoDTO> buscarPorCriterio(String criterio);
    List<ConsultaInfoDTO> listarTodas();
    List<ConsultaInfoDTO> listarPorVeterinarioYFecha(String idUsuarioVet, java.time.LocalDate fecha);
}
