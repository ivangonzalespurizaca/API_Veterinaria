package com.veterinaria.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "detalle_veterinario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleVeterinario {

    @Id
    @Column(name = "id_usuario")
    @JsonProperty("id_usuario")
    private String idUsuario;

    @JsonProperty("especialidad")
    private String especialidad;

    @JsonProperty("numero_colegiatura")
    private String numero_colegiatura;

    @JsonProperty("sede")
    private String sede;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_usuario")
    @JsonProperty("usuario")
    private Usuario usuario;
}