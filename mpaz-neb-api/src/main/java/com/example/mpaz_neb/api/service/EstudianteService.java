package com.example.mpaz_neb.api.service;

import java.util.List;
import com.example.mpaz_neb.api.dto.EstudianteResponse;

public interface EstudianteService {
    List<EstudianteResponse> obtenerEstudiantes();
}