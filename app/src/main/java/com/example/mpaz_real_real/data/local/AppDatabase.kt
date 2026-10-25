package com.example.mpaz_real_real.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mpaz_real_real.data.local.dao.ActividadDao
import com.example.mpaz_real_real.model.Actividad
import com.example.mpaz_real_real.model.Pregunta

@Database(
    entities = [Actividad::class, Pregunta::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val actividadDao: ActividadDao
}