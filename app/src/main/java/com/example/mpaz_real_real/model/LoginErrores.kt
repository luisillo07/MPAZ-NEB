package com.example.mpaz_real_real.model

data class LoginErrores(
    val usuario: String? = null,
    val contrasenia: String? = null,
    // true cuando el usuario o la contraseña no coinciden: el campo Usuario se pinta de
    // rojo SIN mensaje (el mensaje solo se muestra bajo Contraseña, en "contrasenia").
    val usuarioIncorrecto: Boolean = false
)
