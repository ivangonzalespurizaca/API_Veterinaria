package com.veterinaria.api.services;

import com.veterinaria.api.models.Vacuna;
import com.veterinaria.api.repository.VacunaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VacunaService {

    private final VacunaRepository vacunaRepository;

    public VacunaService(VacunaRepository vacunaRepository) {
        this.vacunaRepository = vacunaRepository;
    }

    public List<Vacuna> listar() {
        return vacunaRepository.findAll();
    }

    public Optional<Vacuna> buscarPorId(String id) {
        return vacunaRepository.findById(id);
    }

    public Vacuna guardar(Vacuna vacuna) {
        return vacunaRepository.save(vacuna);
    }

    public Vacuna actualizar(String id, Vacuna vacuna) {
        return vacunaRepository.findById(id)
                .map(v -> {
                    v.setNombreVacuna(vacuna.getNombreVacuna());
                    return vacunaRepository.save(v);
                })
                .orElseThrow(() -> new RuntimeException("Vacuna no encontrada"));
    }

    public void eliminar(String id) {
        vacunaRepository.deleteById(id);
    }
}