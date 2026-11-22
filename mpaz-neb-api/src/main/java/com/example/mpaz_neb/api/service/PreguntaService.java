package com.example.mpaz_neb.api.service;

import java.util.List;
import com.example.mpaz_neb.api.dto.PreguntaRequest;
import com.example.mpaz_neb.api.dto.PreguntaResponse;

public interface PreguntaService {
    public List<PreguntaResponse> obtenerPreguntas(String actividad);
    public PreguntaResponse obtenerPregunta(Integer id);
    public PreguntaResponse crearPregunta(PreguntaRequest pregunta);
    public PreguntaResponse actualizarPregunta(Integer id, PreguntaRequest pregunta);
    public void eliminarPregunta(Integer id);
}