package com.cibertec.veterinaria.repository;

import com.cibertec.veterinaria.entity.VacunaAplicada;
import com.cibertec.veterinaria.entity.enums.TipoEstadoVacuna;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VacunaAplicadaRepository extends JpaRepository<VacunaAplicada, Long> {
    List<VacunaAplicada> findByMascotaIdMascotaOrderByFechaProgramadaAsc(Long idMascota);

    List<VacunaAplicada> findByConsultaIdConsulta(Long idConsulta);

    List<VacunaAplicada> findByEstadoAndFechaProgramada(TipoEstadoVacuna estado, java.time.LocalDate fecha);
}
