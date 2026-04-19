package com.cibertec.veterinaria.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TratamientoDTO {
    @NotBlank(message = "El nombre del medicamento es obligatorio")
    @Size(max = 100, message = "El nombre del medicamento no debe exceder los 100 caracteres")
    private String nombreMedicamento;

    @NotBlank(message = "La dosis es obligatoria")
    @Size(max = 50, message = "La dosis no debe exceder los 50 caracteres")
    private String dosis;

    @NotBlank(message = "La frecuencia es obligatoria")
    @Size(max = 50, message = "Ejemplo de frecuencia: Cada 8 horas, Una vez al día")
    private String frecuencia;

    @NotNull(message = "La duración es obligatoria")
    @Min(value = 1, message = "La duración mínima debe ser de 1 día")
    @Max(value = 365, message = "La duración no puede exceder un año (365 días)")
    private Integer duracionDias;
}