package com.cibertec.veterinaria.service.impl;

import com.cibertec.veterinaria.dto.VacunaInfoDTO;
import com.cibertec.veterinaria.dto.VacunaRegisterDTO;
import com.cibertec.veterinaria.dto.VacunaUpdateDTO;
import com.cibertec.veterinaria.entity.Vacuna;
import com.cibertec.veterinaria.mapper.VacunaMapper;
import com.cibertec.veterinaria.repository.VacunaRepository;
import com.cibertec.veterinaria.service.VacunaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class VacunaServiceImpl implements VacunaService{

    private final VacunaRepository vacunaRepository;
    private final VacunaMapper vacunaMapper;

    @Override
    public List<VacunaInfoDTO> listarVacunas() {
        return vacunaRepository.findByActivoTrue()
                .stream()
                .map(vacunaMapper::toVacunaInfoDTO)
                .toList();
    }

    @Override
    public List<VacunaInfoDTO> listarTodas() {
        return vacunaRepository.findAll()
                .stream()
                .map(vacunaMapper::toVacunaInfoDTO)
                .toList();
    }

    @Override
    public VacunaInfoDTO obtenerVacunaPorId(Long idVacuna) {
        Vacuna vacuna = vacunaRepository.findById(idVacuna)
                .orElseThrow(() -> new RuntimeException("Vacuna no encontrada"));
        return vacunaMapper.toVacunaInfoDTO(vacuna);
    }

    @Override
    public VacunaInfoDTO guardarVacuna(VacunaRegisterDTO vacunaRegisterDTO) {
        Vacuna nuevaVacuna = vacunaMapper.toEntity(vacunaRegisterDTO);
        nuevaVacuna.setActivo(true);
        return vacunaMapper.toVacunaInfoDTO(vacunaRepository.save(nuevaVacuna));
    }

    @Override
    public VacunaInfoDTO actualizarVacuna(Long idVacuna, VacunaUpdateDTO vacunaUpdateDTO) {
        Vacuna vacuna = vacunaRepository.findById(idVacuna)
                .orElseThrow(() -> new RuntimeException("Vacuna no encontrada con ID: " + idVacuna));

        vacunaMapper.updateEntityFromDTO(vacunaUpdateDTO, vacuna);

        return vacunaMapper.toVacunaInfoDTO(vacunaRepository.save(vacuna));
    }

    @Override
    public void desactivarVacuna(Long idVacuna) {
        actualizarEstado(idVacuna, false);
    }

    @Override
    public void activarVacuna(Long idVacuna) {
        actualizarEstado(idVacuna, true);
    }

    public void actualizarEstado(Long idVacuna, boolean estado) {
        Vacuna v = vacunaRepository.findById(idVacuna).orElseThrow();
        v.setActivo(estado);
        vacunaRepository.save(v);
    }
}
