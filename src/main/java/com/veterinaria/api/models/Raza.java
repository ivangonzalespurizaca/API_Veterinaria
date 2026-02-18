package com.veterinaria.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "raza")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Raza {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonProperty("id_raza")
    private String id_raza;

    @JsonProperty("nombre_raza")
    @Column(name = "nombre_raza")
    private String nombre_raza;

    @ManyToOne
    @JoinColumn(name = "id_especie")
    @JsonProperty("especie")
    private Especie especie;
}