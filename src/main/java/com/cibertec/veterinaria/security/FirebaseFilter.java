package com.cibertec.veterinaria.security;

import com.cibertec.veterinaria.repository.UsuarioRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor // Para inyectar el repositorio
public class FirebaseFilter extends OncePerRequestFilter {

    private final UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String idToken = header.substring(7);
            try {
                FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
                String uid = decodedToken.getUid();

                // 1. Intentar obtener el rol de Firebase
                String rol = (String) decodedToken.getClaims().get("rol");

                // 2. BACKUP: Si Firebase no tiene el rol, buscarlo en nuestra DB
                if (rol == null) {
                    rol = usuarioRepository.findById(uid)
                            .map(u -> u.getRol().name())
                            .orElse("CLIENTE"); // Rol básico si no existe en BD aún
                }

                // 3. Crear autoridades con prefijo ROLE_
                List<SimpleGrantedAuthority> authorities = List.of(
                        new SimpleGrantedAuthority("ROLE_" + rol.toUpperCase())
                );

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        uid,
                        null,
                        authorities
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                SecurityContextHolder.clearContext();
                logger.error("Error validando token de Firebase: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}