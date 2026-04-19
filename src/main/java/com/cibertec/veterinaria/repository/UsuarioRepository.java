package com.cibertec.veterinaria.repository;

import com.cibertec.veterinaria.entity.Usuario;
import com.cibertec.veterinaria.entity.Veterinario;
import com.cibertec.veterinaria.entity.enums.TipoRol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    Optional<Usuario> findByEmailAndActivoTrue(String email);
    List<Usuario> findByRol(TipoRol rol);
    boolean existsByDni(String dni);
}
