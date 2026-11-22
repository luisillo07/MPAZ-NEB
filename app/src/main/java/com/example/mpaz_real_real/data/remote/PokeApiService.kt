package com.example.mpaz_real_real.data.remote

import com.example.mpaz_real_real.data.model.Pokemon
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApiService {
    @GET("/api/v2/pokemon/{id}")
    suspend fun obtenerPokemon(@Path("id") id: Int): Pokemon
}