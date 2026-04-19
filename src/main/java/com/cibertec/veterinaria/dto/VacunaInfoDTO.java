package com.cibertec.veterinaria.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VacunaInfoDTO {
    private Long idVacuna;
    private String nombreVacuna;
    private String descripcion;
    private Boolean activo;
}