package com.veterinaria.api.models;

import com.veterinaria.api.models.enums.TipoSexo;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "mascota") @Data
public class Mascota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_mascota;
    private String nombre_mascota;

    @ManyToOne @JoinColumn(name = "id_raza")
    private Raza raza;

    @ManyToOne @JoinColumn(name = "id_cliente")
    private Usuario cliente;

    @Enumerated(EnumType.STRING)
    private TipoSexo sexo;

    private LocalDate fecha_nacimiento;
    private Double peso_inicial;
    private String foto;
    private String codigo_qr;
}