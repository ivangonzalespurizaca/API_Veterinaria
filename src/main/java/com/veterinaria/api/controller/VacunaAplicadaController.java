package com.veterinaria.api.controller;

import com.veterinaria.api.models.VacunaAplicada;
import com.veterinaria.api.services.VacunaAplicadaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vacunas-aplicadas")
@CrossOrigin(origins = "*")
public class VacunaAplicadaController {

    private final VacunaAplicadaService service;

    public VacunaAplicadaController(VacunaAplicadaService service) {
        this.service = service;
    }

    // 🔹 GET - Listar todas
    @GetMapping
    public List<VacunaAplicada> listar() {
        return service.listar();
    }


    @GetMapping("/{id}")
    public VacunaAplicada buscar(@PathVariable String id) {
        return service.buscarPorId(id);
    }


    @PostMapping
    public VacunaAplicada crear(
            @RequestParam String idMascota,
            @RequestParam String idVacuna,
            @RequestBody VacunaAplicada vacunaAplicada) {

        return service.guardar(idMascota, idVacuna, vacunaAplicada);
    }


    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable String id) {
        service.eliminar(id);
    }
}
