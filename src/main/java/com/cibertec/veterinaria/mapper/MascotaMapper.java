package com.cibertec.veterinaria.mapper;

import com.cibertec.veterinaria.dto.MascotaInfoDTO;
import com.cibertec.veterinaria.dto.MascotaRegisterDTO;
import com.cibertec.veterinaria.dto.MascotaUpdateDTO;
import com.cibertec.veterinaria.entity.Mascota;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.time.Period;

@Mapper(componentModel = "spring")
public interface MascotaMapper {

    @Mapping(source = "cliente.nombres", target = "nombreDuenio", qualifiedByName = "formatearNombreDueño")
    @Mapping(source = "fechaNacimiento", target = "edad", qualifiedByName = "calcularEdad")
    @Mapping(source = "cliente.dni", target = "dniDuenio")
    MascotaInfoDTO toInfoDTO(Mascota mascota);

    @Mapping(source = "idCliente", target = "cliente.idUsuario")
    Mascota toEntity(MascotaRegisterDTO registerDTO);

    void updateEntityFromDTO(MascotaUpdateDTO updateDTO, @MappingTarget Mascota mascota);

    @Named("calcularEdad")
    default Integer calcularEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) return 0;
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    @Named("formatearNombreDueño")
    default String formatearNombreDueño(String nombres) {
        return nombres != null ? nombres : "Sin dueño";
    }
}