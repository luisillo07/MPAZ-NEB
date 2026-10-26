package com.example.mpaz_real_real.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mpaz_real_real.data.local.dao.ActividadDao
import com.example.mpaz_real_real.data.local.dao.AsignacionDao
import com.example.mpaz_real_real.data.local.dao.ResultadoDao
import com.example.mpaz_real_real.data.local.dao.UsuarioDao
import com.example.mpaz_real_real.data.local.entity.ResultadoEntity
import com.example.mpaz_real_real.data.local.entity.UsuarioEntity
import com.example.mpaz_real_real.model.Actividad
import com.example.mpaz_real_real.model.Asignacion
import com.example.mpaz_real_real.model.Estudiante
import com.example.mpaz_real_real.model.Pregunta
import com.example.mpaz_real_real.model.RespuestaEntregada

@Database(
    entities = [ResultadoEntity::class, UsuarioEntity::class, Actividad::class, Pregunta::class, Estudiante::class, Asignacion::class, RespuestaEntregada::class],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val resultadoDao: ResultadoDao
    abstract val usuarioDao: UsuarioDao
    abstract val actividadDao: ActividadDao
    abstract val asignacionDao: AsignacionDao
}