package com.cibertec.veterinaria.dto;

import com.cibertec.veterinaria.entity.enums.TipoEspecialidad;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VeterinarioInfoDTO {
    private String idUsuario;
    private Long idVeterinario;
    private String nombres;
    private String apellidos;
    private String email;
    private String fotoUrl;
    private Boolean activo;
    private String dni;
    private String numColegiatura;
    private TipoEspecialidad especialidad;
}