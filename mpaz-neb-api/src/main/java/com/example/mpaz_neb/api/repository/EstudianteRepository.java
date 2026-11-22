package com.example.mpaz_neb.api.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.mpaz_neb.api.model.Estudiante;

public interface EstudianteRepository extends JpaRepository<Estudiante, Integer> {
}