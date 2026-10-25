package com.example.mpaz_real_real.model

data class LoginUiState(
    val tipoUsuario: String = "",
    val usuario: String = "",
    val contrasenia: String = "",
    val codigoTemporal: String = "",
    val errores: LoginErrores = LoginErrores(),
    val ingresoCorrecto: Boolean = false
)