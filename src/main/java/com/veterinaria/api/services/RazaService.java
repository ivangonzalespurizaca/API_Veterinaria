package com.veterinaria.api.services;


import com.veterinaria.api.DTO.RazaDTO;
import com.veterinaria.api.models.Especie;
import com.veterinaria.api.models.Raza;
import com.veterinaria.api.repository.EspecieRepository;
import com.veterinaria.api.repository.RazaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RazaService {

    private final RazaRepository razaRepository;
    private final EspecieRepository especieRepository;

    public RazaService(RazaRepository razaRepository,
                       EspecieRepository especieRepository) {
        this.razaRepository = razaRepository;
        this.especieRepository = especieRepository;
    }

    public List<Raza> listar() {
        return razaRepository.findAll();
    }

    public Raza buscarPorId(String id) {
        return razaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Raza no encontrada"));
    }

    public Raza guardar(RazaDTO dto) {

        Especie especie = especieRepository.findById(dto.getIdEspecie())
                .orElseThrow(() -> new RuntimeException("Especie no encontrada"));

        Raza raza = new Raza();
        raza.setNombre_raza(dto.getNombre_raza());
        raza.setEspecie(especie);

        return razaRepository.save(raza);
    }

    public Raza actualizar(String id, RazaDTO dto) {

        Raza raza = buscarPorId(id);

        Especie especie = especieRepository.findById(dto.getIdEspecie())
                .orElseThrow(() -> new RuntimeException("Especie no encontrada"));

        raza.setNombre_raza(dto.getNombre_raza());
        raza.setEspecie(especie);

        return razaRepository.save(raza);
    }

    public void eliminar(String id) {
        razaRepository.deleteById(id);
    }
}
