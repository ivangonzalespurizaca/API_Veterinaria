package com.cibertec.veterinaria.entity;

import com.cibertec.veterinaria.entity.enums.TipoEstadoVacuna;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vacuna_aplicada", schema = "veterinaria_app")
public class VacunaAplicada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAplicacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mascota", nullable = false)
    private Mascota mascota;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vacuna", nullable = false)
    private Vacuna vacuna;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_consulta")
    private Consulta consulta;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private TipoEstadoVacuna estado;

    @Column(name = "fecha_aplicacion")
    private LocalDate fechaAplicacion;

    @Column(name = "fecha_programada")
    private LocalDate fechaProgramada;

    @Builder.Default
    @Column(name = "nro_dosis")
    private Integer nroDosis = 1;

    @Column(columnDefinition = "TEXT")
    private String observaciones;
}