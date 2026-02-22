package com.veterinaria.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veterinaria.api.models.enums.EstadoSolicitud;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "solicitud_adopcion")
@Data
@NoArgsConstructor // Requerido por Hibernate
@AllArgsConstructor
public class SolicitudAdopcion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonProperty("id_solicitud") // Mapeo exacto para Postman
    private String id_solicitud;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    @JsonProperty("cliente") // Incluye el objeto Usuario (Cliente) completo
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "id_mascota_adopcion")
    @JsonProperty("mascota_adopcion")
    private MascotaAdopcion mascotaAdopcion;

    @JsonProperty("fecha_solicitud")
    @Column(name = "fecha_solicitud")
    private LocalDate fecha_solicitud = LocalDate.now();

    @JsonProperty("fecha_entrevista")
    @Column(name = "fecha_entrevista")
    private LocalDate fecha_entrevista;

    @Column(name = "estado_solicitud")
    @Enumerated(EnumType.STRING)
    private EstadoSolicitud estadoSolicitud = EstadoSolicitud.Pendiente;

}