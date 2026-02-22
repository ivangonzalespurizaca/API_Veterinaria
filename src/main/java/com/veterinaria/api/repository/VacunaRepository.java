package com.veterinaria.api.repository;

import com.veterinaria.api.models.Vacuna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacunaRepository extends JpaRepository<Vacuna, String> {

    List<Vacuna> findByNombreVacunaContainingIgnoreCase(String nombreVacuna);

}