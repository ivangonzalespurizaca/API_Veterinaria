package com.veterinaria.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "especie")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Especie {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonProperty("id_especie")
    private String id_especie;

    @JsonProperty("nombre_especie")
    @Column(name = "nombre_especie")
    private String nombre_especie;

    @JsonProperty("definicion")
    private String definicion;

    @JsonProperty("imagen")
    private String imagen;
}