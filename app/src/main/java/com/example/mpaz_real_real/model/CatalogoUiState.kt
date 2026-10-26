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
    val opcionesContenido: List<String> = listOf("Todos", "Comprensión lectora", "Adición y sustracción hasta 100"),
    val opcionesDificultad: List<String> = listOf("Todos", "Fácil", "Medio", "Difícil"),
    val opcionesEstado: List<String> = listOf("Disponible", "Desactivada"),
    val opcionesFecha: List<String> = listOf("[FECHA1]", "[FECHA2]"),
    val actividades: List<Actividad> = emptyList(),
    val preguntas: List<Pregunta> = emptyList(),
    val codigoRevision: String = "",
    val mensajeError: String? = null,
    val mensajeExito: String? = null,
    val formulario: FormularioActividadUiState = FormularioActividadUiState(),
    val asistente: AsistenteUiState = AsistenteUiState()
) {
    val actividadesVisibles: List<Actividad>
        get() = actividades.filter { actividad ->
            (moduloAplicado == "Todos" || actividad.modulo == moduloAplicado) &&
                    (cursoAplicado == "Todos" || actividad.curso == cursoAplicado) &&
                    (contenidoAplicado == "Todos" || actividad.contenido == contenidoAplicado) &&
                    (dificultadAplicado == "Todos" || actividad.dificultad == dificultadAplicado)
        }

    val actividadRevisada: Actividad?
        get() = actividades.firstOrNull { it.codigo == codigoRevision }

    val preguntasRevisadas: List<Pregunta>
        get() = preguntas.filter { it.actividadCodigo == codigoRevision }

    val actividadesParaAsignar: List<Actividad>
        get() = actividades.filter { actividad ->
            actividad.estado == "disponible" &&
                    (asistente.moduloAplicado == "Todos" || actividad.modulo == asistente.moduloAplicado) &&
                    (asistente.cursoAplicado == "Todos" || actividad.curso == asistente.cursoAplicado) &&
                    (asistente.contenidoAplicado == "Todos" || actividad.contenido == asistente.contenidoAplicado) &&
                    (asistente.dificultadAplicado == "Todos" || actividad.dificultad == asistente.dificultadAplicado)
        }

    val opcionesEstudiante: List<String>
        get() = asistente.estudiantes.map { it.codigo + " · " + it.nombre }

    val asignacionRevisada: Asignacion?
        get() = asistente.asignaciones.firstOrNull { it.codigoTemporal == asistente.codigoAsignacionRevision }

    fun actividadPorCodigo(codigo: String): Actividad? =
        actividades.firstOrNull { it.codigo == codigo }

    fun nombreActividad(codigo: String): String =
        actividadPorCodigo(codigo)?.nombre ?: codigo

    fun preguntasDe(codigo: String): List<Pregunta> =
        preguntas.filter { it.actividadCodigo == codigo }
}