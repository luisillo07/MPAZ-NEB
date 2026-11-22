package com.example.mpaz_neb.api.service;

import com.example.mpaz_neb.api.dto.ActividadResponse;
import com.example.mpaz_neb.api.exception.NoEncontradoException;
import com.example.mpaz_neb.api.mapper.ActividadMapper;
import java.util.List;
import org.springframework.stereotype.Service;
import com.example.mpaz_neb.api.model.Actividad;
import com.example.mpaz_neb.api.repository.ActividadRepository;

@Service
public class ActividadServiceImpl implements ActividadService {
    private final ActividadRepository actividadRepository;
    private final ActividadMapper actividadMapper;
    public ActividadServiceImpl(ActividadRepository actividadRepository, ActividadMapper actividadMapper) {
        this.actividadRepository = actividadRepository;
        this.actividadMapper = actividadMapper;
    }

    @Override
    public List<ActividadResponse> obtenerActividades(String search) {
        List<Actividad> actividades;
        if (search == null || search.isEmpty()) {
            actividades = actividadRepository.findAll();
        } else {
            actividades = actividadRepository.findByCodigoContainingIgnoreCase(search);
        }
        return actividadMapper.toResponseList(actividades);
    }

    @Override
    public ActividadResponse obtenerActividad(Integer id) {
        return actividadMapper.toResponse(actividadRepository
                .findById(id)
                .orElseThrow(() -> new NoEncontradoException("Actividad no encontrada")));
    }
}