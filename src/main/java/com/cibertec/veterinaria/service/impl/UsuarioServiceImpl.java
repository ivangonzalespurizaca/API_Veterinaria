package com.cibertec.veterinaria.service.impl;

import com.cibertec.veterinaria.dto.UsuarioInfoDTO;
import com.cibertec.veterinaria.dto.UsuarioUpdateDTO;
import com.cibertec.veterinaria.entity.Usuario;
import com.cibertec.veterinaria.mapper.UsuarioMapper;
import com.cibertec.veterinaria.repository.UsuarioRepository;
import com.cibertec.veterinaria.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CloudinaryServiceImpl cloudinaryService;
    private final UsuarioMapper usuarioMapper;

    @Override
    public String actualizarFotoPerfil(String idUsuario, MultipartFile archivo) throws java.io.IOException {
        validarPropiedadDelPerfil(idUsuario);

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Map result = cloudinaryService.upload(archivo);
        String url = (String) result.get("url");

        usuario.setFotoUrl(url);
        usuarioRepository.save(usuario);

        return url;
    }

    @Override
    public UsuarioInfoDTO actualizarUsuario(String id, UsuarioUpdateDTO dto) {


        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuarioMapper.updateEntityFromDTO(dto, usuario);

        return usuarioMapper.toUsuarioInfoDTO(usuarioRepository.save(usuario));
    }

    private void validarPropiedadDelPerfil(String idSolicitado) {
        String uidAutenticado = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!idSolicitado.equals(uidAutenticado)) {
            throw new RuntimeException("Acceso denegado: No puedes modificar un perfil ajeno.");
        }
    }
}
