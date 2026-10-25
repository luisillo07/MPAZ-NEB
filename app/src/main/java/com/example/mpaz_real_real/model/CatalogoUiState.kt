package com.example.mpaz_real_real.model

data class CatalogoUiState(
    val modulo: String = "Todos",
    val curso: String = "Todos",
    val contenido: String = "Todos",
    val dificultad: String = "Todos",
    val moduloAplicado: String = "Todos",
    val cursoAplicado: String = "Todos",
    val contenidoAplicado: String = "Todos",
    val dificultadAplicado: String = "Todos",
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
    val opcionesEstado: List<String> = listOf("Disponible", "Desactivada"),
    val opcionesFecha: List<String> = listOf("[FECHA 1]", "[FECHA 2]"),
    val actividades: List<Actividad> = emptyList(),
    val mensajeError: String? = null,
    val mensajeExito: String? = null,
    val formulario: FormularioActividadUiState = FormularioActividadUiState()
) {
    val actividadesVisibles: List<Actividad>
        get() = actividades.filter { actividad ->
            (moduloAplicado == "Todos" || actividad.modulo == moduloAplicado) &&
                    (cursoAplicado == "Todos" || actividad.curso == cursoAplicado) &&
                    (contenidoAplicado == "Todos" || actividad.contenido == contenidoAplicado) &&
                    (dificultadAplicado == "Todos" || actividad.dificultad == dificultadAplicado)
        }
}