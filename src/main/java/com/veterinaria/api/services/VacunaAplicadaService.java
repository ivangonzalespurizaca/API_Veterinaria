package com.veterinaria.api.services;

import com.veterinaria.api.models.Mascota;
import com.veterinaria.api.models.Vacuna;
import com.veterinaria.api.models.VacunaAplicada;
import com.veterinaria.api.repository.MascotaRepository;
import com.veterinaria.api.repository.VacunaAplicadaRepository;
import com.veterinaria.api.repository.VacunaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VacunaAplicadaService {

    private final VacunaAplicadaRepository repository;
    private final MascotaRepository mascotaRepository;
    private final VacunaRepository vacunaRepository;

    public VacunaAplicadaService(
            VacunaAplicadaRepository repository,
            MascotaRepository mascotaRepository,
            VacunaRepository vacunaRepository) {
        this.repository = repository;
        this.mascotaRepository = mascotaRepository;
        this.vacunaRepository = vacunaRepository;
    }

    public List<VacunaAplicada> listar() {
        return repository.findAll();
    }

    public VacunaAplicada buscarPorId(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vacuna aplicada no encontrada"));
    }

    public VacunaAplicada guardar(String idMascota, String idVacuna, VacunaAplicada data) {

        Mascota mascota = mascotaRepository.findById(idMascota)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        Vacuna vacuna = vacunaRepository.findById(idVacuna)
                .orElseThrow(() -> new RuntimeException("Vacuna no encontrada"));

        data.setMascota(mascota);
        data.setVacuna(vacuna);

        return repository.save(data);
    }

    public void eliminar(String id) {
        repository.deleteById(id);
    }
}