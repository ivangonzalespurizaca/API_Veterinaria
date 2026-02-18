package com.veterinaria.api.models;

import com.veterinaria.api.models.enums.EstadoSolicitud;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "solicitud_adopcion") @Data
public class SolicitudAdopcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_solicitud;
    @ManyToOne @JoinColumn(name = "id_cliente")
    private Usuario cliente;
    @ManyToOne @JoinColumn(name = "id_mascota_adopcion")
    private MascotaAdopcion mascotaAdopcion;
    private LocalDate fecha_solicitud = LocalDate.now();
    private LocalDate fecha_entrevista;
    @Enumerated(EnumType.STRING)
    private EstadoSolicitud estado_solicitud = EstadoSolicitud.Pendiente;
    @ManyToOne @JoinColumn(name = "id_usuario_gestor")
    private Usuario gestor;
}