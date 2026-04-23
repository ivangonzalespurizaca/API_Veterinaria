package com.cibertec.veterinaria.controller;

import com.cibertec.veterinaria.dto.VacunaAplicadaDTO;
import com.cibertec.veterinaria.dto.VacunaAplicadaInfoDTO;
import com.cibertec.veterinaria.dto.VacunaAplicadaProgramadaDTO;
import com.cibertec.veterinaria.dto.VacunaConfirmadaDTO;
import com.cibertec.veterinaria.service.VacunaAplicadaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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

    // 1. Obtener todo el historial (Carnet)
    @GetMapping("/mascota/{idMascota}/carnet")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO', 'CLIENTE')")
    public ResponseEntity<List<VacunaAplicadaInfoDTO>> listarCarnet(@PathVariable Long idMascota) {
        List<VacunaAplicadaInfoDTO> carnet = vacunaService.listarCarnetPorMascota(idMascota);
        return carnet.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(carnet);
    }

    // 2. Programar una dosis futura (Escenario A)
    // CAMBIO: Ahora usa VacunaAplicadaProgramadaDTO para recibir la fecha obligatoria
    @PostMapping("/mascota/{idMascota}/programar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<VacunaAplicadaInfoDTO> programarDosis(
            @PathVariable Long idMascota,
            @Valid @RequestBody VacunaAplicadaProgramadaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(vacunaService.programarDosisFutura(idMascota, dto));
    }

    // 3. Confirmar una vacuna programada usando la Consulta (Escenario B)
    @PutMapping("/consulta/{idConsulta}/confirmar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<VacunaAplicadaInfoDTO> confirmar(
            @PathVariable Long idConsulta,
            @Valid @RequestBody VacunaConfirmadaDTO dto) {
        return ResponseEntity.ok(vacunaService.aplicarVacunaProgramada(dto, idConsulta));
    }

    // 4. Aplicar una vacuna inmediata durante la consulta (Escenario C)
    // CAMBIO: Usa VacunaAplicadaDTO (el que no tiene fecha futura ni recordatorios)
    @PostMapping("/mascota/{idMascota}/consulta/{idConsulta}/aplicar-inmediata")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<VacunaAplicadaInfoDTO> aplicarInmediata(
            @PathVariable Long idMascota,
            @PathVariable Long idConsulta,
            @Valid @RequestBody VacunaAplicadaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(vacunaService.aplicarVacunaInmediata(idMascota, dto, idConsulta));
    }
}