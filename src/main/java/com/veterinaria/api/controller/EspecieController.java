package com.veterinaria.api.controller;


import com.veterinaria.api.models.Especie;
import com.veterinaria.api.services.EspecieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/especies")
@CrossOrigin(origins = "*")
public class EspecieController {

    private final EspecieService service;

    public EspecieController(EspecieService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Especie>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Especie> buscar(@PathVariable String id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Especie> crear(@RequestBody Especie especie) {
        return ResponseEntity.ok(service.guardar(especie));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Especie> actualizar(
            @PathVariable String id,
            @RequestBody Especie especie) {

        return ResponseEntity.ok(service.actualizar(id, especie));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}