package com.example.mpaz_real_real.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mpaz_real_real.data.local.entity.UsuarioEntity

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(usuario: UsuarioEntity)
    @Query("SELECT * FROM usuarios WHERE usuario = :usuario AND contrasenia = :contrasenia AND tipoUsuario = :tipoUsuario")
    suspend fun buscar(usuario: String, contrasenia: String, tipoUsuario: String): UsuarioEntity?
}