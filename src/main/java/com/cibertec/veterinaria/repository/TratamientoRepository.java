package com.cibertec.veterinaria.repository;

import com.cibertec.veterinaria.entity.TratamientoMedicamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TratamientoRepository extends JpaRepository<TratamientoMedicamento, Long> {
    List<TratamientoMedicamento> findByConsultaIdConsulta(Long idConsulta);
}
