package com.cibertec.veterinaria.mapper;

import com.cibertec.veterinaria.dto.VacunaAplicadaDTO;
import com.cibertec.veterinaria.dto.VacunaAplicadaInfoDTO;
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
    @Mapping(target = "cita", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "fechaAplicacion", ignore = true)
    VacunaAplicada toEntity(VacunaAplicadaDTO dto);

    default String obtenerNombreCompletoVeterinario(VacunaAplicada entity) {
        if (entity.getCita() == null || entity.getCita().getVeterinario() == null) {
            // Si no tiene cita, es una vacuna huérfana (no debería pasar con el fix de arriba)
            return "Sin asignar";
        }

        var usuario = entity.getCita().getVeterinario().getUsuario();
        String nombreFull = usuario.getNombres() + " " + usuario.getApellidos();

        // Opcional: Indicar si es quien la puso o quien la programó
        if (entity.getEstado() == TipoEstadoVacuna.PROGRAMADA) {
            return "Programado por: " + nombreFull;
        }

        return nombreFull;
    }
}