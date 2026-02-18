package com.veterinaria.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "consulta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonProperty("id_consulta")
    private String id_consulta;

    @ManyToOne
    @JoinColumn(name = "id_mascota")
    @JsonProperty("mascota")
    private Mascota mascota;

    @ManyToOne
    @JoinColumn(name = "id_veterinario")
    @JsonProperty("veterinario")
    private Usuario veterinario;

    @JsonProperty("fecha_consulta")
    @Column(name = "fecha_consulta")
    private LocalDateTime fecha_consulta = LocalDateTime.now();

    @JsonProperty("peso_actual")
    private Double peso_actual;

    @JsonProperty("temperatura")
    private Double temperatura;

    @JsonProperty("motivo")
    private String motivo;

    @JsonProperty("diagnostico")
    private String diagnostico;

    @JsonProperty("recomendaciones")
    private String recomendaciones;

    @ManyToOne
    @JoinColumn(name = "id_tipo_medicamento")
    @JsonProperty("tipo_medicamento")
    private TipoMedicamento tipoMedicamento;

    @JsonProperty("nombre_medicamento")
    private String nombre_medicamento;

    @JsonProperty("dosis")
    private String dosis;

    @JsonProperty("frecuencia")
    private String frecuencia;

    @JsonProperty("duracion")
    private String duracion;

    @JsonProperty("fecha_inicio")
    private LocalDate fecha_inicio;
}