package com.cibertec.veterinaria.security;

import com.cibertec.veterinaria.dto.UsuarioInfoDTO;
import com.cibertec.veterinaria.dto.UsuarioRegisterDTO;
import com.cibertec.veterinaria.entity.Usuario;
import com.cibertec.veterinaria.entity.enums.TipoRol;
import com.cibertec.veterinaria.mapper.UsuarioMapper;
import com.cibertec.veterinaria.repository.UsuarioRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String DEFAULT_USER_IMAGE = "https://res.cloudinary.com/dfid8iuf3/image/upload/v1775790000/1077114_vegvlp.png";
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioInfoDTO syncUserWithFirebase(String idToken) throws Exception {
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
        String uid = decodedToken.getUid();
        String email = decodedToken.getEmail();
        String nombre = (String) decodedToken.getClaims().get("name");

        Optional<Usuario> usuarioExistente = usuarioRepository.findById(uid);

        if (usuarioExistente.isPresent()) {
            return usuarioMapper.toUsuarioInfoDTO(usuarioExistente.get());
        }

        Usuario nuevoUsuario = Usuario.builder()
                .idUsuario(uid)
                .email(email)
                .nombres(nombre != null ? nombre : "Usuario Firebase")
                .apellidos("")
                .rol(TipoRol.CLIENTE)
                .activo(true)
                .fotoUrl(DEFAULT_USER_IMAGE)
                .dni("00000000")
                .build();

        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);

        return usuarioMapper.toUsuarioInfoDTO(usuarioGuardado);
    }

    public UsuarioInfoDTO registrarNuevoUsuario(String idToken, UsuarioRegisterDTO extraData) throws Exception {
        // 1. Validamos con Google para obtener el UID y Email
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
        String uid = decodedToken.getUid();

        if (usuarioRepository.existsByDni(extraData.getDni())) {
            throw new RuntimeException("El DNI " + extraData.getDni() + " ya se encuentra registrado.");
        }

        // 2. IMPORTANTE: Buscamos si ya existe para ACTUALIZARLO, si no, creamos instancia
        Usuario usuario = usuarioRepository.findById(uid).orElse(new Usuario());

        // 3. Seteamos los datos de identidad (Vienen de Firebase)
        usuario.setIdUsuario(uid);
        usuario.setEmail(decodedToken.getEmail());

        // 4. Seteamos los datos de negocio (Vienen de tu DTO)
        usuario.setNombres(extraData.getNombres()); // Asegura que no sea null
        usuario.setApellidos(extraData.getApellidos());
        usuario.setDni(extraData.getDni());
        usuario.setCelular(extraData.getCelular());
        usuario.setGenero(extraData.getGenero());
        if (usuario.getFotoUrl() == null || usuario.getFotoUrl().isBlank()) {
            usuario.setFotoUrl(DEFAULT_USER_IMAGE);
        }

        // 5. Datos por defecto
        TipoRol rolAsignado = TipoRol.CLIENTE;
        usuario.setRol(rolAsignado);
        usuario.setActivo(true);

        //asignarRolAFirebase(uid, rolAsignado.name());

        // 6. Al usar save() sobre un objeto que ya tiene ID, JPA hará un UPDATE
        try {
            Usuario usuarioGuardado = usuarioRepository.save(usuario);
            return usuarioMapper.toUsuarioInfoDTO(usuarioGuardado);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            // Si falla por DNI duplicado a nivel de base de datos
            throw new RuntimeException("Error de integridad: El DNI ya existe.");
        }
    }

    public void asignarRolAFirebase(String uid, String rol) throws Exception {
        Map<String, Object> claims = new HashMap<>();
        claims.put("rol", rol);

        FirebaseAuth.getInstance().setCustomUserClaims(uid, claims);
    }
}