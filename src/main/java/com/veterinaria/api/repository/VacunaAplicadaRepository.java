package com.veterinaria.api.repository;

import com.veterinaria.api.models.VacunaAplicada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VacunaAplicadaRepository extends JpaRepository<VacunaAplicada, String> {
}