package com.cibertec.veterinaria.controller;

import com.cibertec.veterinaria.dto.HorarioInfoDTO;
import com.cibertec.veterinaria.dto.HorarioRegisterDTO;
import com.cibertec.veterinaria.dto.HorarioUpdateDTO;
import com.cibertec.veterinaria.service.HorarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/horarios")
@RequiredArgsConstructor
public class HorarioController {

    private final HorarioService horarioService;

    @PostMapping("/admin")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<HorarioInfoDTO> registrar(@Valid @RequestBody HorarioRegisterDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(horarioService.registrarHorario(dto));
    }

    @GetMapping("/disponibles/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO', 'CLIENTE')")
    public ResponseEntity<List<HorarioInfoDTO>> obtenerDisponibles(@PathVariable Long id) {
        return ResponseEntity.ok(horarioService.listarDisponiblesPorVeterinario(id));
    }

    @GetMapping("/admin/historial/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<List<HorarioInfoDTO>> obtenerTodo(@PathVariable Long id) {
        return ResponseEntity.ok(horarioService.listarTodoPorVeterinario(id));
    }

    @PutMapping("/admin/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<HorarioInfoDTO> actualizar(@PathVariable Long id, @Valid @RequestBody HorarioUpdateDTO dto) {
        return ResponseEntity.ok(horarioService.actualizarHorario(id, dto));
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<HorarioInfoDTO> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(horarioService.desactivarHorario(id));
    }

    @PatchMapping("/admin/{id}/activar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<HorarioInfoDTO> activar(@PathVariable Long id) {
        return ResponseEntity.ok(horarioService.activarHorario(id));
    }
}