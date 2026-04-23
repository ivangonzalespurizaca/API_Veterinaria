package com.cibertec.veterinaria.mapper;

import com.cibertec.veterinaria.dto.HorarioInfoDTO;
import com.cibertec.veterinaria.dto.HorarioRegisterDTO;
import com.cibertec.veterinaria.dto.HorarioUpdateDTO;
import com.cibertec.veterinaria.entity.HorarioAtencion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface HorarioMapper {

    @Mapping(source = "veterinario.usuario.nombres", target = "nombreVeterinario")
    @Mapping(source = "diaSemana", target = "diaSemana")
    HorarioInfoDTO toInfoDTO(HorarioAtencion horario);

    @Mapping(source = "idVeterinario", target = "veterinario.idVeterinario")
    HorarioAtencion toEntity(HorarioRegisterDTO registerDTO);

    void updateEntityFromDTO(HorarioUpdateDTO updateDTO, @MappingTarget HorarioAtencion horario);
}