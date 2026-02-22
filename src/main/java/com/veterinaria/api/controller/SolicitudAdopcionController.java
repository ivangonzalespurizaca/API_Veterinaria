package com.veterinaria.api.controller;


import com.veterinaria.api.DTO.SolicitudAdopcionDTO;
import com.veterinaria.api.models.SolicitudAdopcion;
import com.veterinaria.api.services.SolicitudAdopcionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
@CrossOrigin(origins = "*")
public class SolicitudAdopcionController {

    private final SolicitudAdopcionService service;

    public SolicitudAdopcionController(SolicitudAdopcionService service) {
        this.service = service;
    }

    // Listar todas
    @GetMapping
    public ResponseEntity<List<SolicitudAdopcion>> listar() {
        return ResponseEntity.ok(service.listarTodas());
    }

    // Listar pendientes
    @GetMapping("/pendientes")
    public ResponseEntity<List<SolicitudAdopcion>> pendientes() {
        return ResponseEntity.ok(service.listarPendientes());
    }

    // Crear solicitud
    @PostMapping
    public ResponseEntity<SolicitudAdopcion> crear(@RequestBody SolicitudAdopcionDTO dto) {
        return ResponseEntity.ok(service.crearSolicitud(dto));
    }

    // Aprobar
    @PatchMapping("/{id}/aprobar")
    public ResponseEntity<SolicitudAdopcion> aprobar(
            @PathVariable String id,
            @RequestParam String idGestor) {

        return ResponseEntity.ok(service.aprobarSolicitud(id, idGestor));
    }

    // Rechazar
    @PatchMapping("/{id}/rechazar")
    public ResponseEntity<SolicitudAdopcion> rechazar(
            @PathVariable String id,
            @RequestParam String idGestor) {

        return ResponseEntity.ok(service.rechazarSolicitud(id, idGestor));
    }
}