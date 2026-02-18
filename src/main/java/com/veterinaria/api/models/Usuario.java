package com.veterinaria.api.models;

import com.veterinaria.api.models.enums.TipoRol;
import com.veterinaria.api.models.enums.TipoSexo;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "usuario") @Data
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_usuario;
    private String nombres_completo;
    private String dni;
    private String celular;
    private String correo;
    private String contrasenia;
    private String foto; // Aquí guardarás la URL de Firebase Storage
    @Enumerated(EnumType.STRING)
    private TipoRol rol;
}