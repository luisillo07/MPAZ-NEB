package com.example.mpaz_neb.api.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.mpaz_neb.api.dto.EstudianteResponse;
import com.example.mpaz_neb.api.mapper.EstudianteMapper;
import com.example.mpaz_neb.api.repository.EstudianteRepository;

@Service
public class EstudianteServiceImpl implements EstudianteService {
    private final EstudianteRepository estudianteRepository;
    private final EstudianteMapper estudianteMapper;
    public EstudianteServiceImpl(EstudianteRepository estudianteRepository, EstudianteMapper estudianteMapper) {
        this.estudianteRepository = estudianteRepository;
        this.estudianteMapper = estudianteMapper;
    }

    @Override
    public List<EstudianteResponse> obtenerEstudiantes() {
        return estudianteMapper.toResponseList(estudianteRepository.findAll());
    }
}