package com.cibertec.veterinaria.controller;

import com.cibertec.veterinaria.dto.VacunaInfoDTO;
import com.cibertec.veterinaria.dto.VacunaRegisterDTO;
import com.cibertec.veterinaria.dto.VacunaUpdateDTO;
import com.cibertec.veterinaria.service.VacunaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vacunas")
@RequiredArgsConstructor
public class VacunaController{
    private final VacunaService vacunaService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO', 'CLIENTE')")
    public ResponseEntity<List<VacunaInfoDTO>> listarActivas() {
        return ResponseEntity.ok(vacunaService.listarVacunas());
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/mantenimiento")
    public ResponseEntity<List<VacunaInfoDTO>> listarParaAdmin() {
        return ResponseEntity.ok(vacunaService.listarTodas());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO', 'CLIENTE')")
    public ResponseEntity<VacunaInfoDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(vacunaService.obtenerVacunaPorId(id));
    }

    @PostMapping("/mantenimiento")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<VacunaInfoDTO> guardar(@Valid @RequestBody VacunaRegisterDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vacunaService.guardarVacuna(dto));
    }

    @PutMapping("/mantenimiento/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<VacunaInfoDTO> actualizar(@PathVariable Long id, @Valid @RequestBody VacunaUpdateDTO dto) {
        return ResponseEntity.ok(vacunaService.actualizarVacuna(id, dto));
    }

    @PatchMapping("/mantenimiento/{id}/desactivar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        vacunaService.desactivarVacuna(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/mantenimiento/{id}/activar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> activar(@PathVariable Long id) {
        vacunaService.activarVacuna(id);
        return ResponseEntity.noContent().build();
    }
}
