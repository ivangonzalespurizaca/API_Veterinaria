package com.veterinaria.api.repository;

import com.veterinaria.api.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    Optional<Usuario> findByFirebaseUid(String firebaseUid);

}
