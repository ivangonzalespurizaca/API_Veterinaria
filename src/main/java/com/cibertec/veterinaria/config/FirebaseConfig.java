package com.cibertec.veterinaria.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource; // IMPORTANTE

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @Value("${FIREBASE_JSON_CONTENT:}")
    private String firebaseJsonContent;

    @Value("${firebase.config.path}")
    private String configPath;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            InputStream serviceAccount;

            // Prioridad 1: Contenido JSON (Railway)
            if (firebaseJsonContent != null && !firebaseJsonContent.trim().isEmpty()) {
                serviceAccount = new ByteArrayInputStream(
                        firebaseJsonContent.getBytes(StandardCharsets.UTF_8)
                );
            }
            // Prioridad 2: Archivo físico o Classpath (Local)
            else {
                // Si la ruta configurada usa "classpath:", usamos ClassPathResource de Spring
                if (configPath.startsWith("classpath:")) {
                    String cleanPath = configPath.replace("classpath:", "");
                    serviceAccount = new ClassPathResource(cleanPath).getInputStream();
                } else {
                    // Si es una ruta absoluta (como en algunos servidores), usamos el método viejo
                    serviceAccount = new FileInputStream(configPath);
                }
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            return FirebaseApp.initializeApp(options);
        }
        return FirebaseApp.getInstance();
    }
}