package com.cibertec.veterinaria.entity;

import com.cibertec.veterinaria.entity.enums.TipoEspecie;
import com.cibertec.veterinaria.entity.enums.TipoSexoMascota;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mascota", schema = "veterinaria_app")
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMascota;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", referencedColumnName = "id_usuario", nullable = false)
    private Usuario cliente;

    @Column(name = "nombre_mascota", nullable = false)
    private String nombreMascota;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEspecie especie;

    @Column(nullable = false)
    private String raza;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoSexoMascota sexo;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "peso_actual", nullable = false, precision = 5, scale = 2)
    private BigDecimal pesoActual;

    @Column(name = "foto_url")
    private String fotoUrl;

    @Builder.Default
    private Boolean activo = true;
}