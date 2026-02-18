package com.veterinaria.api.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "especie") @Data
public class Especie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_especie;
    private String nombre_especie;
    private String definicion;
    private String imagen;
}