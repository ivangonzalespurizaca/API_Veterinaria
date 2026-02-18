package com.veterinaria.api.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "detalle_veterinario") @Data
public class DetalleVeterinario {
    @Id
    @Column(name = "id_usuario")
    private Integer idUsuario;
    private String especialidad;
    private String numero_colegiatura;
    private String sede;

    @OneToOne
    @MapsId @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}