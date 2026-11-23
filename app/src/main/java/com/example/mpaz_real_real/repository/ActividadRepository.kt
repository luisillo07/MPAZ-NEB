package com.example.mpaz_real_real.repository

import com.example.mpaz_real_real.data.model.ActividadResponse
import com.example.mpaz_real_real.data.remote.RetrofitInstance
import com.example.mpaz_real_real.model.Actividad
import com.example.mpaz_real_real.model.Pregunta

open class ActividadRepository {
    open suspend fun obtenerActividades(): List<ActividadResponse> {
        return RetrofitInstance.api.obtenerActividades()
    }

    open suspend fun buscarActividades(codigo: String): List<ActividadResponse> {
        return RetrofitInstance.api.buscarActividades(codigo)
    }

    open suspend fun crearActividad(actividad: Actividad): ActividadResponse {
        return RetrofitInstance.api.crearActividad(actividad)
    }

    open suspend fun actualizarActividad(id: Int, actividad: Actividad): ActividadResponse {
        return RetrofitInstance.api.actualizarActividad(id, actividad)
    }

    open suspend fun eliminarActividad(id: Int) {
        RetrofitInstance.api.eliminarActividad(id)
    }

    open suspend fun obtenerPreguntas(codigo: String): List<Pregunta> {
        return RetrofitInstance.api.obtenerPreguntas(codigo)
    }

    open suspend fun crearPregunta(pregunta: Pregunta): Pregunta {
        return RetrofitInstance.api.crearPregunta(pregunta)
    }

    open suspend fun eliminarPregunta(id: Long) {
        RetrofitInstance.api.eliminarPregunta(id)
    }
}