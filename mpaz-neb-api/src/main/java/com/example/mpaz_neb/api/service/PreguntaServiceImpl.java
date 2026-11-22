package com.example.mpaz_neb.api.service;

import com.example.mpaz_neb.api.dto.PreguntaRequest;
import com.example.mpaz_neb.api.dto.PreguntaResponse;
import com.example.mpaz_neb.api.exception.NoEncontradoException;
import com.example.mpaz_neb.api.mapper.PreguntaMapper;
import jakarta.transaction.Transactional;
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

    @Override
    @Transactional
    public PreguntaResponse crearPregunta(PreguntaRequest preguntaRequest) {
        Pregunta nuevaPregunta = new Pregunta();
        preguntaMapper.toEntity(preguntaRequest, nuevaPregunta);
        return preguntaMapper.toResponse(preguntaRepository.save(nuevaPregunta));
    }

    @Override
    @Transactional
    public PreguntaResponse actualizarPregunta(Integer id, PreguntaRequest preguntaRequest) {
        Pregunta preguntaEditada = preguntaRepository
                .findById(id)
                .orElseThrow(() -> new NoEncontradoException("Pregunta no encontrada"));
        preguntaMapper.toEntity(preguntaRequest, preguntaEditada);
        preguntaEditada.setId(id);
        return preguntaMapper.toResponse(preguntaRepository.save(preguntaEditada));
    }

    @Override
    public void eliminarPregunta(Integer id) {
        if (!preguntaRepository.existsById(id)) {
            throw new NoEncontradoException("Pregunta no encontrada");
        }
        preguntaRepository.deleteById(id);
    }
}