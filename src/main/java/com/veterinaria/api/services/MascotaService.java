package com.veterinaria.api.services;


import com.veterinaria.api.DTO.MascotaDTO;
import com.veterinaria.api.models.Mascota;
import com.veterinaria.api.models.Raza;
import com.veterinaria.api.models.Usuario;
import com.veterinaria.api.repository.MascotaRepository;
import com.veterinaria.api.repository.RazaRepository;
import com.veterinaria.api.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MascotaService {

    private final MascotaRepository mascotaRepository;
    private final RazaRepository razaRepository;
    private final UsuarioRepository usuarioRepository;

    public MascotaService(
            MascotaRepository mascotaRepository,
            RazaRepository razaRepository,
            UsuarioRepository usuarioRepository) {

        this.mascotaRepository = mascotaRepository;
        this.razaRepository = razaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Mascota> listar() {
        return mascotaRepository.findAll();
    }

    public Mascota buscarPorId(String id) {
        return mascotaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
    }

    public Mascota guardar(MascotaDTO dto) {

        Raza raza = razaRepository.findById(dto.getIdRaza())
                .orElseThrow(() -> new RuntimeException("Raza no encontrada"));

        Usuario cliente = usuarioRepository.findById(dto.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Mascota mascota = new Mascota();
        mascota.setNombre_mascota(dto.getNombre_mascota());
        mascota.setRaza(raza);
        mascota.setCliente(cliente);
        mascota.setSexo(dto.getSexo());
        mascota.setFecha_nacimiento(dto.getFecha_nacimiento());
        mascota.setPeso_inicial(dto.getPeso_inicial());
        mascota.setFoto(dto.getFoto());
        mascota.setCodigo_qr(dto.getCodigo_qr());

        return mascotaRepository.save(mascota);
    }

    public Mascota actualizar(String id, MascotaDTO dto) {

        Mascota mascota = buscarPorId(id);

        Raza raza = razaRepository.findById(dto.getIdRaza())
                .orElseThrow(() -> new RuntimeException("Raza no encontrada"));

        Usuario cliente = usuarioRepository.findById(dto.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        mascota.setNombre_mascota(dto.getNombre_mascota());
        mascota.setRaza(raza);
        mascota.setCliente(cliente);
        mascota.setSexo(dto.getSexo());
        mascota.setFecha_nacimiento(dto.getFecha_nacimiento());
        mascota.setPeso_inicial(dto.getPeso_inicial());
        mascota.setFoto(dto.getFoto());
        mascota.setCodigo_qr(dto.getCodigo_qr());

        return mascotaRepository.save(mascota);
    }

    public void eliminar(String id) {
        mascotaRepository.deleteById(id);
    }
}