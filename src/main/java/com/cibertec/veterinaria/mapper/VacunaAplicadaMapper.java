package com.cibertec.veterinaria.mapper;

import com.cibertec.veterinaria.dto.VacunaAplicadaDTO;
import com.cibertec.veterinaria.dto.VacunaAplicadaInfoDTO;
import com.cibertec.veterinaria.dto.VacunaAplicadaProgramadaDTO;
import com.cibertec.veterinaria.entity.VacunaAplicada;
import com.cibertec.veterinaria.entity.enums.TipoEstadoVacuna;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VacunaAplicadaMapper {

    @Mapping(target = "nombreVacuna", source = "vacuna.nombreVacuna")
    @Mapping(target = "descripcionVacuna", source = "vacuna.descripcion")
    @Mapping(target = "nombreVeterinario", expression = "java(obtenerNombreCompletoVeterinario(entity))")
    VacunaAplicadaInfoDTO toInfoDTO(VacunaAplicada entity);

    @Mapping(target = "idAplicacion", ignore = true)
    @Mapping(target = "mascota", ignore = true)
    @Mapping(target = "consulta", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "fechaAplicacion", ignore = true)
    @Mapping(target = "fechaProgramada", ignore = true)
    VacunaAplicada toEntity(VacunaAplicadaDTO dto);

    @Mapping(target = "idAplicacion", ignore = true)
    @Mapping(target = "mascota", ignore = true)
    @Mapping(target = "consulta", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "fechaAplicacion", ignore = true)
    VacunaAplicada toEntity(VacunaAplicadaProgramadaDTO dto);

    default String obtenerNombreCompletoVeterinario(VacunaAplicada entity) {

        if (entity.getConsulta() == null ||
                entity.getConsulta().getCita() == null ||
                entity.getConsulta().getCita().getVeterinario() == null) {
            return "Sin asignar";
        }

        var usuario = entity.getConsulta().getCita().getVeterinario().getUsuario();
        String nombreFull = usuario.getNombres() + " " + usuario.getApellidos();

        if (entity.getEstado() == TipoEstadoVacuna.PROGRAMADA) {
            return "Programado por: " + nombreFull;
        }

        return nombreFull;
    }
}