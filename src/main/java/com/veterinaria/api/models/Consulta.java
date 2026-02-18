package com.veterinaria.api.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "consulta") @Data
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_consulta;

    @ManyToOne @JoinColumn(name = "id_mascota")
    private Mascota mascota;

    @ManyToOne @JoinColumn(name = "id_veterinario")
    private Usuario veterinario;

    private LocalDateTime fecha_consulta = LocalDateTime.now();
    private Double peso_actual;
    private Double temperatura;
    private String motivo;
    private String diagnostico;
    private String recomendaciones;

    @ManyToOne @JoinColumn(name = "id_tipo_medicamento")
    private TipoMedicamento tipoMedicamento;

    // Campos de tratamiento unificados
    private String nombre_medicamento;
    private String dosis;
    private String frecuencia;
    private String duracion;
    private LocalDate fecha_inicio;
}