package com.cibertec.veterinaria.repository;

import com.cibertec.veterinaria.entity.HorarioAtencion;
import com.cibertec.veterinaria.entity.enums.TipoDiaSemana;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface HorarioAtencionRepository extends JpaRepository<HorarioAtencion, Long> {

    Optional<HorarioAtencion> findByVeterinarioIdVeterinarioAndDiaSemana(Long idVeterinario, TipoDiaSemana dia);

    Optional<HorarioAtencion> findByVeterinarioIdVeterinarioAndDiaSemanaAndActivoTrue(Long idVeterinario, TipoDiaSemana dia);
    Optional<HorarioAtencion> findByVeterinarioUsuarioIdUsuarioAndDiaSemanaAndActivoTrue(String idUsuario, TipoDiaSemana diaBusqueda);

    List<HorarioAtencion> findByVeterinarioIdVeterinario(Long idVeterinario);
    List<HorarioAtencion> findByVeterinarioIdVeterinarioAndActivoTrue(Long idVeterinario);
}
