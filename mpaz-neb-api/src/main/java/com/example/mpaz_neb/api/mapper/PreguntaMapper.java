package com.example.mpaz_neb.api.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import com.example.mpaz_neb.api.dto.PreguntaResponse;
import com.example.mpaz_neb.api.model.Pregunta;

@Mapper(componentModel = "spring")
public interface PreguntaMapper {
    PreguntaResponse toResponse(Pregunta pregunta);
    List<PreguntaResponse> toResponseList(List<Pregunta> preguntas);
}