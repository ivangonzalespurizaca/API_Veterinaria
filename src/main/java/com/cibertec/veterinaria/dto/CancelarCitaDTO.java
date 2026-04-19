package com.cibertec.veterinaria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelarCitaDTO {
    @NotBlank(message = "El motivo de cancelación es obligatorio")
    @Size(min = 10, message = "El motivo debe ser más descriptivo (min. 10 caracteres)")
    String motivo;
}
