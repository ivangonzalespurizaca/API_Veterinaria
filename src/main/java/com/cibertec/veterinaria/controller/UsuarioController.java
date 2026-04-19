package com.cibertec.veterinaria.controller;

import com.cibertec.veterinaria.dto.UsuarioInfoDTO;
import com.cibertec.veterinaria.dto.UsuarioUpdateDTO;
import com.cibertec.veterinaria.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // 1. Editar datos básicos (Nombres, Apellidos, Celular, etc.)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO', 'CLIENTE')")
    public ResponseEntity<UsuarioInfoDTO> actualizarDatos(
            @PathVariable String id,
            @Valid @RequestBody UsuarioUpdateDTO dto) {
        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, dto));
    }

    // 2. Endpoint especial para subir/actualizar la foto a Cloudinary
    @PatchMapping("/{id}/foto")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO', 'CLIENTE')")
    public ResponseEntity<?> actualizarFoto(
            @PathVariable String id,
            @RequestParam("file") MultipartFile file) {
        try {
            String nuevaUrl = usuarioService.actualizarFotoPerfil(id, file);
            return ResponseEntity.ok(java.util.Map.of("fotoUrl", nuevaUrl));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al subir imagen: " + e.getMessage());
        }
    }
}