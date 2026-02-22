package com.veterinaria.api.DTO;

import com.veterinaria.api.models.enums.TipoSexo;
import lombok.Data;

@Data
public class MascotaAdopcionDTO {

    private String idRaza;
    private String idEspecie;
    private String nombre_mascota;
    private TipoSexo sexo;
    private String edad_estimada;
    private String foto;
}