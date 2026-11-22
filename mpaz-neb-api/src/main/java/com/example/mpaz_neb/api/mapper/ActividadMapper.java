package com.example.mpaz_neb.api.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import com.example.mpaz_neb.api.dto.ActividadResponse;
import com.example.mpaz_neb.api.model.Actividad;

@Mapper(componentModel = "spring")
public interface ActividadMapper {
    ActividadResponse toResponse(Actividad actividad);
    List<ActividadResponse> toResponseList(List<Actividad> actividades);
}