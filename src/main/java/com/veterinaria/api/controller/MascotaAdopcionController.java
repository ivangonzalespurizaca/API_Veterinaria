package com.veterinaria.api.controller;


import com.veterinaria.api.DTO.MascotaAdopcionDTO;
import com.veterinaria.api.models.MascotaAdopcion;
import com.veterinaria.api.models.enums.EstadoAdopcion;
import com.veterinaria.api.repository.MascotaAdopcionRepository;
import com.veterinaria.api.services.MascotaAdopcionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mascotas-adopcion")
@CrossOrigin(origins = "*")
public class MascotaAdopcionController {

    private final MascotaAdopcionService service;
    private final MascotaAdopcionRepository mascotaAdopcionRepository;

    public MascotaAdopcionController(MascotaAdopcionService service,MascotaAdopcionRepository mascotaAdopcionRepository) {
        this.service = service;
        this.mascotaAdopcionRepository = mascotaAdopcionRepository;
    }

    @GetMapping
    public ResponseEntity<List<MascotaAdopcion>> listar() {
        return ResponseEntity.ok(service.listar());
    }
    @GetMapping("/mostrar")
    public ResponseEntity<List<MascotaAdopcion>> listarEnAdopcion() {
        return ResponseEntity.ok(
                mascotaAdopcionRepository.findByEstado(EstadoAdopcion.Disponible)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MascotaAdopcion> buscar(@PathVariable String id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<MascotaAdopcion> crear(@RequestBody MascotaAdopcionDTO dto) {
        return ResponseEntity.ok(service.guardar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MascotaAdopcion> actualizar(
            @PathVariable String id,
            @RequestBody MascotaAdopcionDTO dto) {

        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    // 🔥 Endpoint para marcar como adoptado
    @PatchMapping("/{id}/adoptar")
    public ResponseEntity<MascotaAdopcion> adoptar(@PathVariable String id) {
        return ResponseEntity.ok(service.marcarComoAdoptado(id));
    }
}