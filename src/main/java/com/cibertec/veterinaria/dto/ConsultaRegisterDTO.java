package com.cibertec.veterinaria.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaRegisterDTO {
    @NotNull(message = "El ID de la cita es obligatorio")
    private Long idCita;

    @NotNull(message = "El peso es obligatorio")
    @DecimalMin(value = "0.1", message = "El peso debe ser mayor a 0")
    @Digits(integer = 3, fraction = 2, message = "Formato de peso inválido (máx 3 enteros y 2 decimales)")
    private BigDecimal pesoActual;

    @NotNull(message = "La temperatura es obligatoria")
    @DecimalMin(value = "30.0", message = "Temperatura demasiado baja")
    @DecimalMax(value = "45.0", message = "Temperatura demasiado alta")
    private BigDecimal temperatura;

    @NotBlank(message = "El diagnóstico es obligatorio")
    @Size(min = 5, message = "El diagnóstico debe ser más detallado")
    private String diagnostico;

    @NotBlank(message = "Las recomendaciones son obligatorias")
    private String recomendaciones;

    @Valid
    private List<TratamientoDTO> tratamientos;

    @Valid
    private List<VacunaAplicadaDTO> vacunas;
}