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

        Consulta consulta = consultaMapper.toEntity(dto);
        consulta.setCita(cita);
        Consulta consultaGuardada = consultaRepository.save(consulta);

        if (dto.getTratamientos() != null && !dto.getTratamientos().isEmpty()) {
            dto.getTratamientos().forEach(tDto -> {
                TratamientoMedicamento tm = tratamientoMapper.toEntity(tDto);
                tm.setConsulta(consultaGuardada);
                tratamientoRepository.save(tm);
            });
        }

        if (dto.getVacunas() != null && !dto.getVacunas().isEmpty()) {
            dto.getVacunas().forEach(vDto -> {
                Vacuna vCatalogo = vacunaCatalogoRepository.findById(vDto.getIdVacuna())
                        .orElseThrow(() -> new RuntimeException("Vacuna no encontrada en el catálogo"));

                VacunaAplicada aplicada = VacunaAplicada.builder()
                        .mascota(cita.getMascota())
                        .vacuna(vCatalogo)
                        .cita(cita)
                        .estado(TipoEstadoVacuna.APLICADA)
                        .fechaAplicacion(java.time.LocalDate.now())
                        .proximaDosis(vDto.getProximaDosis())
                        .nroDosis(vDto.getNroDosis())
                        .observaciones(vDto.getObservaciones())
                        .build();
                vacunaAplicadaRepository.save(aplicada);

                if (vDto.isGenerarRecordatorio() && vDto.getProximaDosis() != null) {
                    VacunaAplicada recordatorio = VacunaAplicada.builder()
                            .mascota(cita.getMascota())
                            .vacuna(vCatalogo)
                            .cita(cita)
                            .estado(TipoEstadoVacuna.PROGRAMADA)
                            .proximaDosis(vDto.getProximaDosis())
                            .nroDosis(vDto.getNroDosis() + 1)
                            .observaciones("Refuerzo automático programado")
                            .build();
                    vacunaAplicadaRepository.save(recordatorio);
                }
            });
        }
        cita.setEstado(TipoEstadoCita.COMPLETADA);
        citaRepository.save(cita);

        String idVeterinario = cita.getVeterinario().getUsuario().getIdUsuario();
        String nombreMascota = cita.getMascota().getNombreMascota();

        logService.registrarLog(
                idVeterinario,
                "CONSULTA",
                "REGISTRO",
                "Atención médica finalizada. Mascota: " + nombreMascota + ". Diagnóstico: " + consultaGuardada.getDiagnostico()
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