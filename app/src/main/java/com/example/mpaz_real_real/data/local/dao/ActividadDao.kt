package com.example.mpaz_real_real.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mpaz_real_real.model.Actividad
import kotlinx.coroutines.flow.Flow

@Dao
interface ActividadDao {

    @Query("SELECT * FROM actividades ORDER BY rowid")
    fun observarTodas(): Flow<List<Actividad>>

    @Query("SELECT COUNT(*) FROM actividades")
    suspend fun contar(): Int

    @Insert
    suspend fun insertar(actividad: Actividad)

    @Update
    suspend fun actualizar(actividad: Actividad)
}