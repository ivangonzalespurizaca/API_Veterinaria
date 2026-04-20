package com.cibertec.veterinaria.dto;

import com.cibertec.veterinaria.entity.enums.TipoGenero;
import com.cibertec.veterinaria.entity.enums.TipoRol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioInfoDTO {
    private String idUsuario;
    private String nombres;
    private String apellidos;
    private String dni;
    private String email;
    private TipoRol rol;
    private TipoGenero genero;
    private String celular;
    private Boolean activo;
    private String fotoUrl;
}
