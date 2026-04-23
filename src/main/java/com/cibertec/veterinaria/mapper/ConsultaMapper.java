package com.cibertec.veterinaria.mapper;

import com.cibertec.veterinaria.dto.ConsultaInfoDTO;
import com.cibertec.veterinaria.dto.ConsultaRegisterDTO;
import com.cibertec.veterinaria.entity.Consulta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TratamientoMapper.class, VacunaAplicadaMapper.class})
public interface ConsultaMapper {

    @Mapping(source = "cita.idCita", target = "idCita")
    @Mapping(source = "cita.mascota.nombreMascota", target = "nombreMascota")
    @Mapping(source = "cita.fecha", target = "fechaConsulta")
    @Mapping(target = "nombreVeterinario", expression = "java(consulta.getCita().getVeterinario().getUsuario().getNombres() + \" \" + consulta.getCita().getVeterinario().getUsuario().getApellidos())")
    @Mapping(source = "tratamientos", target = "tratamientos")
    @Mapping(source = "vacunasAplicadas", target = "vacunas") // Aquí le decimos que las busque en la Cita
    ConsultaInfoDTO toInfoDTO(Consulta consulta);

    @Mapping(source = "idCita", target = "cita.idCita")
    @Mapping(target = "tratamientos", ignore = true)
        // CAMBIO AQUÍ: Eliminamos el mapeo de vacunas porque la ENTIDAD Consulta no las tiene
    Consulta toEntity(ConsultaRegisterDTO registerDTO);
}