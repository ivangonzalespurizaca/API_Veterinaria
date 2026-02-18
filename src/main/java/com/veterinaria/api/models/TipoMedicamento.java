package com.veterinaria.api.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tipo_medicamento") @Data
public class TipoMedicamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_tipo_medicamento;
    private String nombre_tipo;
}