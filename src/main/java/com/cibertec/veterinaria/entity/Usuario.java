package com.cibertec.veterinaria.entity;

import com.cibertec.veterinaria.entity.enums.TipoGenero;
import com.cibertec.veterinaria.entity.enums.TipoRol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario", schema = "veterinaria_app")
public class Usuario {
    @Id
    @Column(name = "id_usuario", length = 128)
    private String idUsuario;

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 15)
    private String celular;

    @Column(nullable = false, unique = true, length = 8)
    private String dni;

    @Column(name = "foto_url")
    private String fotoUrl;

    @Enumerated(EnumType.STRING)
    private TipoGenero genero;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoRol rol;

    @Builder.Default
    private Boolean activo = true;
}
