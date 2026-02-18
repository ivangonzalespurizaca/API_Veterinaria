package com.veterinaria.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veterinaria.api.models.enums.TipoSexo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "mascota")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonProperty("id_mascota")
    private String id_mascota;

    @JsonProperty("nombre_mascota")
    @Column(name = "nombre_mascota")
    private String nombre_mascota;

    @ManyToOne
    @JoinColumn(name = "id_raza")
    @JsonProperty("raza")
    private Raza raza;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    @JsonProperty("cliente")
    private Usuario cliente;

    @Enumerated(EnumType.STRING)
    @JsonProperty("sexo")
    private TipoSexo sexo;

    @JsonProperty("fecha_nacimiento")
    @Column(name = "fecha_nacimiento")
    private LocalDate fecha_nacimiento;

    @JsonProperty("peso_inicial")
    @Column(name = "peso_inicial")
    private Double peso_inicial;

    @JsonProperty("foto")
    private String foto;

    @JsonProperty("codigo_qr")
    @Column(name = "codigo_qr")
    private String codigo_qr;
}