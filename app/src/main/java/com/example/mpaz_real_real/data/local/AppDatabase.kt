package com.example.mpaz_real_real.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mpaz_real_real.data.local.dao.ResultadoDao
import com.example.mpaz_real_real.data.local.entity.ResultadoEntity

@Database(
    entities = [ResultadoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val resultadoDao: ResultadoDao
}