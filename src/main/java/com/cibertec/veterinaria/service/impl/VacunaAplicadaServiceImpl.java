package com.cibertec.veterinaria.service.impl;

import com.cibertec.veterinaria.dto.VacunaAplicadaDTO;
import com.cibertec.veterinaria.dto.VacunaAplicadaInfoDTO;
import com.cibertec.veterinaria.dto.VacunaAplicadaProgramadaDTO;
import com.cibertec.veterinaria.dto.VacunaConfirmadaDTO;
import com.cibertec.veterinaria.entity.Consulta;
import com.cibertec.veterinaria.entity.Mascota;
import com.cibertec.veterinaria.entity.Vacuna;
import com.cibertec.veterinaria.entity.VacunaAplicada;
import com.cibertec.veterinaria.entity.enums.TipoEstadoVacuna;
import com.cibertec.veterinaria.mapper.VacunaAplicadaMapper;
import com.cibertec.veterinaria.repository.ConsultaRepository;
import com.cibertec.veterinaria.repository.MascotaRepository;
import com.cibertec.veterinaria.repository.VacunaAplicadaRepository;
import com.cibertec.veterinaria.repository.VacunaRepository;
import com.cibertec.veterinaria.service.LogSistemaService;
import com.cibertec.veterinaria.service.VacunaAplicadaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VacunaAplicadaServiceImpl implements VacunaAplicadaService {

    private final VacunaAplicadaRepository vacunaAplicadaRepository;
    private final MascotaRepository mascotaRepository;
    private final VacunaRepository vacunaRepository;
    private final ConsultaRepository consultaRepository;
    private final VacunaAplicadaMapper vacunaAplicadaMapper;
    private final LogSistemaService logService;

    @Override
    @Transactional(readOnly = true)
    public List<VacunaAplicadaInfoDTO> listarCarnetPorMascota(Long idMascota) {
        return vacunaAplicadaRepository.findByMascotaIdMascotaOrderByFechaProgramadaAsc(idMascota)
                .stream()
                .map(vacunaAplicadaMapper::toInfoDTO)
                .collect(Collectors.toList());
    }

    // ESCENARIO A: Solo programar para el futuro (id_consulta queda NULL)
    @Override
    @Transactional
    public VacunaAplicadaInfoDTO programarDosisFutura(Long idMascota, VacunaAplicadaProgramadaDTO dto) {
        Mascota mascota = mascotaRepository.findById(idMascota)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        Vacuna vacunaBase = vacunaRepository.findById(dto.getIdVacuna())
                .orElseThrow(() -> new RuntimeException("Vacuna no encontrada"));

        VacunaAplicada nuevaProg = vacunaAplicadaMapper.toEntity(dto);
        nuevaProg.setMascota(mascota);
        nuevaProg.setVacuna(vacunaBase);
        nuevaProg.setEstado(TipoEstadoVacuna.PROGRAMADA);
        nuevaProg.setConsulta(null);

        return vacunaAplicadaMapper.toInfoDTO(vacunaAplicadaRepository.save(nuevaProg));
    }

    @Override
    @Transactional
    public VacunaAplicadaInfoDTO aplicarVacunaProgramada(VacunaConfirmadaDTO dto, Long idConsulta) {
        VacunaAplicada vacuna = vacunaAplicadaRepository.findById(dto.getIdAplicacion())
                .orElseThrow(() -> new RuntimeException("Registro de vacuna programada no encontrado"));

        Consulta consulta = consultaRepository.findById(idConsulta)
                .orElseThrow(() -> new RuntimeException("Consulta no encontrada para vincular la aplicación"));

        if (vacuna.getEstado() == TipoEstadoVacuna.APLICADA) {
            throw new RuntimeException("Esta vacuna ya fue aplicada anteriormente");
        }

        vacuna.setEstado(TipoEstadoVacuna.APLICADA);
        vacuna.setFechaAplicacion(dto.getFechaAplicacion());
        vacuna.setConsulta(consulta);
        if (dto.getObservaciones() != null) {
            vacuna.setObservaciones(dto.getObservaciones());
        }

        logService.registrarLog(
                consulta.getCita().getVeterinario().getUsuario().getIdUsuario(),
                "VACUNA_APLICADA",
                "CONFIRMACION",
                "Se aplicó la vacuna " + vacuna.getVacuna().getNombreVacuna() + " a la mascota " + vacuna.getMascota().getNombreMascota()
        );

        return vacunaAplicadaMapper.toInfoDTO(vacunaAplicadaRepository.save(vacuna));
    }

    @Override
    @Transactional
    public VacunaAplicadaInfoDTO aplicarVacunaInmediata(Long idMascota, VacunaAplicadaDTO dto, Long idConsulta) {
        Mascota mascota = mascotaRepository.findById(idMascota)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        Vacuna vacunaBase = vacunaRepository.findById(dto.getIdVacuna())
                .orElseThrow(() -> new RuntimeException("Vacuna no encontrada"));

        Consulta consulta = consultaRepository.findById(idConsulta)
                .orElseThrow(() -> new RuntimeException("Consulta no encontrada"));

        VacunaAplicada nuevaAplicacion = vacunaAplicadaMapper.toEntity(dto);
        nuevaAplicacion.setMascota(mascota);
        nuevaAplicacion.setVacuna(vacunaBase);
        nuevaAplicacion.setConsulta(consulta);
        nuevaAplicacion.setEstado(TipoEstadoVacuna.APLICADA);
        nuevaAplicacion.setFechaAplicacion(LocalDate.now());
        nuevaAplicacion.setFechaProgramada(null);

        logService.registrarLog(
                consulta.getCita().getVeterinario().getUsuario().getIdUsuario(),
                "VACUNA_APLICADA",
                "CONFIRMACION",
                "Se aplicó la vacuna " + vacunaBase.getNombreVacuna() + " a la mascota " + mascota.getNombreMascota()
        );

        return vacunaAplicadaMapper.toInfoDTO(vacunaAplicadaRepository.save(nuevaAplicacion));
    }
}