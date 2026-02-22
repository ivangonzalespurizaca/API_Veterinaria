package com.veterinaria.api.services;

import com.veterinaria.api.DTO.SolicitudAdopcionDTO;
import com.veterinaria.api.models.*;
import com.veterinaria.api.models.enums.EstadoAdopcion;
import com.veterinaria.api.models.enums.EstadoSolicitud;
import com.veterinaria.api.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolicitudAdopcionService {

    private final SolicitudAdopcionRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;
    private final MascotaAdopcionRepository mascotaRepository;

    public SolicitudAdopcionService(
            SolicitudAdopcionRepository solicitudRepository,
            UsuarioRepository usuarioRepository,
            MascotaAdopcionRepository mascotaRepository) {

        this.solicitudRepository = solicitudRepository;
        this.usuarioRepository = usuarioRepository;
        this.mascotaRepository = mascotaRepository;
    }

    public List<SolicitudAdopcion> listarTodas() {
        return solicitudRepository.findAll();
    }

    public List<SolicitudAdopcion> listarPendientes() {
        return solicitudRepository.findByEstadoSolicitud(EstadoSolicitud.Pendiente);
    }
    @Transactional
    public SolicitudAdopcion crearSolicitud(SolicitudAdopcionDTO dto) {
        Usuario usuario = usuarioRepository
                .findByFirebaseUid(dto.getFirebaseUid())
                .orElseGet(() -> {
                    Usuario nuevo = new Usuario();
                    nuevo.setFirebaseUid(dto.getFirebaseUid());
                    return usuarioRepository.save(nuevo);
                });
        MascotaAdopcion mascota = mascotaRepository.findById(dto.getIdMascota())
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        if (mascota.getEstado() == EstadoAdopcion.Adoptado) {
            throw new RuntimeException("La mascota ya fue adoptada");
        }

        mascota.setEstado(EstadoAdopcion.En_Entrevista);
        mascotaRepository.save(mascota);

        SolicitudAdopcion solicitud = new SolicitudAdopcion();
        solicitud.setCliente(usuario);
        solicitud.setMascotaAdopcion(mascota);

        return solicitudRepository.save(solicitud);
    }

    @Transactional
    public SolicitudAdopcion aprobarSolicitud(String idSolicitud, String idGestor) {

        SolicitudAdopcion solicitud = solicitudRepository.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (solicitud.getEstadoSolicitud() != EstadoSolicitud.Pendiente) {
            throw new RuntimeException("La solicitud ya fue procesada");
        }

        Usuario gestor = usuarioRepository.findById(idGestor)
                .orElseThrow(() -> new RuntimeException("Gestor no encontrado"));

        MascotaAdopcion mascota = solicitud.getMascotaAdopcion();

        if (mascota.getEstado() == EstadoAdopcion.Adoptado) {
            throw new RuntimeException("La mascota ya fue adoptada");
        }

        solicitud.setEstadoSolicitud(EstadoSolicitud.Aprobado);

        mascota.setEstado(EstadoAdopcion.Adoptado);

        mascotaRepository.save(mascota);

        return solicitudRepository.save(solicitud);
    }

    @Transactional
    public SolicitudAdopcion rechazarSolicitud(String idSolicitud, String idGestor) {

        SolicitudAdopcion solicitud = solicitudRepository.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (solicitud.getEstadoSolicitud() != EstadoSolicitud.Pendiente) {
            throw new RuntimeException("La solicitud ya fue procesada");
        }

        Usuario gestor = usuarioRepository.findById(idGestor)
                .orElseThrow(() -> new RuntimeException("Gestor no encontrado"));

        solicitud.setEstadoSolicitud(EstadoSolicitud.Rechazado);


        return solicitudRepository.save(solicitud);
    }
}