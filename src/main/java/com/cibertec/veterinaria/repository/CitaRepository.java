package com.cibertec.veterinaria.repository;

import com.cibertec.veterinaria.entity.Cita;
import com.cibertec.veterinaria.entity.enums.TipoEstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    // Buscamos todas las citas que NO estén canceladas para no solapar
    @Query("SELECT c.hora FROM Cita c WHERE c.veterinario.idVeterinario = :idVet " +
            "AND c.fecha = :fecha AND c.estado != 'CANCELADA'")
    List<LocalTime> findHorasOcupadas(@Param("idVet") Long idVet, @Param("fecha") LocalDate fecha);

    long countByMascotaIdMascotaAndEstado(Long idMascota, TipoEstadoCita estado);

    // Para el Veterinario: Navega Cita -> Veterinario -> Usuario -> idUsuario
    List<Cita> findByVeterinarioUsuarioIdUsuarioOrderByFechaAscHoraAsc(String idUsuario);

    // Para el Cliente: Navega Cita -> Mascota -> Cliente (Usuario) -> idUsuario
    List<Cita> findByMascotaClienteIdUsuarioOrderByFechaDescHoraDesc(String idUsuario);

    // Para el Admin (Todas las citas ordenadas por lo más reciente)
    List<Cita> findAllByOrderByFechaAscHoraAsc();

    List<Cita> findByVeterinarioUsuarioIdUsuarioAndFechaAndEstadoNot(String idUsuario, LocalDate fecha, TipoEstadoCita estado);

    // Búsqueda para el Administrador (Busca en toda la base de datos)
    @Query("SELECT c FROM Cita c WHERE " +
            "LOWER(c.mascota.nombreMascota) LIKE LOWER(CONCAT('%', :criterio, '%')) OR " +
            "LOWER(c.mascota.cliente.nombres) LIKE LOWER(CONCAT('%', :criterio, '%')) OR " +
            "LOWER(c.mascota.cliente.apellidos) LIKE LOWER(CONCAT('%', :criterio, '%')) OR " +
            "c.mascota.cliente.dni LIKE CONCAT('%', :criterio, '%') " +
            "ORDER BY c.fecha ASC, c.hora ASC")
    List<Cita> buscarTodoPorCriterio(@Param("criterio") String criterio);

    // Búsqueda para el Veterinario (Solo entre sus propias citas)
    @Query("SELECT c FROM Cita c WHERE c.veterinario.usuario.idUsuario = :idVet AND (" +
            "LOWER(c.mascota.nombreMascota) LIKE LOWER(CONCAT('%', :criterio, '%')) OR " +
            "LOWER(c.mascota.cliente.nombres) LIKE LOWER(CONCAT('%', :criterio, '%')) OR " +
            "LOWER(c.mascota.cliente.apellidos) LIKE LOWER(CONCAT('%', :criterio, '%')) OR " +
            "c.mascota.cliente.dni LIKE CONCAT('%', :criterio, '%')) " +
            "ORDER BY c.fecha ASC, c.hora ASC")
    List<Cita> buscarPropiasPorCriterio(@Param("idVet") String idVet, @Param("criterio") String criterio);

    // Filtro global (Admin)
    List<Cita> findByEstadoOrderByFechaAscHoraAsc(TipoEstadoCita estado);

    // Filtro por Veterinario (Vet)
    List<Cita> findByVeterinarioUsuarioIdUsuarioAndEstadoOrderByFechaAscHoraAsc(String idUsuario, TipoEstadoCita estado);

    List<Cita> findByMascotaClienteIdUsuarioAndEstadoOrderByFechaDescHoraDesc(String idUsuario, TipoEstadoCita estado);

    List<Cita> findByVeterinarioUsuarioIdUsuarioAndFechaAndEstadoNotOrderByHoraAsc(String idUsuario, LocalDate fecha, TipoEstadoCita estado);
}
