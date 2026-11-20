package com.example.mpaz_real_real.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mpaz_real_real.model.Asignacion
import com.example.mpaz_real_real.model.Estudiante
import com.example.mpaz_real_real.model.RespuestaEntregada
import kotlinx.coroutines.flow.Flow

import androidx.room.Update
@Dao
interface AsignacionDao {

    @Query("SELECT * FROM estudiantes ORDER BY codigo")
    fun observarEstudiantes(): Flow<List<Estudiante>>

    @Query("SELECT * FROM asignaciones ORDER BY rowid")
    fun observarAsignaciones(): Flow<List<Asignacion>>

    @Query("SELECT * FROM respuestas ORDER BY codigoTemporal, numeroPregunta")
    fun observarRespuestas(): Flow<List<RespuestaEntregada>>

    @Query("SELECT * FROM asignaciones WHERE codigoTemporal = :codigo")
    suspend fun buscarAsignacion(codigo: String): Asignacion?

    @Query("SELECT COUNT(*) FROM estudiantes")
    suspend fun contarEstudiantes(): Int

    @Insert
    suspend fun insertarEstudiante(estudiante: Estudiante)

    @Insert
    suspend fun insertarAsignacion(asignacion: Asignacion)

    @Insert
    suspend fun insertarRespuesta(respuesta: RespuestaEntregada)

    @Update
    suspend fun actualizarAsignacion(asignacion: Asignacion)
}
