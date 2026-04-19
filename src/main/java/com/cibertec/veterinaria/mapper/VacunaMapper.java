package com.cibertec.veterinaria.mapper;

import com.cibertec.veterinaria.dto.VacunaInfoDTO;
import com.cibertec.veterinaria.dto.VacunaRegisterDTO;
import com.cibertec.veterinaria.dto.VacunaUpdateDTO;
import com.cibertec.veterinaria.entity.Vacuna;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VacunaMapper {

    VacunaInfoDTO toVacunaInfoDTO(Vacuna entity);

    Vacuna toEntity(VacunaRegisterDTO registerDTO);

    void updateEntityFromDTO(VacunaUpdateDTO vacunaUpdateDTO, @MappingTarget Vacuna vacuna);
}
