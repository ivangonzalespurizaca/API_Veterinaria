package com.cibertec.veterinaria.mapper;

import com.cibertec.veterinaria.dto.CitaInfoDTO;
import com.cibertec.veterinaria.dto.CitaRegisterDTO;
import com.cibertec.veterinaria.dto.CitaStatusUpdateDTO;
import com.cibertec.veterinaria.entity.Cita;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CitaMapper {

    @Mapping(source = "mascota.nombreMascota", target = "nombreMascota")
    @Mapping(source = "mascota.fotoUrl", target = "fotoMascotaUrl")
    @Mapping(source = "mascota.cliente.nombres", target = "nombreCliente")
    @Mapping(source = "mascota.cliente.celular", target = "celularCliente")
    @Mapping(source = "veterinario.usuario.nombres", target = "nombreVeterinario")
    @Mapping(source = "veterinario.especialidad", target = "especialidadVeterinario")
    CitaInfoDTO toInfoDTO(Cita cita);

    @Mapping(source = "idMascota", target = "mascota.idMascota")
    @Mapping(source = "idUsuarioVeterinario", target = "veterinario.usuario.idUsuario")
    Cita toEntity(CitaRegisterDTO registerDTO);

    @Mapping(source = "nuevoEstado", target = "estado")
    void updateStatusFromDTO(CitaStatusUpdateDTO updateDTO, @MappingTarget Cita cita);
}