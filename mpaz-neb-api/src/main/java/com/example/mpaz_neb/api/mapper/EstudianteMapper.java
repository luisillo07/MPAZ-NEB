package com.example.mpaz_neb.api.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import com.example.mpaz_neb.api.dto.EstudianteResponse;
import com.example.mpaz_neb.api.model.Estudiante;

@Mapper(componentModel = "spring")
public interface EstudianteMapper {
    EstudianteResponse toResponse(Estudiante estudiante);
    List<EstudianteResponse> toResponseList(List<Estudiante> estudiantes);
}