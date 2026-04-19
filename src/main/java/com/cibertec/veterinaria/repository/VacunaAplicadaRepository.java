package com.cibertec.veterinaria.repository;

import com.cibertec.veterinaria.entity.VacunaAplicada;
import com.cibertec.veterinaria.entity.enums.TipoEstadoVacuna;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VacunaAplicadaRepository extends JpaRepository<VacunaAplicada, Long> {
    List<VacunaAplicada> findByMascotaIdMascotaOrderByProximaDosisAsc(Long idMascota);

    List<VacunaAplicada> findByCitaIdCita(Long idCita);

    List<VacunaAplicada> findByEstadoAndProximaDosis(TipoEstadoVacuna estado, java.time.LocalDate fecha);
}
