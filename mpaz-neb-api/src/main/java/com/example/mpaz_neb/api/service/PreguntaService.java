package com.example.mpaz_neb.api.service;

import java.util.List;
import com.example.mpaz_neb.api.dto.PreguntaResponse;

public interface PreguntaService {
    public List<PreguntaResponse> obtenerPreguntas(String actividad);
    public PreguntaResponse obtenerPregunta(Integer id);
}