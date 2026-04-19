package com.cibertec.veterinaria.dto;

import com.cibertec.veterinaria.entity.enums.TipoEspecialidad;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VeterinarioUpdateDTO {

    @NotBlank(message = "El número de colegiatura no puede estar vacío")
    @Size(max = 20)
    private String numColegiatura;

    @NotNull(message = "La especialidad es obligatoria")
    private TipoEspecialidad especialidad;

    private Boolean activo;
}