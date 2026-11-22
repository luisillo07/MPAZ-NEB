package com.example.mpaz_real_real.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PokeApiInstance {
    val api: PokeApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://pokeapi.co")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokeApiService::class.java)
    }
}