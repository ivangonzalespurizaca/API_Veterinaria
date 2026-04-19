package com.cibertec.veterinaria.service;

import com.cibertec.veterinaria.dto.AgendaVetDTO;
import com.cibertec.veterinaria.dto.CitaInfoDTO;
import com.cibertec.veterinaria.dto.CitaRegisterDTO;
import com.cibertec.veterinaria.dto.SlotDTO;
import com.cibertec.veterinaria.entity.enums.TipoEstadoCita;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CitaService {
    List<SlotDTO> obtenerHorasDisponibles(Long idVeterinario, LocalDate fecha);
    // Consultas
    List<CitaInfoDTO> listarTodas(); // Admin
    List<CitaInfoDTO> listarPorVeterinario(String idUsuarioVet); // Vet
    List<CitaInfoDTO> listarPorCliente(String idUsuarioCliente); // Cliente (App iOS)
    CitaInfoDTO obtenerPorId(Long id);
    List<AgendaVetDTO> obtenerAgendaDelDia(Long idVeterinario, LocalDate fecha);
    // Filtros
    List<CitaInfoDTO> buscarTodoPorCriterio(String criterio);
    List<CitaInfoDTO> buscarCitasPropiasPorCriterio(String idUsuarioVet, String criterio);
    List<CitaInfoDTO> filtrarTodoPorEstado(TipoEstadoCita estado);
    List<CitaInfoDTO> filtrarPorVeterinarioYEstado(String idUsuarioVet, TipoEstadoCita estado);
    List<CitaInfoDTO> filtrarPorClienteYEstado(String idUsuarioCliente, TipoEstadoCita estado);


    // Acciones de registro y flujo
    CitaInfoDTO registrarCita(CitaRegisterDTO dto);

    // Gestión de Estados (Podemos usar un solo método genérico o específicos)
    CitaInfoDTO cambiarEstado(Long id, TipoEstadoCita nuevoEstado);
    CitaInfoDTO cancelarCita(Long id, String motivo);
    CitaInfoDTO reprogramarCita(Long id, LocalDate nuevaFecha, LocalTime nuevaHora);
    List<CitaInfoDTO> listarCitasDeHoy(String idUsuarioVet);

}
