package com.cibertec.veterinaria.service.impl;

import com.cibertec.veterinaria.dto.LogSistemaInfoDTO;
import com.cibertec.veterinaria.entity.LogSistema;
import com.cibertec.veterinaria.entity.Usuario;
import com.cibertec.veterinaria.mapper.LogSistemaMapper;
import com.cibertec.veterinaria.repository.LogSistemaRepository;
import com.cibertec.veterinaria.repository.UsuarioRepository;
import com.cibertec.veterinaria.service.LogSistemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogSistemaServiceImpl implements LogSistemaService {

    private final LogSistemaRepository logRepository;
    private final UsuarioRepository usuarioRepository;
    private final LogSistemaMapper logMapper;

    @Override
    @Transactional(readOnly = true)
    public List<LogSistemaInfoDTO> listarLogsPorFecha() {
        return logRepository.findAllByOrderByFechaRegistroDesc()
                .stream()
                .map(logMapper::toInfoDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LogSistemaInfoDTO> listarLogsPorRangoFechas(LocalDate inicio, LocalDate fin) {
        LocalDateTime fechaInicio = inicio.atStartOfDay();
        LocalDateTime fechaFin = fin.atTime(LocalTime.MAX);

        return logRepository.findByFechaRegistroBetweenOrderByFechaRegistroDesc(fechaInicio, fechaFin)
                .stream()
                .map(logMapper::toInfoDTO)
                .toList();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void registrarLog(String idUsuario, String tabla, String accion, String descripcion) {
        // Buscamos al usuario (si idUsuario es nulo, el log será del "SISTEMA")
        Usuario usuario = null;
        if (idUsuario != null) {
            usuario = usuarioRepository.findById(idUsuario).orElse(null);
        }

        LogSistema log = LogSistema.builder()
                .usuario(usuario)
                .tablaAfectada(tabla)
                .accion(accion)
                .descripcion(descripcion)
                .build();

        logRepository.save(log);
    }
}