package com.cibertec.veterinaria.mapper;

import com.cibertec.veterinaria.dto.TratamientoDTO;
import com.cibertec.veterinaria.dto.TratamientoInfoDTO;
import com.cibertec.veterinaria.entity.TratamientoMedicamento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TratamientoMapper {

    TratamientoInfoDTO toInfoDTO(TratamientoMedicamento tratamiento);

    @Mapping(target = "idTratamiento", ignore = true)
    @Mapping(target = "consulta", ignore = true)
    TratamientoMedicamento toEntity(TratamientoDTO tratamientoDTO);

    List<TratamientoInfoDTO> toInfoDTOList(List<TratamientoMedicamento> tratamientos);

    List<TratamientoMedicamento> toEntityList(List<TratamientoDTO> tratamientosDTO);
}