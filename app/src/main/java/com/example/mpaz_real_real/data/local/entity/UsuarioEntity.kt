package com.example.mpaz_real_real.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey
    val usuario: String,
    val contrasenia: String,
    val tipoUsuario: String
)