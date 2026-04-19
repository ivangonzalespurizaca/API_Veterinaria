package com.cibertec.veterinaria.service.impl;

import com.cibertec.veterinaria.dto.HorarioInfoDTO;
import com.cibertec.veterinaria.dto.HorarioRegisterDTO;
import com.cibertec.veterinaria.dto.HorarioUpdateDTO;
import com.cibertec.veterinaria.entity.HorarioAtencion;
import com.cibertec.veterinaria.entity.Veterinario;
import com.cibertec.veterinaria.mapper.HorarioMapper;
import com.cibertec.veterinaria.repository.HorarioAtencionRepository;
import com.cibertec.veterinaria.repository.VeterinarioRepository;
import com.cibertec.veterinaria.service.HorarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class HorarioServiceImpl implements HorarioService {

    private final HorarioAtencionRepository horarioRepository;
    private final VeterinarioRepository veterinarioRepository;
    private final HorarioMapper horarioMapper;

    @Transactional
    @Override
    public HorarioInfoDTO registrarHorario(HorarioRegisterDTO dto) {
        // 1. Validar horas
        if (dto.getHoraInicio().isAfter(dto.getHoraFin())) {
            throw new RuntimeException("La hora de inicio no puede ser después de la hora de fin");
        }

        // 2. Buscar si ya existe el registro (activo o inactivo)
        Optional<HorarioAtencion> horarioOpt = horarioRepository.findByVeterinarioUsuarioIdUsuarioAndDiaSemana(dto.getIdUsuarioVeterinario(), dto.getDiaSemana());

        if (horarioOpt.isPresent()) {
            HorarioAtencion horarioExistente = horarioOpt.get();

            // Si ya está activo, lanzamos error
            if (horarioExistente.getActivo()) {
                throw new RuntimeException("El veterinario ya tiene un horario ACTIVO para el " + dto.getDiaSemana());
            }

            // Si estaba inactivo, lo actualizamos y reactivamos
            horarioExistente.setHoraInicio(dto.getHoraInicio());
            horarioExistente.setHoraFin(dto.getHoraFin());
            horarioExistente.setActivo(true);

            return horarioMapper.toInfoDTO(horarioRepository.save(horarioExistente));
        }

        // 3. SI NO EXISTE: Procedemos con la creación normal
        Veterinario veterinario = veterinarioRepository.findByUsuarioIdUsuario(dto.getIdUsuarioVeterinario())
                .orElseThrow(() -> new RuntimeException("Veterinario no encontrado"));

        HorarioAtencion nuevoHorario = horarioMapper.toEntity(dto);
        nuevoHorario.setVeterinario(veterinario);
        nuevoHorario.setActivo(true);

        return horarioMapper.toInfoDTO(horarioRepository.save(nuevoHorario));
    }

    @Override
    public List<HorarioInfoDTO> listarDisponiblesPorVeterinario(String idUsuario) {
        return horarioRepository.findByVeterinarioUsuarioIdUsuarioAndActivoTrue(idUsuario)
                .stream()
                .map(horarioMapper::toInfoDTO)
                .toList();
    }

    @Override
    public List<HorarioInfoDTO> listarTodoPorVeterinario(String idUsuario) {
        return horarioRepository.findByVeterinarioUsuarioIdUsuario(idUsuario)
                .stream()
                .map(horarioMapper::toInfoDTO)
                .toList();
    }

    @Override
    @Transactional
    public HorarioInfoDTO actualizarHorario(Long id, HorarioUpdateDTO dto) {
        HorarioAtencion horario = horarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));

        if (dto.getHoraInicio() != null && dto.getHoraFin() != null) {
            if (dto.getHoraInicio().isAfter(dto.getHoraFin())) {
                throw new RuntimeException("La hora de inicio no puede ser posterior a la de fin");
            }
        }

        if (dto.getActivo() != null) horario.setActivo(dto.getActivo());

        horarioMapper.updateEntityFromDTO(dto, horario);
        return horarioMapper.toInfoDTO(horarioRepository.save(horario));
    }

    @Override
    @Transactional
    public HorarioInfoDTO desactivarHorario(Long id) {
        HorarioAtencion horario = horarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));
        horario.setActivo(false);
        return horarioMapper.toInfoDTO(horarioRepository.save(horario));
    }

    @Override
    @Transactional
    public HorarioInfoDTO activarHorario(Long id) {
        HorarioAtencion horario = horarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));
        horario.setActivo(true);
        return horarioMapper.toInfoDTO(horarioRepository.save(horario));
    }
}
