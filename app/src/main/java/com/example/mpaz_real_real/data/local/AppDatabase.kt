package com.example.mpaz_real_real.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mpaz_real_real.data.local.dao.UsuarioDao
import com.example.mpaz_real_real.data.local.entity.UsuarioEntity

@Database(
    entities = [UsuarioEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val usuarioDao: UsuarioDao
}