package com.example.mpaz_real_real.data.remote

import com.example.mpaz_real_real.data.model.ActividadResponse
import retrofit2.http.GET

interface ApiService {
    @GET("/api/actividad")
    suspend fun obtenerActividades(): List<ActividadResponse>
}