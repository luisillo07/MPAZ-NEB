package com.example.mpaz_neb.api.service;

import com.example.mpaz_neb.api.dto.ActividadRequest;
import com.example.mpaz_neb.api.dto.ActividadResponse;
import com.example.mpaz_neb.api.exception.ConflictoException;
import com.example.mpaz_neb.api.exception.NoEncontradoException;
import com.example.mpaz_neb.api.mapper.ActividadMapper;
import jakarta.transaction.Transactional;
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

    @Override
    @Transactional
    public ActividadResponse crearActividad(ActividadRequest actividadRequest) {
        validarCodigoUnico(null, actividadRequest.getCodigo());
        Actividad nuevaActividad = new Actividad();
        actividadMapper.toEntity(actividadRequest, nuevaActividad);
        return actividadMapper.toResponse(actividadRepository.save(nuevaActividad));
    }

    @Override
    @Transactional
    public ActividadResponse actualizarActividad(Integer id, ActividadRequest actividadRequest) {
        Actividad actividadEditada = actividadRepository
                .findById(id)
                .orElseThrow(() -> new NoEncontradoException("Actividad no encontrada"));
        validarCodigoUnico(id, actividadRequest.getCodigo());
        actividadMapper.toEntity(actividadRequest, actividadEditada);
        actividadEditada.setId(id);
        return actividadMapper.toResponse(actividadRepository.save(actividadEditada));
    }

    @Override
    public void eliminarActividad(Integer id) {
        if (!actividadRepository.existsById(id)) {
            throw new NoEncontradoException("Actividad no encontrada");
        }
        actividadRepository.deleteById(id);
    }

    private void validarCodigoUnico(Integer id, String codigo) {
        if (id == null) {
            if (actividadRepository.existsByCodigoIgnoreCase(codigo)) {
                throw new ConflictoException("Ya existe una actividad con ese código");
            }
        } else {
            Actividad actividad = actividadRepository.findById(id).orElse(null);
            if (actividad != null && !actividad.getCodigo().equalsIgnoreCase(codigo)) {
                if (actividadRepository.existsByCodigoIgnoreCase(codigo)) {
                    throw new ConflictoException("Ya existe una actividad con ese código");
                }
            }
        }
    }
}