package com.example.mpaz_real_real.model

data class FormularioActividadUiState(
    val esNuevo: Boolean = false,
    val id: String = "",
    val nombre: String = "",
    val modulo: String = "",
    val curso: String = "",
    val contenido: String = "",
    val objetivo: String = "",
    val dificultad: String = "",
    val instrucciones: String = "",
    val cantidadPreguntas: String = "",
    val intentosPermitidos: String = "",
    val estado: String = "",
    val fecha: String = "",
    val errores: FormularioActividadErrores = FormularioActividadErrores()
)

data class FormularioActividadErrores(
    val id: String? = null,
    val cantidadPreguntas: String? = null,
    val intentosPermitidos: String? = null
)