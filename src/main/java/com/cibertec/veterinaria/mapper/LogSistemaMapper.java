package com.cibertec.veterinaria.mapper;

import com.cibertec.veterinaria.dto.LogSistemaInfoDTO;
import com.cibertec.veterinaria.entity.LogSistema;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface LogSistemaMapper {

    @Mapping(target = "nombreUsuario", source = "usuario", qualifiedByName = "mapearNombreCompleto")
    @Mapping(target = "rolUsuario", source = "usuario.rol", qualifiedByName = "mapearRolUsuario")
    LogSistemaInfoDTO toInfoDTO(LogSistema log);

    @Named("mapearNombreCompleto")
    default String mapearNombreCompleto(com.cibertec.veterinaria.entity.Usuario usuario) {
        if (usuario == null) return "SISTEMA";
        return usuario.getNombres() + " " + usuario.getApellidos();
    }

    @Named("mapearRolUsuario")
    default String mapearRolUsuario(com.cibertec.veterinaria.entity.enums.TipoRol rol) {
        return rol != null ? rol.name() : "SISTEMA";
    }
}