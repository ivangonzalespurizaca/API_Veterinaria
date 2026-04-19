package com.cibertec.veterinaria.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaInfoDTO {
    private Long idConsulta;
    private Long idCita;

    private String nombreMascota;

    private LocalDate fechaConsulta;
    private BigDecimal pesoActual;
    private BigDecimal temperatura;
    private String diagnostico;
    private String recomendaciones;
    private String nombreVeterinario;

    private List<TratamientoInfoDTO> tratamientos;
    private List<VacunaAplicadaInfoDTO> vacunas;
}