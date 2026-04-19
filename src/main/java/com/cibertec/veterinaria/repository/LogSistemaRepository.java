package com.cibertec.veterinaria.repository;

import com.cibertec.veterinaria.entity.LogSistema;
import lombok.extern.java.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface LogSistemaRepository extends JpaRepository<LogSistema, Long> {

    List<LogSistema> findByFechaRegistroBetweenOrderByFechaRegistroDesc(
            LocalDateTime inicio,
            LocalDateTime fin
    );

    List<LogSistema> findAllByOrderByFechaRegistroDesc();
}
