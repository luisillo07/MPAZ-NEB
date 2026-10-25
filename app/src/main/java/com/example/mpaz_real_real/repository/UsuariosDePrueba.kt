package com.example.mpaz_real_real.repository

import com.example.mpaz_real_real.data.local.entity.UsuarioEntity

val usuariosDePrueba = listOf(
    UsuarioEntity(usuario = "docente", contrasenia = "1234", tipoUsuario = "docente"),
    UsuarioEntity(usuario = "asistente", contrasenia = "1234", tipoUsuario = "asistente"),
    UsuarioEntity(usuario = "estudiante", contrasenia = "1234", tipoUsuario = "estudiante")
)