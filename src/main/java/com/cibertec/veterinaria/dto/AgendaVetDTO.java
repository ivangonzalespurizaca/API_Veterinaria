package com.cibertec.veterinaria.dto;

import com.cibertec.veterinaria.entity.enums.TipoEstadoCita;

import java.time.LocalTime;

public record AgendaVetDTO(
        LocalTime hora,
        boolean libre,
        String paciente,
        TipoEstadoCita estadoCita
) {}