package com.example.mpaz_real_real.repository

import com.example.mpaz_real_real.data.model.ActividadResponse
import com.example.mpaz_real_real.data.remote.RetrofitInstance
import com.example.mpaz_real_real.model.Actividad
import com.example.mpaz_real_real.model.Pregunta

class ActividadRepository {
    suspend fun obtenerActividades(): List<ActividadResponse> {
        return RetrofitInstance.api.obtenerActividades()
    }

    suspend fun buscarActividades(codigo: String): List<ActividadResponse> {
        return RetrofitInstance.api.buscarActividades(codigo)
    }

    suspend fun crearActividad(actividad: Actividad): ActividadResponse {
        return RetrofitInstance.api.crearActividad(actividad)
    }

    suspend fun actualizarActividad(id: Int, actividad: Actividad): ActividadResponse {
        return RetrofitInstance.api.actualizarActividad(id, actividad)
    }

    suspend fun eliminarActividad(id: Int) {
        RetrofitInstance.api.eliminarActividad(id)
    }

    suspend fun obtenerPreguntas(codigo: String): List<Pregunta> {
        return RetrofitInstance.api.obtenerPreguntas(codigo)
    }

    suspend fun crearPregunta(pregunta: Pregunta): Pregunta {
        return RetrofitInstance.api.crearPregunta(pregunta)
    }

    suspend fun eliminarPregunta(id: Long) {
        RetrofitInstance.api.eliminarPregunta(id)
    }
}