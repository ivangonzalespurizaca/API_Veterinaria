package com.veterinaria.api.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "vacuna") @Data
public class Vacuna {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_vacuna;
    private String nombre_vacuna;
}