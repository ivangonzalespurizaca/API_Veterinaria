package com.cibertec.veterinaria.service;

import com.cibertec.veterinaria.dto.LogSistemaInfoDTO;

import java.time.LocalDate;
import java.util.List;

public interface LogSistemaService {

    List<LogSistemaInfoDTO> listarLogsPorFecha();

    List<LogSistemaInfoDTO> listarLogsPorRangoFechas(LocalDate inicio, LocalDate fin);

    void registrarLog(String idUsuario, String tabla, String accion, String descripcion);
}