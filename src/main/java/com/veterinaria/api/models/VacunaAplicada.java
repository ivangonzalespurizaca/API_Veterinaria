package com.veterinaria.api.models;

import com.veterinaria.api.models.enums.EstadoVacuna;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "vacuna_aplicada") @Data
public class VacunaAplicada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_vacuna_aplicada;
    @ManyToOne @JoinColumn(name = "id_mascota")
    private Mascota mascota;
    @ManyToOne @JoinColumn(name = "id_vacuna")
    private Vacuna vacuna;
    private LocalDate fecha_aplicacion;
    @Enumerated(EnumType.STRING)
    private EstadoVacuna estado;
}