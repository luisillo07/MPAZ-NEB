package com.example.mpaz_neb.api.service;

import java.util.List;
import com.example.mpaz_neb.api.dto.ActividadResponse;

public interface ActividadService {
    public List<ActividadResponse> obtenerActividades(String search);
    public ActividadResponse obtenerActividad(Integer id);
}