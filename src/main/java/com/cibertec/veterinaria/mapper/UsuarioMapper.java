package com.cibertec.veterinaria.mapper;

import com.cibertec.veterinaria.dto.UsuarioInfoDTO;
import com.cibertec.veterinaria.dto.UsuarioRegisterDTO;
import com.cibertec.veterinaria.dto.UsuarioUpdateDTO;
import com.cibertec.veterinaria.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioInfoDTO toUsuarioInfoDTO(Usuario entity);

    Usuario toEntity(UsuarioRegisterDTO registerDTO);

    void updateEntityFromRegisterDTO(UsuarioRegisterDTO registerDTO, @MappingTarget Usuario usuario);

    void updateEntityFromDTO(UsuarioUpdateDTO updateDTO, @MappingTarget Usuario usuario);
}
