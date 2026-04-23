package com.cibertec.veterinaria.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VacunaConfirmadaDTO {
    @NotNull(message = "El ID de la aplicación es obligatorio")
    private Long idAplicacion;

    @NotNull(message = "La fecha de aplicación es obligatoria")
    @PastOrPresent(message = "No puede aplicar una vacuna en el futuro")
    private LocalDate fechaAplicacion;

    private String observaciones;
}