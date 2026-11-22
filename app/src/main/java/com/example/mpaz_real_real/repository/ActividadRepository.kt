package com.example.mpaz_real_real.repository

import com.example.mpaz_real_real.data.model.ActividadResponse
import com.example.mpaz_real_real.data.remote.RetrofitInstance

class ActividadRepository {
    suspend fun obtenerActividades(): List<ActividadResponse> {
        return RetrofitInstance.api.obtenerActividades()
    }
}