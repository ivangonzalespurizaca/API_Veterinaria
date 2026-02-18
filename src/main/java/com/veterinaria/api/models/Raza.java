package com.veterinaria.api.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "raza") @Data
public class Raza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_raza;
    private String nombre_raza;
    @ManyToOne @JoinColumn(name = "id_especie")
    private Especie especie;
}