package com.cibertec.veterinaria.dto;

import com.cibertec.veterinaria.entity.enums.TipoEspecie;
import com.cibertec.veterinaria.entity.enums.TipoSexoMascota;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MascotaRegisterDTO {
    @NotBlank(message = "El ID del cliente es obligatorio")
    private String idCliente;

    @NotBlank(message = "El nombre de la mascota es obligatorio")
    private String nombreMascota;

    @NotNull(message = "La especie es obligatoria")
    private TipoEspecie especie;

    @NotBlank(message = "La raza es obligatoria")
    private String raza;

    @NotNull(message = "El sexo es obligatorio")
    private TipoSexoMascota sexo;

    @PastOrPresent(message = "La fecha de nacimiento no puede ser futura")
    private LocalDate fechaNacimiento;

    @NotNull(message = "El peso es obligatorio")
    @Positive(message = "El peso debe ser mayor a 0")
    private BigDecimal pesoActual;
}