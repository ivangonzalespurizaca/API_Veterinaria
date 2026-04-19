package com.cibertec.veterinaria.controller;

import com.cibertec.veterinaria.dto.LogSistemaInfoDTO;
import com.cibertec.veterinaria.service.LogSistemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/logs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class LogSistemaController {

    private final LogSistemaService logService;

    @GetMapping
    public ResponseEntity<List<LogSistemaInfoDTO>> listarLogs(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

        List<LogSistemaInfoDTO> logs;

        if (inicio != null && fin != null) {
            logs = logService.listarLogsPorRangoFechas(inicio, fin);
        } else {
            logs = logService.listarLogsPorFecha();
        }

        return logs.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(logs);
    }
}