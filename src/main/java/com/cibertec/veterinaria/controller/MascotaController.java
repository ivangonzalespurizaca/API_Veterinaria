package com.cibertec.veterinaria.controller;

import com.cibertec.veterinaria.dto.MascotaInfoDTO;
import com.cibertec.veterinaria.dto.MascotaRegisterDTO;
import com.cibertec.veterinaria.dto.MascotaUpdateDTO;
import com.cibertec.veterinaria.entity.enums.TipoEspecie;
import com.cibertec.veterinaria.service.MascotaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mascotas")
@RequiredArgsConstructor
public class MascotaController {

    private final MascotaService mascotaService;

    // 1. Registrar Mascota
    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMINISTRADOR')")
    public ResponseEntity<MascotaInfoDTO> registrar(@Valid @RequestBody MascotaRegisterDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mascotaService.guardarMascota(dto));
    }

    // 2. Obtener Mis Mascotas (El cliente pasa su id_usuario)
    @GetMapping("/cliente/{idUsuario}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'VETERINARIO', 'ADMINISTRADOR')")
    public ResponseEntity<List<MascotaInfoDTO>> listarPorCliente(@PathVariable String idUsuario) {
        return ResponseEntity.ok(mascotaService.listarPorCliente(idUsuario));
    }

    // 3. Obtener detalles de una mascota específica
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'VETERINARIO', 'ADMINISTRADOR')")
    public ResponseEntity<MascotaInfoDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(mascotaService.obtenerPorId(id));
    }

    // 4. Editar datos de la mascota
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMINISTRADOR')")
    public ResponseEntity<MascotaInfoDTO> actualizar(@PathVariable Long id, @Valid @RequestBody MascotaUpdateDTO dto) {
        return ResponseEntity.ok(mascotaService.editarMascota(id, dto));
    }

    // 5. Subir/Actualizar Foto (PATCH)
    @PatchMapping("/{id}/foto")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMINISTRADOR')")
    public ResponseEntity<Map<String, String>> actualizarFoto(@PathVariable Long id, @RequestParam("archivo") MultipartFile archivo) throws IOException {
        String url = mascotaService.actualizarFotoMascota(id, archivo);
        return ResponseEntity.ok(Map.of("fotoUrl", url));
    }

    // 6. Eliminar (Baja Lógica)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMINISTRADOR')")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        mascotaService.eliminarLogica(id);
        return ResponseEntity.ok(Map.of("message", "Mascota dada de baja correctamente"));
    }

    // 7. Buscar por DNI del dueño
    @GetMapping("/admin/buscar-por-dni")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<List<MascotaInfoDTO>> buscarPorDni(@RequestParam String dni) {
        return ResponseEntity.ok(mascotaService.buscarPorDniDuenio(dni));
    }

    // 8. Filtrar por Especie
    @GetMapping("/admin/filtrar-especie")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<List<MascotaInfoDTO>> listarPorEspecie(@RequestParam TipoEspecie especie) {
        return ResponseEntity.ok(mascotaService.listarPorEspecie(especie));
    }
}