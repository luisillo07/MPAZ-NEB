package com.example.mpaz_real_real.model

data class CatalogoUiState(
    val modulo: String = "Todos",
    val curso: String = "Todos",
    val contenido: String = "Todos",
    val dificultad: String = "Todos",
    val actividades: List<Actividad> = emptyList()
)