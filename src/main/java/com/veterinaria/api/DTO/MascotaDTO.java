package com.veterinaria.api.DTO;


import com.veterinaria.api.models.enums.TipoSexo;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MascotaDTO {
    private String nombre_mascota;
    private String idRaza;
    private String idCliente;
    private TipoSexo sexo;
    private LocalDate fecha_nacimiento;
    private Double peso_inicial;
    private String foto;
    private String codigo_qr;
}