package com.veterinaria.api.controller;

import com.veterinaria.api.DTO.RazaDTO;
import com.veterinaria.api.models.Raza;
import com.veterinaria.api.services.RazaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/razas")
@CrossOrigin(origins = "*")
public class RazaController {

    private final RazaService service;

    public RazaController(RazaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Raza>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Raza> buscar(@PathVariable String id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Raza> crear(@RequestBody RazaDTO dto) {
        return ResponseEntity.ok(service.guardar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Raza> actualizar(
            @PathVariable String id,
            @RequestBody RazaDTO dto) {

        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
