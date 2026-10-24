package com.example.mpaz_real_real.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.mpaz_real_real.data.local.entity.ResultadoEntity

@Dao
interface ResultadoDao {

    @Insert
    suspend fun insertar(resultado: ResultadoEntity)
}