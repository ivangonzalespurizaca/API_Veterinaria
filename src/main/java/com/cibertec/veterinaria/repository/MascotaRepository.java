package com.cibertec.veterinaria.repository;

import com.cibertec.veterinaria.entity.Mascota;
import com.cibertec.veterinaria.entity.enums.TipoEspecie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MascotaRepository extends JpaRepository<Mascota, Long> {

    List<Mascota> findByClienteIdUsuarioAndActivoTrue(String idUsuario);

    List<Mascota> findByClienteDniAndActivoTrue(String dni);

    List<Mascota> findByEspecieAndActivoTrue(TipoEspecie especie);

}
