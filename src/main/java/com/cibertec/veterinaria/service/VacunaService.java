package com.cibertec.veterinaria.service;

import com.cibertec.veterinaria.dto.VacunaInfoDTO;
import com.cibertec.veterinaria.dto.VacunaRegisterDTO;
import com.cibertec.veterinaria.dto.VacunaUpdateDTO;

import java.util.List;

public interface VacunaService {
    List<VacunaInfoDTO> listarVacunas();
    List<VacunaInfoDTO> listarTodas();
    VacunaInfoDTO obtenerVacunaPorId(Long idVacuna);
    VacunaInfoDTO guardarVacuna(VacunaRegisterDTO vacunaRegisterDTO);
    VacunaInfoDTO actualizarVacuna(Long idVacuna, VacunaUpdateDTO vacunaUpdateDTO);
    void desactivarVacuna(Long idVacuna);
    void activarVacuna(Long idVacuna);
}
