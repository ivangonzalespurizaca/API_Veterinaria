package com.cibertec.veterinaria.repository;

import com.cibertec.veterinaria.entity.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface VeterinarioRepository extends JpaRepository<Veterinario, Long> {
    @Query("SELECT v FROM Veterinario v WHERE " +
            "LOWER(v.usuario.nombres) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(v.usuario.apellidos) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "v.usuario.dni LIKE CONCAT('%', :filtro, '%')")
    List<Veterinario> buscarPorNombreODni(@Param("filtro") String filtro);

    Optional<Veterinario> findByUsuarioIdUsuario(String idUsuario);
}