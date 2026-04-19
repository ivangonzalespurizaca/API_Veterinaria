package com.cibertec.veterinaria.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MascotaInfoDTO {
    private Long idMascota;
    private String nombreMascota;
    private String especie;
    private String raza;
    private String sexo;
    private String nombreDuenio;
    private String dniDuenio;
    private Integer edad;
    private BigDecimal pesoActual;
    private String fotoUrl;
}