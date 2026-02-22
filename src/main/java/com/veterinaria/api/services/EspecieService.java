package com.veterinaria.api.services;


import com.veterinaria.api.models.Especie;
import com.veterinaria.api.repository.EspecieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspecieService {

    private final EspecieRepository especieRepository;

    public EspecieService(EspecieRepository especieRepository) {
        this.especieRepository = especieRepository;
    }

    public List<Especie> listar() {
        return especieRepository.findAll();
    }

    public Especie buscarPorId(String id) {
        return especieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especie no encontrada"));
    }

    public Especie guardar(Especie especie) {
        return especieRepository.save(especie);
    }

    public Especie actualizar(String id, Especie data) {

        Especie especie = buscarPorId(id);

        especie.setNombre_especie(data.getNombre_especie());
        especie.setDefinicion(data.getDefinicion());
        especie.setImagen(data.getImagen());

        return especieRepository.save(especie);
    }

    public void eliminar(String id) {
        especieRepository.deleteById(id);
    }
}