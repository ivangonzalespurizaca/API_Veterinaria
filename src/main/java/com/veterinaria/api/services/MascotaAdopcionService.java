package com.veterinaria.api.services;


import com.veterinaria.api.DTO.MascotaAdopcionDTO;
import com.veterinaria.api.models.Especie;
import com.veterinaria.api.models.MascotaAdopcion;
import com.veterinaria.api.models.Raza;
import com.veterinaria.api.models.enums.EstadoAdopcion;
import com.veterinaria.api.repository.EspecieRepository;
import com.veterinaria.api.repository.MascotaAdopcionRepository;
import com.veterinaria.api.repository.RazaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MascotaAdopcionService {

    private final MascotaAdopcionRepository repository;
    private final RazaRepository razaRepository;
    private final EspecieRepository especieRepository;

    public MascotaAdopcionService(
            MascotaAdopcionRepository repository,
            RazaRepository razaRepository,
            EspecieRepository especieRepository) {

        this.repository = repository;
        this.razaRepository = razaRepository;
        this.especieRepository = especieRepository;
    }

    public List<MascotaAdopcion> listar() {
        return repository.findAll();
    }

    public MascotaAdopcion buscarPorId(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
    }

    public MascotaAdopcion guardar(MascotaAdopcionDTO dto) {

        Raza raza = razaRepository.findById(dto.getIdRaza())
                .orElseThrow(() -> new RuntimeException("Raza no encontrada"));

        Especie especie = especieRepository.findById(dto.getIdEspecie())
                .orElseThrow(() -> new RuntimeException("Especie no encontrada"));

        MascotaAdopcion mascota = new MascotaAdopcion();
        mascota.setNombre_mascota(dto.getNombre_mascota());
        mascota.setSexo(dto.getSexo());
        mascota.setEdad_estimada(dto.getEdad_estimada());
        mascota.setFoto(dto.getFoto());
        mascota.setRaza(raza);
        mascota.setEspecie(especie);
        mascota.setEstado(EstadoAdopcion.Disponible);

        return repository.save(mascota);
    }

    // 🔥 En vez de borrar, cambiamos estado
    public MascotaAdopcion marcarComoAdoptado(String id) {

        MascotaAdopcion mascota = buscarPorId(id);
        mascota.setEstado(EstadoAdopcion.Adoptado);

        return repository.save(mascota);
    }

    public MascotaAdopcion actualizar(String id, MascotaAdopcionDTO dto) {

        MascotaAdopcion mascota = buscarPorId(id);

        Raza raza = razaRepository.findById(dto.getIdRaza())
                .orElseThrow(() -> new RuntimeException("Raza no encontrada"));

        Especie especie = especieRepository.findById(dto.getIdEspecie())
                .orElseThrow(() -> new RuntimeException("Especie no encontrada"));

        mascota.setNombre_mascota(dto.getNombre_mascota());
        mascota.setSexo(dto.getSexo());
        mascota.setEdad_estimada(dto.getEdad_estimada());
        mascota.setFoto(dto.getFoto());
        mascota.setRaza(raza);
        mascota.setEspecie(especie);

        return repository.save(mascota);
    }
}