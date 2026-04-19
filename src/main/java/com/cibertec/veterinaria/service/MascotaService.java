package com.cibertec.veterinaria.service;

import com.cibertec.veterinaria.dto.MascotaInfoDTO;
import com.cibertec.veterinaria.dto.MascotaRegisterDTO;
import com.cibertec.veterinaria.dto.MascotaUpdateDTO;
import com.cibertec.veterinaria.entity.enums.TipoEspecie;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MascotaService {

    MascotaInfoDTO guardarMascota(MascotaRegisterDTO dto);
    MascotaInfoDTO editarMascota(Long id, MascotaUpdateDTO dto);
    MascotaInfoDTO obtenerPorId(Long id);
    List<MascotaInfoDTO> listarPorCliente(String idUsuario);
    List<MascotaInfoDTO> listarPorEspecie(TipoEspecie especie);
    List<MascotaInfoDTO> buscarPorDniDuenio(String dni);
    void eliminarLogica(Long id);
    String actualizarFotoMascota(Long id, MultipartFile archivo) throws IOException;

}
