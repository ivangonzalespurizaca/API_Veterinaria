package com.cibertec.veterinaria.dto;

import com.cibertec.veterinaria.entity.enums.TipoGenero;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioUpdateDTO {

    @NotBlank(message = "Los nombres no pueden estar vacíos")
    @Size(min = 2, max = 50, message = "Los nombres deben tener entre 2 y 50 caracteres")
    private String nombres;

    @NotBlank(message = "Los apellidos no pueden estar vacíos")
    @Size(min = 2, max = 50, message = "Los apellidos deben tener entre 2 y 50 caracteres")
    private String apellidos;

    @NotBlank(message = "El celular es obligatorio")
    @Size(min = 9, max = 15, message = "El celular debe tener entre 9 y 15 caracteres")
    @Pattern(regexp = "\\+?\\d+", message = "El celular debe tener un formato válido")
    private String celular;

    @NotNull(message = "El género es obligatorio")
    private TipoGenero genero;
}