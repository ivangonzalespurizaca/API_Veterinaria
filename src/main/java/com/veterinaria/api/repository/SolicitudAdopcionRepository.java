package com.veterinaria.api.repository;

import com.veterinaria.api.models.SolicitudAdopcion;
import com.veterinaria.api.models.enums.EstadoSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitudAdopcionRepository extends JpaRepository<SolicitudAdopcion, String> {

    List<SolicitudAdopcion> findByEstadoSolicitud(EstadoSolicitud estado);

    List<SolicitudAdopcion> findByClienteIdUsuario(String idUsuario);
}
