package com.cibertec.veterinaria.service.impl;

import com.cibertec.veterinaria.dto.ConsultaInfoDTO;
import com.cibertec.veterinaria.dto.ConsultaRegisterDTO;
import com.cibertec.veterinaria.entity.*;
import com.cibertec.veterinaria.entity.enums.TipoEstadoCita;
import com.cibertec.veterinaria.entity.enums.TipoEstadoVacuna;
import com.cibertec.veterinaria.mapper.ConsultaMapper;
import com.cibertec.veterinaria.mapper.TratamientoMapper;
import com.cibertec.veterinaria.mapper.VacunaAplicadaMapper;
import com.cibertec.veterinaria.repository.*;
import com.cibertec.veterinaria.service.ConsultaService;
import com.cibertec.veterinaria.service.LogSistemaService;
import com.cibertec.veterinaria.service.VacunaAplicadaService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultaServiceImpl implements ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final CitaRepository citaRepository;
    private final TratamientoRepository tratamientoRepository;
    private final VacunaAplicadaRepository vacunaAplicadaRepository;
    private final VacunaRepository vacunaCatalogoRepository;

    private final ConsultaMapper consultaMapper;
    private final TratamientoMapper tratamientoMapper;
    private final VacunaAplicadaMapper vacunaAplicadaMapper;
    private final LogSistemaService logService;
    private final VacunaAplicadaService vacunaAplicadaService;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public ConsultaInfoDTO registrarAtencionCompleta(ConsultaRegisterDTO dto) {

        Cita cita = citaRepository.findById(dto.getIdCita())
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        if (consultaRepository.existsByCitaIdCita(dto.getIdCita())) {
            throw new RuntimeException("Ya existe una consulta registrada para esta cita");
        }

        // 1. Guardar la Consulta primero (Necesitamos su ID para las vacunas)
        Consulta consulta = consultaMapper.toEntity(dto);
        consulta.setCita(cita);
        Consulta consultaGuardada = consultaRepository.save(consulta);

        // 2. Guardar Tratamientos
        if (dto.getTratamientos() != null && !dto.getTratamientos().isEmpty()) {
            dto.getTratamientos().forEach(tDto -> {
                TratamientoMedicamento tm = tratamientoMapper.toEntity(tDto);
                tm.setConsulta(consultaGuardada);
                tratamientoRepository.save(tm);
            });
        }

        if (dto.getVacunas() != null && !dto.getVacunas().isEmpty()) {
            dto.getVacunas().forEach(vDto -> {
                vacunaAplicadaService.aplicarVacunaInmediata(
                        cita.getMascota().getIdMascota(),
                        vDto,
                        consultaGuardada.getIdConsulta()
                );
            });
        }

        // 4. Finalizar Cita
        cita.setEstado(TipoEstadoCita.COMPLETADA);
        citaRepository.save(cita);

        // 5. Log y Limpieza
        logService.registrarLog(
                cita.getVeterinario().getUsuario().getIdUsuario(),
                "CONSULTA",
                "REGISTRO",
                "Atención médica #" + consultaGuardada.getIdConsulta() + " finalizada para: " + cita.getMascota().getNombreMascota()
        );

        entityManager.flush();
        entityManager.clear();

        return obtenerPorId(consultaGuardada.getIdConsulta());
    }

    @Override
    @Transactional(readOnly = true)
    public ConsultaInfoDTO obtenerPorId(Long idConsulta) {
        Consulta consulta = consultaRepository.findById(idConsulta)
                .orElseThrow(() -> new RuntimeException("Consulta no encontrada"));

        return consultaMapper.toInfoDTO(consulta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsultaInfoDTO> listarPorMascota(Long idMascota) {
        return consultaRepository.findByCitaMascotaIdMascotaOrderByCitaFechaDescCitaHoraDesc(idMascota)
                .stream()
                .map(consultaMapper::toInfoDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsultaInfoDTO> listarPorVeterinario(String idUsuarioVet) {
        return consultaRepository.findByCitaVeterinarioUsuarioIdUsuarioOrderByCitaFechaDesc(idUsuarioVet)
                .stream()
                .map(consultaMapper::toInfoDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsultaInfoDTO> listarPorVeterinarioYFecha(String idUsuarioVet, java.time.LocalDate fecha) {
        return consultaRepository.findByCitaVeterinarioUsuarioIdUsuarioAndCitaFecha(idUsuarioVet, fecha)
                .stream()
                .map(consultaMapper::toInfoDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsultaInfoDTO> buscarPorCriterio(String criterio) {
        return consultaRepository.buscarPorCriterio(criterio)
                .stream()
                .map(consultaMapper::toInfoDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsultaInfoDTO> listarTodas() {
        return consultaRepository.findAll().stream()
                .map(consultaMapper::toInfoDTO)
                .collect(Collectors.toList());
    }
}