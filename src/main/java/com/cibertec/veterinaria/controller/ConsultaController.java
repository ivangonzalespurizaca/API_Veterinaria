package com.cibertec.veterinaria.controller;

import com.cibertec.veterinaria.dto.ConsultaInfoDTO;
import com.cibertec.veterinaria.dto.ConsultaRegisterDTO;
import com.cibertec.veterinaria.service.ConsultaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultas")
@RequiredArgsConstructor
public class ConsultaController {
    private final ConsultaService consultaService;

    @PostMapping("/registrar")
    @PreAuthorize("hasAnyRole('VETERINARIO', 'ADMINISTRADOR')")
    public ResponseEntity<ConsultaInfoDTO> registrarAtencionCompleta(@Valid @RequestBody ConsultaRegisterDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(consultaService.registrarAtencionCompleta(dto));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO', 'CLIENTE')")
    public ResponseEntity<ConsultaInfoDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.obtenerPorId(id));
    }

    @GetMapping("/mascota/{idMascota}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO', 'CLIENTE')")
    public ResponseEntity<List<ConsultaInfoDTO>> listarPorMascota(@PathVariable Long idMascota) {
        List<ConsultaInfoDTO> historial = consultaService.listarPorMascota(idMascota);
        return historial.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(historial);
    }

    @GetMapping("/veterinario/{idUsuarioVet}")
    @PreAuthorize("hasAnyRole('VETERINARIO', 'ADMINISTRADOR')")
    public ResponseEntity<List<ConsultaInfoDTO>> listarPorVeterinario(
            @PathVariable String idUsuarioVet,
            @RequestParam(required = false)
            @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE)
            java.time.LocalDate fecha) {

        List<ConsultaInfoDTO> resultado;

        if (fecha != null) {
            resultado = consultaService.listarPorVeterinarioYFecha(idUsuarioVet, fecha);
        } else {
            resultado = consultaService.listarPorVeterinario(idUsuarioVet);
        }

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('VETERINARIO', 'ADMINISTRADOR')")
    public ResponseEntity<List<ConsultaInfoDTO>> buscarPorCriterio(@RequestParam String criterio) {
        return ResponseEntity.ok(consultaService.buscarPorCriterio(criterio));
    }

    @GetMapping("/listar")
    @PreAuthorize("hasAnyRole('VETERINARIO', 'ADMINISTRADOR')")
    public ResponseEntity<List<ConsultaInfoDTO>> listarTodas() {
        return ResponseEntity.ok(consultaService.listarTodas());
    }

}
