package com.example.mpaz_neb.api.service;

import java.util.List;
import com.example.mpaz_neb.api.dto.ActividadRequest;
import com.example.mpaz_neb.api.dto.ActividadResponse;

public interface ActividadService {
    public List<ActividadResponse> obtenerActividades(String search);
    public ActividadResponse obtenerActividad(Integer id);
    public ActividadResponse crearActividad(ActividadRequest actividad);
    public ActividadResponse actualizarActividad(Integer id, ActividadRequest actividad);
    public void eliminarActividad(Integer id);
}