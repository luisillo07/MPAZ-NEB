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
    // Fechas que puede elegir el asistente al asignar una actividad (lunes a viernes, formato
    // día-mes-año). Son aparte de opcionesFecha, que usa el formulario del docente.
    val opcionesFechaAsignacion: List<String> = listOf(
        "23-11-2026", "24-11-2026", "25-11-2026", "26-11-2026", "27-11-2026"
    ),
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

    // Actividades de la pantalla "Seleccionar actividad": las que cumplen los filtros, primero las
    // disponibles y después las desactivadas (cada grupo conserva su orden). Los borradores (actividades
    // que el docente dejó incompletas) no se muestran.
    val actividadesParaAsignar: List<Actividad>
        get() {
            val filtradas = actividadesAsignables.filter { actividad ->
                (asistente.moduloAplicado == "Todos" || actividad.modulo == asistente.moduloAplicado) &&
                        (asistente.cursoAplicado == "Todos" || actividad.curso == asistente.cursoAplicado) &&
                        (asistente.contenidoAplicado == "Todos" || actividad.contenido == asistente.contenidoAplicado) &&
                        (asistente.dificultadAplicado == "Todos" || actividad.dificultad == asistente.dificultadAplicado)
            }
            return filtradas.filter { it.estado == "disponible" } +
                    filtradas.filter { it.estado != "disponible" }
        }

    // Actividades que el asistente puede ver: disponibles y desactivadas (sin borradores).
    private val actividadesAsignables: List<Actividad>
        get() = actividades.filter { it.estado == "disponible" || it.estado == "desactivada" }

    // Opciones de los filtros de "Seleccionar actividad". A diferencia de las listas de arriba
    // (que usa también el formulario del docente y traen todos los cursos, contenidos, etc.),
    // estas solo traen lo que existe en las actividades que el asistente ve. Los filtros son
    // independientes entre sí, así que hay combinaciones sin resultados (por ejemplo Lectura
    // con "Adición y sustracción hasta 100"): en ese caso se muestra "No hay actividades".
    val opcionesModuloAsistente: List<String>
        get() = opcionesModulo.filter { opcion ->
            opcion == "Todos" || actividadesAsignables.any { it.modulo == opcion }
        }

    val opcionesCursoAsistente: List<String>
        get() = opcionesCurso.filter { opcion ->
            opcion == "Todos" || actividadesAsignables.any { it.curso == opcion }
        }

    val opcionesContenidoAsistente: List<String>
        get() = listOf("Todos") + actividadesAsignables.map { it.contenido }.distinct()

    val opcionesDificultadAsistente: List<String>
        get() = opcionesDificultad.filter { opcion ->
            opcion == "Todos" || actividadesAsignables.any { it.dificultad == opcion }
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