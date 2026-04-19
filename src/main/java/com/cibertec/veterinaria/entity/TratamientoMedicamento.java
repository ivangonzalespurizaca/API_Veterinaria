package com.cibertec.veterinaria.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tratamiento_medicamento", schema = "veterinaria_app")
public class TratamientoMedicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTratamiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_consulta", nullable = false)
    private Consulta consulta;

    @Column(name = "nombre_medicamento", nullable = false, length = 100)
    private String nombreMedicamento;

    @Column(length = 100)
    private String dosis;

    @Column(length = 100)
    private String frecuencia;

    @Column(name = "duracion_dias")
    private Integer duracionDias;
}