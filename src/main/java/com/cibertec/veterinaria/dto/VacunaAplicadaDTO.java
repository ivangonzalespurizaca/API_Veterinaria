package com.cibertec.veterinaria.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VacunaAplicadaDTO {
    @NotNull(message = "El ID de la vacuna es obligatorio")
    private Long idVacuna;

    @NotNull(message = "El número de dosis es obligatorio")
    @Min(value = 1, message = "El número de dosis debe ser al menos 1")
    @Max(value = 10, message = "El número de dosis no parece válido")
    private Integer nroDosis;

    @Future(message = "La fecha de la próxima dosis debe ser una fecha futura")
    private LocalDate proximaDosis;

    @Size(max = 255, message = "Las observaciones no deben exceder los 255 caracteres")
    private String observaciones;

    private boolean generarRecordatorio;
}
