package com.example.mpaz_real_real.model

data class CatalogoUiState(
    val modulo: String = "Todos",
    val curso: String = "Todos",
    val contenido: String = "Todos",
    val dificultad: String = "Todos",
    val opcionesModulo: List<String> = listOf("Todos", "Lectura", "Matemática"),
    val opcionesCurso: List<String> = listOf("Todos", "1.º básico", "2.º básico", "3.º básico", "4.º básico"),
    val opcionesContenido: List<String> = listOf(
        "Todos",
        "[CONTENIDO LECTURA 1]",
        "[CONTENIDO LECTURA 2]",
        "[CONTENIDO MATEMÁTICA 1]",
        "[CONTENIDO MATEMÁTICA 2]"
    ),
    val opcionesDificultad: List<String> = listOf("Todos", "Fácil", "Medio", "Difícil"),
    val actividades: List<Actividad> = emptyList(),
    val actividadesVisibles: List<Actividad> = emptyList(),
    val mensajeError: String? = null
)