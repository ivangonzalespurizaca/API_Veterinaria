package com.veterinaria.api.repository;

import com.veterinaria.api.models.Raza;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RazaRepository extends JpaRepository<Raza,String> {
}
