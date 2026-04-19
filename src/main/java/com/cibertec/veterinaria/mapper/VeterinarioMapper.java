package com.cibertec.veterinaria.mapper;

import com.cibertec.veterinaria.dto.VeterinarioInfoDTO;
import com.cibertec.veterinaria.dto.VeterinarioRegisterDTO;
import com.cibertec.veterinaria.entity.Veterinario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VeterinarioMapper {

    @Mapping(source = "idVeterinario", target = "idVeterinario")
    @Mapping(source = "usuario.idUsuario", target = "idUsuario")
    @Mapping(source = "usuario.nombres", target = "nombres")
    @Mapping(source = "usuario.apellidos", target = "apellidos")
    @Mapping(source = "usuario.email", target = "email")
    @Mapping(source = "usuario.fotoUrl", target = "fotoUrl")
    @Mapping(source = "usuario.activo", target = "activo")
    @Mapping(source = "usuario.dni", target = "dni")
    @Mapping(source = "numColegiatura", target = "numColegiatura")
    @Mapping(source = "especialidad", target = "especialidad")
    VeterinarioInfoDTO toVeterinarioInfoDTO(Veterinario entity);

    @Mapping(source = "idUsuario", target = "usuario.idUsuario")
    @Mapping(source = "nombres", target = "usuario.nombres")
    @Mapping(source = "apellidos", target = "usuario.apellidos")
    @Mapping(source = "email", target = "usuario.email")
    @Mapping(source = "dni", target = "usuario.dni")
    @Mapping(source = "celular", target = "usuario.celular")
    @Mapping(source = "genero", target = "usuario.genero")
    Veterinario toEntity(VeterinarioRegisterDTO registerDTO);

    @Mapping(source = "activo", target = "usuario.activo")
    void updateEntityFromDTO(VeterinarioInfoDTO infoDTO, @MappingTarget Veterinario veterinario);
}
