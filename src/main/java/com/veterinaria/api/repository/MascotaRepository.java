package com.veterinaria.api.repository;

import com.veterinaria.api.models.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MascotaRepository extends JpaRepository<Mascota,String> {
}
