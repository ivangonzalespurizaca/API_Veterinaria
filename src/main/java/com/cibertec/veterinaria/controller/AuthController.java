package com.cibertec.veterinaria.controller;

import com.cibertec.veterinaria.dto.AuthResponse;
import com.cibertec.veterinaria.dto.UsuarioInfoDTO;
import com.cibertec.veterinaria.dto.UsuarioRegisterDTO;
import com.cibertec.veterinaria.entity.enums.TipoGenero;
import com.cibertec.veterinaria.security.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/public/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sync")
    public ResponseEntity<?> login(@Valid @RequestBody AuthResponse request) {
        try {
            String idToken = request.getIdToken();
            if (idToken == null || idToken.isEmpty()) {
                return ResponseEntity.badRequest().body("El idToken es obligatorio");
            }
            return ResponseEntity.ok(authService.syncUserWithFirebase(idToken));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Error de autenticación: " + e.getMessage());
        }
    }

    @PostMapping("/registrar")
    public ResponseEntity<UsuarioInfoDTO> registrar(@Valid @RequestBody UsuarioRegisterDTO request) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.registrarNuevoUsuario(request.getIdToken(), request));
    }
}