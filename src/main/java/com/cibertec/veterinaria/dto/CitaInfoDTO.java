package com.cibertec.veterinaria.dto;

import com.cibertec.veterinaria.entity.enums.TipoEstadoCita;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CitaInfoDTO {

    private Long idCita;
    private String nombreMascota;
    private String fotoMascotaUrl;
    private String nombreCliente;
    private String celularCliente;
    private String nombreVeterinario;
    private String especialidadVeterinario;
    private LocalDate fecha;
    private LocalTime hora;
    private TipoEstadoCita estado;
    private String motivo;

}