package com.cibertec.veterinaria.service.impl;

import com.cibertec.veterinaria.dto.MascotaInfoDTO;
import com.cibertec.veterinaria.dto.MascotaRegisterDTO;
import com.cibertec.veterinaria.dto.MascotaUpdateDTO;
import com.cibertec.veterinaria.entity.Mascota;
import com.cibertec.veterinaria.entity.Usuario;
import com.cibertec.veterinaria.entity.enums.TipoEspecie;
import com.cibertec.veterinaria.mapper.MascotaMapper;
import com.cibertec.veterinaria.repository.MascotaRepository;
import com.cibertec.veterinaria.repository.UsuarioRepository;
import com.cibertec.veterinaria.service.CloudinaryService;
import com.cibertec.veterinaria.service.MascotaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MascotaServiceImpl implements MascotaService {

    private final MascotaRepository mascotaRepository;
    private final UsuarioRepository usuarioRepository;
    private final MascotaMapper mascotaMapper;
    private final CloudinaryService cloudinaryService;

    private static final String DEFAULT_PET_PHOTO = "https://res.cloudinary.com/dfid8iuf3/image/upload/q_auto/f_auto/v1775845460/huella_sltxjt.png";

    @Override
    public MascotaInfoDTO guardarMascota(MascotaRegisterDTO dto) {
        Usuario cliente = usuarioRepository.findById(dto.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + dto.getIdCliente()));

        Mascota mascota = mascotaMapper.toEntity(dto);
        mascota.setCliente(cliente);
        mascota.setFotoUrl(DEFAULT_PET_PHOTO);
        mascota.setActivo(true);

        return mascotaMapper.toInfoDTO(mascotaRepository.save(mascota));
    }

    @Override
    public List<MascotaInfoDTO> listarPorCliente(String idUsuario) {
        return mascotaRepository.findByClienteIdUsuarioAndActivoTrue(idUsuario)
                .stream()
                .map(mascotaMapper::toInfoDTO)
                .toList();
    }

    @Override
    public MascotaInfoDTO editarMascota(Long id, MascotaUpdateDTO dto) {
        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        mascotaMapper.updateEntityFromDTO(dto, mascota);

        return mascotaMapper.toInfoDTO(mascotaRepository.save(mascota));
    }

    @Override
    public List<MascotaInfoDTO> buscarPorDniDuenio(String dni) {
        return mascotaRepository.findByClienteDniAndActivoTrue(dni)
                .stream()
                .map(mascotaMapper::toInfoDTO)
                .toList();
    }

    @Override
    public List<MascotaInfoDTO> listarPorEspecie(TipoEspecie especie) {
        return mascotaRepository.findByEspecieAndActivoTrue(especie)
                .stream()
                .map(mascotaMapper::toInfoDTO)
                .toList();
    }

    @Override
    public void eliminarLogica(Long id) {
        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
        mascota.setActivo(false);
        mascotaRepository.save(mascota);
    }

    @Override
    public String actualizarFotoMascota(Long id, MultipartFile archivo) throws IOException {
        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        Map result = cloudinaryService.upload(archivo);
        String url = result.get("url").toString();

        mascota.setFotoUrl(url);
        mascotaRepository.save(mascota);

        return url;
    }

    @Override
    public MascotaInfoDTO obtenerPorId(Long id) {
        return mascotaRepository.findById(id)
                .map(mascotaMapper::toInfoDTO)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada con ID: " + id));
    }
}