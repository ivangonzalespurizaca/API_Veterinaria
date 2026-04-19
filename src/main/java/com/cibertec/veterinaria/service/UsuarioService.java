package com.cibertec.veterinaria.service;

import com.cibertec.veterinaria.dto.UsuarioInfoDTO;
import com.cibertec.veterinaria.dto.UsuarioUpdateDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UsuarioService {

    String actualizarFotoPerfil(String idUsuario, MultipartFile archivo) throws IOException;
    UsuarioInfoDTO actualizarUsuario(String id, UsuarioUpdateDTO usuarioUpdateDTO);

}
