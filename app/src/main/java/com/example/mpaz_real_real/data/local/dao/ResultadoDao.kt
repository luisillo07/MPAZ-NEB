package com.example.mpaz_real_real.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.mpaz_real_real.data.local.entity.ResultadoEntity
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ResultadoDao {

    @Insert
    suspend fun insertar(resultado: ResultadoEntity)
    @Query("SELECT COUNT(*) FROM resultados WHERE codigoActividad = :codigo")
    suspend fun contarIntentos(codigo: String): Int
    @Query("SELECT * FROM resultados")
    fun obtenerResultados(): Flow<List<ResultadoEntity>>
}