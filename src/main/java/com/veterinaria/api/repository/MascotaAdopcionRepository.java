package com.veterinaria.api.repository;

import com.veterinaria.api.models.MascotaAdopcion;
import com.veterinaria.api.models.enums.EstadoAdopcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MascotaAdopcionRepository extends JpaRepository<MascotaAdopcion, String> {
    List<MascotaAdopcion> findByEstado(EstadoAdopcion estado);


}
