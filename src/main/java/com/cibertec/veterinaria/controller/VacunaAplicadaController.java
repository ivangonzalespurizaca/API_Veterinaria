package com.cibertec.veterinaria.controller;

import com.cibertec.veterinaria.dto.VacunaAplicadaInfoDTO;
import com.cibertec.veterinaria.service.VacunaAplicadaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/vacunas-aplicadas")
@RequiredArgsConstructor
public class VacunaAplicadaController {

    private final VacunaAplicadaService vacunaService;

    @GetMapping("/mascota/{idMascota}/carnet")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO', 'CLIENTE')")
    public ResponseEntity<List<VacunaAplicadaInfoDTO>> listarCarnet(@PathVariable Long idMascota) {
        List<VacunaAplicadaInfoDTO> carnet = vacunaService.listarCarnetPorMascota(idMascota);
        return carnet.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(carnet);
    }

    @GetMapping("/mascota/{idMascota}/pendientes")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO', 'CLIENTE')")
    public ResponseEntity<List<VacunaAplicadaInfoDTO>> listarPendientes(@PathVariable Long idMascota) {
        List<VacunaAplicadaInfoDTO> pendientes = vacunaService.listarPendientesPorMascota(idMascota);
        return pendientes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(pendientes);
    }

    @PatchMapping("/{id}/reprogramar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<VacunaAplicadaInfoDTO> reprogramar(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate nuevaFecha) {
        return ResponseEntity.ok(vacunaService.reprogramarDosis(id, nuevaFecha));
    }

    @PutMapping("/{id}/confirmar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<VacunaAplicadaInfoDTO> confirmar(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaReal) {
        return ResponseEntity.ok(vacunaService.confirmarAplicacion(id, fechaReal));
    }
}