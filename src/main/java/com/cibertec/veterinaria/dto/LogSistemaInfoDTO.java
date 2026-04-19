package com.cibertec.veterinaria.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogSistemaInfoDTO {
    private Long idLog;
    private String nombreUsuario;
    private String rolUsuario;
    private String tablaAfectada;
    private String accion;
    private String descripcion;
    private LocalDateTime fechaRegistro;
}