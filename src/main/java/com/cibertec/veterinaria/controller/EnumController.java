package com.cibertec.veterinaria.controller;

import com.cibertec.veterinaria.entity.enums.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/enums")
public class EnumController {

    @GetMapping("/dias-semana")
    public TipoDiaSemana[] getDiasSemana() {
        return TipoDiaSemana.values();
    }

    @GetMapping("/especies")
    public ResponseEntity<TipoEspecie[]> getEspecies() {
        return ResponseEntity.ok(TipoEspecie.values());
    }

    @GetMapping("/especialidades")
    public ResponseEntity<TipoEspecialidad[]> getEspecialidades() {
        return ResponseEntity.ok(TipoEspecialidad.values());
    }

    @GetMapping("/generos")
    public ResponseEntity<TipoGenero[]> getGeneros() {
        return ResponseEntity.ok(TipoGenero.values());
    }

    @GetMapping("/sexo-mascota")
    public ResponseEntity<TipoSexoMascota[]> getSexoMascota() {
        return ResponseEntity.ok(TipoSexoMascota.values());
    }

    @GetMapping("/rol")
    public ResponseEntity<TipoRol[]> getRoles() {
        return ResponseEntity.ok(TipoRol.values());
    }

    @GetMapping("/estado-cita")
    public ResponseEntity<TipoEstadoCita[]> getEstadoCita() {
        return ResponseEntity.ok(TipoEstadoCita.values());
    }

    @GetMapping("/estado-vacuna")
    public ResponseEntity<TipoEstadoVacuna[]> getEstadoVacuna() {
        return ResponseEntity.ok(TipoEstadoVacuna.values());
    }

}