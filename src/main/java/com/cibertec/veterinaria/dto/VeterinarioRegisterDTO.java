package com.cibertec.veterinaria.dto;

import com.cibertec.veterinaria.entity.enums.TipoEspecialidad;
import com.cibertec.veterinaria.entity.enums.TipoGenero;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VeterinarioRegisterDTO {

    @NotBlank(message = "El ID de usuario es obligatorio")
    private String idUsuario;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombres;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100)
    private String apellidos;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    private String email;

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos numéricos")
    private String dni;

    @Size(max = 15)
    private String celular;

    @NotNull(message = "El género es obligatorio")
    private TipoGenero genero;

    @NotBlank(message = "El número de colegiatura es obligatorio")
    @Size(max = 20)
    private String numColegiatura;

    @NotNull(message = "La especialidad es obligatoria")
    private TipoEspecialidad especialidad;
}