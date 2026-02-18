package com.veterinaria.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veterinaria.api.models.enums.EstadoVacuna;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "vacuna_aplicada")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacunaAplicada {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonProperty("id_vacuna_aplicada")
    private String id_vacuna_aplicada;

    @ManyToOne
    @JoinColumn(name = "id_mascota")
    @JsonProperty("mascota")
    private Mascota mascota;

    @ManyToOne
    @JoinColumn(name = "id_vacuna")
    @JsonProperty("vacuna")
    private Vacuna vacuna;

    @JsonProperty("fecha_aplicacion")
    @Column(name = "fecha_aplicacion")
    private LocalDate fecha_aplicacion;

    @Enumerated(EnumType.STRING)
    @JsonProperty("estado")
    private EstadoVacuna estado;
}