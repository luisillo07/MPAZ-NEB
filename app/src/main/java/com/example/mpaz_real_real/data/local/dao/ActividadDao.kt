package com.example.mpaz_real_real.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mpaz_real_real.model.Actividad
import com.example.mpaz_real_real.model.Pregunta
import kotlinx.coroutines.flow.Flow

@Dao
interface ActividadDao {

    @Query("SELECT * FROM actividades ORDER BY rowid")
    fun observarTodas(): Flow<List<Actividad>>

    @Query("SELECT * FROM preguntas ORDER BY id")
    fun observarPreguntas(): Flow<List<Pregunta>>

    @Query("SELECT * FROM actividades WHERE estado = 'disponible' ORDER BY rowid")
    fun observarDisponibles(): Flow<List<Actividad>>

    @Query("SELECT * FROM actividades WHERE codigo = :codigo")
    suspend fun buscar(codigo: String): Actividad?

    @Query("SELECT * FROM preguntas WHERE actividadCodigo = :codigo ORDER BY id")
    suspend fun obtenerPreguntas(codigo: String): List<Pregunta>

    @Query("SELECT COUNT(*) FROM actividades")
    suspend fun contar(): Int

    @Insert
    suspend fun insertar(actividad: Actividad)

    @Update
    suspend fun actualizar(actividad: Actividad)

    @Insert
    suspend fun insertarPregunta(pregunta: Pregunta)

    @Query("DELETE FROM preguntas WHERE actividadCodigo = :codigo")
    suspend fun borrarPreguntasDe(codigo: String)
}