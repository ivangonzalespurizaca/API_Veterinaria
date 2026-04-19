package com.cibertec.veterinaria.repository;

import com.cibertec.veterinaria.entity.Vacuna;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VacunaRepository extends JpaRepository<Vacuna, Long> {
    List<Vacuna> findByActivoTrue();
}
