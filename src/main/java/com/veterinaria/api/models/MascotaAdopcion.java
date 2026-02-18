package com.veterinaria.api.models;

import com.veterinaria.api.models.enums.EstadoAdopcion;
import com.veterinaria.api.models.enums.TipoSexo;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "mascota_adopcion") @Data
public class MascotaAdopcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_mascota_adopcion;
    @ManyToOne @JoinColumn(name = "id_raza")
    private Raza raza;
    private String nombre_mascota;
    @Enumerated(EnumType.STRING)
    private TipoSexo sexo;
    private String edad_estimada;
    @Enumerated(EnumType.STRING)
    private EstadoAdopcion estado = EstadoAdopcion.Disponible;
    private String foto;
}