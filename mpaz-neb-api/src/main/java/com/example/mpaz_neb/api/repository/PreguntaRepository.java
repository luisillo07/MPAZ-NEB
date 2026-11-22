package com.example.mpaz_neb.api.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.mpaz_neb.api.model.Pregunta;

public interface PreguntaRepository extends JpaRepository<Pregunta, Integer> {
    List<Pregunta> findByActividadCodigoIgnoreCase(String actividadCodigo);
}