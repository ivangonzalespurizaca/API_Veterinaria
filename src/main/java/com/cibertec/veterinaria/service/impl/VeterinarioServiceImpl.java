package com.cibertec.veterinaria.service.impl;

import com.cibertec.veterinaria.dto.VeterinarioInfoDTO;
import com.cibertec.veterinaria.dto.VeterinarioRegisterDTO;
import com.cibertec.veterinaria.dto.VeterinarioUpdateDTO;
import com.cibertec.veterinaria.entity.Usuario;
import com.cibertec.veterinaria.entity.Veterinario;
import com.cibertec.veterinaria.entity.enums.TipoRol;
import com.cibertec.veterinaria.mapper.VeterinarioMapper;
import com.cibertec.veterinaria.repository.UsuarioRepository;
import com.cibertec.veterinaria.repository.VeterinarioRepository;
import com.cibertec.veterinaria.security.AuthService;
import com.cibertec.veterinaria.service.LogSistemaService;
import com.cibertec.veterinaria.service.VeterinarioService;
import com.google.firebase.auth.FirebaseAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VeterinarioServiceImpl implements VeterinarioService {

    private final UsuarioRepository usuarioRepository;
    private final VeterinarioRepository veterinarioRepository;
    private final VeterinarioMapper veterinarioMapper;
    private final LogSistemaService logService;

    @Override
    public List<VeterinarioInfoDTO> listarTodos() {
        return veterinarioRepository.findAll().stream()
                .map(veterinarioMapper::toVeterinarioInfoDTO)
                .toList();
    }

    @Override
    public List<VeterinarioInfoDTO> buscarPorFiltro(String query) {
        return veterinarioRepository.buscarPorNombreODni(query).stream()
                .map(veterinarioMapper::toVeterinarioInfoDTO)
                .toList();
    }

    @Override
    @Transactional
    public VeterinarioInfoDTO registrar(VeterinarioRegisterDTO dto) {

        Usuario usuario = Usuario.builder()
                .idUsuario(dto.getIdUsuario())
                .nombres(dto.getNombres())
                .apellidos(dto.getApellidos())
                .email(dto.getEmail())
                .dni(dto.getDni())
                .celular(dto.getCelular())
                .genero(dto.getGenero())
                .fotoUrl("https://res.cloudinary.com/dfid8iuf3/image/upload/v1775790000/1077114_vegvlp.png")
                .rol(TipoRol.VETERINARIO) // Rol en DB Local
                .activo(true)
                .build();

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        // 2. PASO CRUCIAL: Sincronizar el ROL con Firebase
        //actualizarRolEnFirebase(dto.getIdUsuario(), "VETERINARIO");

        Veterinario veterinario = new Veterinario();
        veterinario.setUsuario(usuarioGuardado);
        veterinario.setNumColegiatura(dto.getNumColegiatura());
        veterinario.setEspecialidad(dto.getEspecialidad());

        Veterinario veterinarioGuardado = veterinarioRepository.save(veterinario);

        // 3. LOG de Auditoría
        logService.registrarLog(
                null, // O el ID del admin que está registrando
                "VETERINARIO",
                "REGISTRO",
                "Nuevo veterinario registrado: " + dto.getNombres() + " " + dto.getApellidos() + " - CNP: " + dto.getNumColegiatura()
        );

        return veterinarioMapper.toVeterinarioInfoDTO(veterinarioGuardado);
    }

    @Override
    @Transactional
    public VeterinarioInfoDTO actualizarDatosProfesionales(Long id, VeterinarioUpdateDTO dto) {
        Veterinario veterinario = veterinarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veterinario no encontrado"));

        veterinario.setNumColegiatura(dto.getNumColegiatura());
        veterinario.setEspecialidad(dto.getEspecialidad());

        if (dto.getActivo() != null) {
            veterinario.getUsuario().setActivo(dto.getActivo());
        }

        return veterinarioMapper.toVeterinarioInfoDTO(veterinarioRepository.save(veterinario));
    }

    @Override
    @Transactional
    public void cambiarEstado(Long id, boolean activo) {
        Veterinario veterinario = veterinarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veterinario no encontrado"));

        veterinario.getUsuario().setActivo(activo);
        usuarioRepository.save(veterinario.getUsuario());

        // LOG de cambio de estado
        logService.registrarLog(
                null,
                "VETERINARIO",
                activo ? "ACTIVACION" : "DESACTIVACION",
                "Se cambió el estado del veterinario ID: " + id + " a " + (activo ? "Activo" : "Inactivo")
        );
    }

    @Override
    public List<VeterinarioInfoDTO> listarDisponibles() {
        return veterinarioRepository.findByUsuarioActivoTrue().stream()
                .map(veterinarioMapper::toVeterinarioInfoDTO)
                .toList();
    }

    private void actualizarRolEnFirebase(String uid, String rol) {
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("rol", rol);
            FirebaseAuth.getInstance().setCustomUserClaims(uid, claims);
            System.out.println("✅ Rol sincronizado en Firebase para: " + uid);
        } catch (Exception e) {
            System.err.println("⚠️ Aviso: No se sincronizó el rol en la nube (IAM Error), " +
                    "pero el veterinario se guardará en BD local: " + e.getMessage());
        }
    }
}
