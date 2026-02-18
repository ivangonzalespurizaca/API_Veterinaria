package com.veterinaria.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tipo_medicamento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoMedicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonProperty("id_tipo_medicamento")
    private String id_tipo_medicamento;

    @JsonProperty("nombre_tipo")
    @Column(name = "nombre_tipo")
    private String nombre_tipo;
}