package com.cibertec.veterinaria.repository;

import com.cibertec.veterinaria.entity.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    List<Consulta> findByCitaMascotaIdMascotaOrderByCitaFechaDescCitaHoraDesc(Long idMascota);

    List<Consulta> findByCitaVeterinarioUsuarioIdUsuarioOrderByCitaFechaDesc(String idUsuarioVet);

    List<Consulta> findByCitaVeterinarioUsuarioIdUsuarioAndCitaFecha(String idVet, LocalDate fecha);

    @Query("SELECT c FROM Consulta c WHERE " +
            "LOWER(c.cita.mascota.nombreMascota) LIKE LOWER(CONCAT('%', :criterio, '%')) OR " +
            "LOWER(c.cita.mascota.cliente.nombres) LIKE LOWER(CONCAT('%', :criterio, '%')) OR " +
            "LOWER(c.cita.mascota.cliente.apellidos) LIKE LOWER(CONCAT('%', :criterio, '%')) OR " +
            "c.cita.mascota.cliente.dni LIKE CONCAT('%', :criterio, '%') " +
            "ORDER BY c.cita.fecha DESC")
    List<Consulta> buscarPorCriterio(@Param("criterio") String criterio);

    boolean existsByCitaIdCita(Long idCita);
}
