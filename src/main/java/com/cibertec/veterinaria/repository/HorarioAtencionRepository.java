package com.cibertec.veterinaria.repository;

import com.cibertec.veterinaria.entity.HorarioAtencion;
import com.cibertec.veterinaria.entity.enums.TipoDiaSemana;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface HorarioAtencionRepository extends JpaRepository<HorarioAtencion, Long> {
    // Para ver el horario completo de un veterinario (Admin y el propio Vet)
    List<HorarioAtencion> findByVeterinarioUsuarioIdUsuario(String idUsuario);

    List<HorarioAtencion> findByVeterinarioUsuarioIdUsuarioAndActivoTrue(String idUsuario);

    Optional<HorarioAtencion> findByVeterinarioUsuarioIdUsuarioAndDiaSemana(String idUsuario, TipoDiaSemana dia);

    Optional<HorarioAtencion> findByVeterinarioIdVeterinarioAndDiaSemanaAndActivoTrue(Long idVeterinario, TipoDiaSemana dia);

}
