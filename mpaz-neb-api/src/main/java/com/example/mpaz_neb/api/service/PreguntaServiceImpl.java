package com.example.mpaz_neb.api.service;

import com.example.mpaz_neb.api.dto.PreguntaResponse;
import com.example.mpaz_neb.api.exception.NoEncontradoException;
import com.example.mpaz_neb.api.mapper.PreguntaMapper;
import java.util.List;
import org.springframework.stereotype.Service;
import com.example.mpaz_neb.api.model.Pregunta;
import com.example.mpaz_neb.api.repository.PreguntaRepository;

@Service
public class PreguntaServiceImpl implements PreguntaService {
    private final PreguntaRepository preguntaRepository;
    private final PreguntaMapper preguntaMapper;
    public PreguntaServiceImpl(PreguntaRepository preguntaRepository, PreguntaMapper preguntaMapper) {
        this.preguntaRepository = preguntaRepository;
        this.preguntaMapper = preguntaMapper;
    }

    @Override
    public List<PreguntaResponse> obtenerPreguntas(String actividad) {
        List<Pregunta> preguntas;
        if (actividad == null || actividad.isEmpty()) {
            preguntas = preguntaRepository.findAll();
        } else {
            preguntas = preguntaRepository.findByActividadCodigoIgnoreCase(actividad);
        }
        return preguntaMapper.toResponseList(preguntas);
    }

    @Override
    public PreguntaResponse obtenerPregunta(Integer id) {
        return preguntaMapper.toResponse(preguntaRepository
                .findById(id)
                .orElseThrow(() -> new NoEncontradoException("Pregunta no encontrada")));
    }
}