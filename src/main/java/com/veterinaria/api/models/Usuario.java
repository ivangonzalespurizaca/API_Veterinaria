package com.veterinaria.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veterinaria.api.models.enums.EstadoUsuario;
import com.veterinaria.api.models.enums.TipoRol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuario")
@Data // Genera Getters, Setters, toString, etc.
@NoArgsConstructor // Requerido por JPA
@AllArgsConstructor // Útil para crear usuarios de prueba rápidamente
public class Usuario {

    @Id
    @JsonProperty("id_usuario")
    private String id_usuario;

    @JsonProperty("nombres_completo")
    @Column(name = "nombres_completo")
    private String nombres_completo;

    @JsonProperty("dni")
    private String dni;

    @JsonProperty("celular")
    private String celular;

    @JsonProperty("correo")
    private String correo;

    private String contrasenia;

    @JsonProperty("foto")
    private String foto;

    @Enumerated(EnumType.STRING)
    @JsonProperty("estado")
    private EstadoUsuario estado;

    @Enumerated(EnumType.STRING)
    @JsonProperty("rol")
    private TipoRol rol;
}