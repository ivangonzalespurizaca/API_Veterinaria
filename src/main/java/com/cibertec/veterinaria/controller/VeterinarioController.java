package com.cibertec.veterinaria.controller;

import com.cibertec.veterinaria.dto.VeterinarioInfoDTO;
import com.cibertec.veterinaria.dto.VeterinarioRegisterDTO;
import com.cibertec.veterinaria.dto.VeterinarioUpdateDTO;
import com.cibertec.veterinaria.service.VeterinarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/veterinarios")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class VeterinarioController {

    private final VeterinarioService veterinarioService;

    @GetMapping
    public ResponseEntity<List<VeterinarioInfoDTO>> listarTodos() {
        return ResponseEntity.ok(veterinarioService.listarTodos());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<VeterinarioInfoDTO>> buscar(@RequestParam String query) {
        return ResponseEntity.ok(veterinarioService.buscarPorFiltro(query));
    }

    @PostMapping
    public ResponseEntity<VeterinarioInfoDTO> registrar(@Valid @RequestBody VeterinarioRegisterDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(veterinarioService.registrar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VeterinarioInfoDTO> actualizarProfesional(
            @PathVariable Long id,
            @Valid @RequestBody VeterinarioUpdateDTO dto) {
        return ResponseEntity.ok(veterinarioService.actualizarDatosProfesionales(id, dto));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(
            @PathVariable Long id,
            @RequestParam boolean activo) {
        veterinarioService.cambiarEstado(id, activo);
        String mensaje = activo ? "Veterinario activado correctamente" : "Veterinario desactivado correctamente";
        return ResponseEntity.ok(Map.of("message", mensaje));
    }
}