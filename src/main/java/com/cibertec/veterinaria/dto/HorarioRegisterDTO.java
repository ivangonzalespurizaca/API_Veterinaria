package com.cibertec.veterinaria.dto;

import com.cibertec.veterinaria.entity.enums.TipoDiaSemana;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HorarioRegisterDTO {

    @NotBlank(message = "El ID del veterinario es obligatorio")
    private String idUsuarioVeterinario;

    @NotNull(message = "El día de la semana es obligatorio")
    private TipoDiaSemana diaSemana;

    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    private LocalTime horaFin;
}