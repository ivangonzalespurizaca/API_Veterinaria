package com.cibertec.veterinaria.entity;

import com.cibertec.veterinaria.entity.enums.TipoEspecialidad;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "veterinario", schema = "veterinaria_app")
public class Veterinario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVeterinario;

    @OneToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", unique = true, nullable = false)
    private Usuario usuario;

    @Column(name = "num_colegiatura", nullable = false, unique = true, length = 20)
    private String numColegiatura;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEspecialidad especialidad;
}