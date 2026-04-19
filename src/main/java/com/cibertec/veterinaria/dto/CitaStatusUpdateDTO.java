package com.cibertec.veterinaria.dto;

import com.cibertec.veterinaria.entity.enums.TipoEstadoCita;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CitaStatusUpdateDTO {
    @NotNull(message = "El nuevo estado no puede ser nulo")
    private TipoEstadoCita nuevoEstado;
}