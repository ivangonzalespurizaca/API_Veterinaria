package com.cibertec.veterinaria.dto;

import com.cibertec.veterinaria.entity.enums.TipoEstadoVacuna;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VacunaAplicadaInfoDTO {
    private Long idAplicacion;

    // Datos de la Vacuna
    private String nombreVacuna;
    private String descripcionVacuna;

    // Datos de la dosis
    private LocalDate fechaAplicacion;
    private LocalDate proximaDosis;
    private Integer nroDosis;

    // Estado y Responsable
    private TipoEstadoVacuna estado;
    private String nombreVeterinario;
    private String observaciones;
}