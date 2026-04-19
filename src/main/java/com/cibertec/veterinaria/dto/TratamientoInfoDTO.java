package com.cibertec.veterinaria.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TratamientoInfoDTO {
    private String nombreMedicamento;
    private String dosis;
    private String frecuencia;
    private Integer duracionDias;
}