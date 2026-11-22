package com.example.mpaz_neb.api.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.mpaz_neb.api.model.Actividad;

public interface ActividadRepository extends JpaRepository<Actividad, Integer> {
    List<Actividad> findByCodigoContainingIgnoreCase(String codigo);
}