package com.cibertec.veterinaria.service;

import com.cibertec.veterinaria.dto.VeterinarioInfoDTO;
import com.cibertec.veterinaria.dto.VeterinarioRegisterDTO;
import com.cibertec.veterinaria.dto.VeterinarioUpdateDTO;

import java.util.List;

public interface VeterinarioService {
    List<VeterinarioInfoDTO> listarTodos();
    List<VeterinarioInfoDTO> buscarPorFiltro(String query);
    VeterinarioInfoDTO registrar(VeterinarioRegisterDTO dto);
    VeterinarioInfoDTO actualizarDatosProfesionales(Long id, VeterinarioUpdateDTO dto);
    void cambiarEstado(Long id, boolean activo);
}
