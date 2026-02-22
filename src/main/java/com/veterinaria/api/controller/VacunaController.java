package com.veterinaria.api.controller;

import com.veterinaria.api.models.Vacuna;
import com.veterinaria.api.services.VacunaService;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/vacunas")
@CrossOrigin(origins = "*") // útil si consumes desde Android
public class VacunaController {

    private final VacunaService vacunaService;

    public VacunaController(VacunaService vacunaService) {
        this.vacunaService = vacunaService;
    }

    // 🔹 GET - Listar todas
    @GetMapping
    public List<Vacuna> listar() {
        return vacunaService.listar();
    }

    // 🔹 GET - Buscar por ID
    @GetMapping("/{id}")
    public Vacuna buscar(@PathVariable String id) {
        return vacunaService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Vacuna no encontrada"));
    }

    // 🔹 POST - Crear
    @PostMapping
    public Vacuna crear(@RequestBody Vacuna vacuna) {
        return vacunaService.guardar(vacuna);
    }

    // 🔹 PUT - Actualizar
    @PutMapping("/{id}")
    public Vacuna actualizar(@PathVariable String id, @RequestBody Vacuna vacuna) {
        return vacunaService.actualizar(id, vacuna);
    }

    // 🔹 DELETE - Eliminar
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable String id) {
        vacunaService.eliminar(id);
    }
}
