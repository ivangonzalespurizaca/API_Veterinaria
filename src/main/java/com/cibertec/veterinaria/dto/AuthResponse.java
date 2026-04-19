package com.cibertec.veterinaria.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthResponse {
    @NotBlank
    private String idToken;
}