package com.cibertec.veterinaria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MascotaUpdateDTO {
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombreMascota;

    @NotBlank(message = "La raza no puede estar vacía")
    private String raza;

    @NotNull(message = "El peso es obligatorio")
    @Positive(message = "El peso debe ser mayor a 0")
    private BigDecimal pesoActual;

    private Boolean activo;
}