package com.cibertec.veterinaria.controller;

import com.cibertec.veterinaria.dto.*;
import com.cibertec.veterinaria.entity.enums.TipoEstadoCita;
import com.cibertec.veterinaria.service.CitaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;

    @GetMapping("/disponibilidad")
    public ResponseEntity<List<SlotDTO>> obtenerDisponibilidad(
            @RequestParam Long idVeterinario,
            @RequestParam LocalDate fecha) {

        return ResponseEntity.ok(citaService.obtenerHorasDisponibles(idVeterinario, fecha));
    }

    @PostMapping("/registrar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CLIENTE')")
    public ResponseEntity<CitaInfoDTO> registrar(@Valid @RequestBody CitaRegisterDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(citaService.registrarCita(dto));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO', 'CLIENTE')")
    public ResponseEntity<CitaInfoDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.obtenerPorId(id));
    }

    @GetMapping("/listar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<CitaInfoDTO>> listarTodas() {
        return ResponseEntity.ok(citaService.listarTodas());
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<CitaInfoDTO>> buscarPorCriterio(@RequestParam String criterio) {
        return ResponseEntity.ok(citaService.buscarTodoPorCriterio(criterio));
    }

    @GetMapping("/filtrar/estado/{estado}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<CitaInfoDTO>> filtrarPorEstado(@PathVariable TipoEstadoCita estado) {
        return ResponseEntity.ok(citaService.filtrarTodoPorEstado(estado));
    }

    @GetMapping("/veterinario/{idUsuarioVet}")
    @PreAuthorize("hasRole('VETERINARIO')")
    public ResponseEntity<List<CitaInfoDTO>> listarPorVeterinario(@PathVariable String idUsuarioVet) {
        return ResponseEntity.ok(citaService.listarPorVeterinario(idUsuarioVet));
    }

    @GetMapping("/veterinario/{idUsuarioVet}/hoy")
    @PreAuthorize("hasRole('VETERINARIO')")
    public ResponseEntity<List<CitaInfoDTO>> listarCitasDeHoy(@PathVariable String idUsuarioVet) {
        return ResponseEntity.ok(citaService.listarCitasDeHoy(idUsuarioVet));
    }

    @GetMapping("/veterinario/{idUsuarioVet}/buscar")
    @PreAuthorize("hasRole('VETERINARIO')")
    public ResponseEntity<List<CitaInfoDTO>> buscarCitasPropias(
            @PathVariable String idUsuarioVet,
            @RequestParam String criterio) {
        return ResponseEntity.ok(citaService.buscarCitasPropiasPorCriterio(idUsuarioVet, criterio));
    }

    @GetMapping("/cliente/{idUsuarioCliente}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<CitaInfoDTO>> listarPorCliente(@PathVariable String idUsuarioCliente) {
        return ResponseEntity.ok(citaService.listarPorCliente(idUsuarioCliente));
    }

    @GetMapping("/veterinario/{idUsuarioVet}/estado/{estado}")
    @PreAuthorize("hasRole('VETERINARIO')")
    public ResponseEntity<List<CitaInfoDTO>> filtrarPorVeterinarioYEstado(
            @PathVariable String idUsuarioVet,
            @PathVariable TipoEstadoCita estado) {

        List<CitaInfoDTO> citas = citaService.filtrarPorVeterinarioYEstado(idUsuarioVet, estado);

        if (citas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(citas);
    }

    @GetMapping("/cliente/{idUsuarioCliente}/estado/{estado}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<CitaInfoDTO>> filtrarPorClienteYEstado(
            @PathVariable String idUsuarioCliente,
            @PathVariable TipoEstadoCita estado) {

        List<CitaInfoDTO> citas = citaService.filtrarPorClienteYEstado(idUsuarioCliente, estado);

        if (citas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(citas);
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<CitaInfoDTO> cambiarEstado(
            @PathVariable Long id,
            @RequestParam TipoEstadoCita nuevoEstado) {
        return ResponseEntity.ok(citaService.cambiarEstado(id, nuevoEstado));
    }

    @PutMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('VETERINARIO', 'CLIENTE', 'ADMINISTRADOR')")
    public ResponseEntity<CitaInfoDTO> cancelar(
            @PathVariable Long id,
            @Valid @RequestBody CancelarCitaDTO dto) {
        return ResponseEntity.ok(citaService.cancelarCita(id, dto.getMotivo()));
    }

    @PutMapping("/{id}/reprogramar")
    @PreAuthorize("hasAnyRole('VETERINARIO', 'CLIENTE', 'ADMINISTRADOR')")
    public ResponseEntity<CitaInfoDTO> reprogramar(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate nuevaFecha,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime nuevaHora) {
        return ResponseEntity.ok(citaService.reprogramarCita(id, nuevaFecha, nuevaHora));
    }

    @GetMapping("/veterinario/{idVeterinario}/agenda")
    @PreAuthorize("hasRole('VETERINARIO')")
    public ResponseEntity<List<AgendaVetDTO>> obtenerAgenda(
            @PathVariable String idVeterinario,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        return ResponseEntity.ok(citaService.obtenerAgendaDelDia(idVeterinario, fecha));
    }
}