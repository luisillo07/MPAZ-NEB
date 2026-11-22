package com.example.mpaz_real_real.data.remote

import com.example.mpaz_real_real.data.model.ActividadResponse
import com.example.mpaz_real_real.model.Actividad
import com.example.mpaz_real_real.model.Pregunta
import retrofit2.http.GET
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/api/actividad")
    suspend fun obtenerActividades(): List<ActividadResponse>

    @GET("/api/actividad")
    suspend fun buscarActividades(@Query("search") codigo: String): List<ActividadResponse>

    @POST("/api/actividad")
    suspend fun crearActividad(@Body actividad: Actividad): ActividadResponse

    @PUT("/api/actividad/{id}")
    suspend fun actualizarActividad(@Path("id") id: Int, @Body actividad: Actividad): ActividadResponse

    @GET("/api/pregunta")
    suspend fun obtenerPreguntas(@Query("actividad") codigo: String): List<Pregunta>

    @POST("/api/pregunta")
    suspend fun crearPregunta(@Body pregunta: Pregunta): Pregunta

    @DELETE("/api/pregunta/{id}")
    suspend fun eliminarPregunta(@Path("id") id: Long)
}