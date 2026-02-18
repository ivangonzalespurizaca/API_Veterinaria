package com.veterinaria.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veterinaria.api.models.enums.EstadoAdopcion;
import com.veterinaria.api.models.enums.TipoSexo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mascota_adopcion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MascotaAdopcion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonProperty("id_mascota_adopcion")
    private String id_mascota_adopcion;

    @ManyToOne
    @JoinColumn(name = "id_raza")
    @JsonProperty("raza")
    private Raza raza;

    @JsonProperty("nombre_mascota")
    @Column(name = "nombre_mascota")
    private String nombre_mascota;

    @Enumerated(EnumType.STRING)
    @JsonProperty("sexo")
    private TipoSexo sexo;

    @JsonProperty("edad_estimada")
    @Column(name = "edad_estimada")
    private String edad_estimada;

    @Enumerated(EnumType.STRING)
    @JsonProperty("estado")
    private EstadoAdopcion estado = EstadoAdopcion.Disponible;

    @JsonProperty("foto")
    private String foto;
}