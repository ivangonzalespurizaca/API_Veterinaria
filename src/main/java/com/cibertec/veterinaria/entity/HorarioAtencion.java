package com.cibertec.veterinaria.entity;

import com.cibertec.veterinaria.entity.enums.TipoDiaSemana;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "horario_atencion",
        schema = "veterinaria_app",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"id_veterinario", "dia_semana"})
        }
)
public class HorarioAtencion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHorario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_veterinario", nullable = false)
    private Veterinario veterinario;

    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana", nullable = false)
    private TipoDiaSemana diaSemana;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;

    @Builder.Default
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Builder.Default
    @Column(name = "duracion_minutos")
    private Integer duracionMinutos = 30;
}