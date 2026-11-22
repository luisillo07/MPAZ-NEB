package com.example.mpaz_real_real.model

data class AsistenteUiState(
    val modulo: String = "Todos",
    val curso: String = "Todos",
    val contenido: String = "Todos",
    val dificultad: String = "Todos",
    val moduloAplicado: String = "Todos",
    val cursoAplicado: String = "Todos",
    val contenidoAplicado: String = "Todos",
    val dificultadAplicado: String = "Todos",
    // Alias del usuario que inició sesión como asistente (se completa al ingresar).
    val asistenteResponsable: String = "asistente",
    val estudiantes: List<Estudiante> = emptyList(),
    val asignaciones: List<Asignacion> = emptyList(),
    val respuestas: List<RespuestaEntregada> = emptyList(),
    val codigoAsignacionRevision: String = "",
    // Estado por el que se filtra la lista de asignaciones ("pendiente", "en desarrollo" o
    // "finalizada"). Vacío significa que se muestran todas.
    val filtroEstado: String = "",
    val formulario: AsignarFormUiState = AsignarFormUiState(),
    // Código temporal de la asignación que se acaba de crear. Mientras no sea null se muestra el
    // mensaje "Actividad asignada" con ese código.
    val codigoAsignado: String? = null,
    val mensajeError: String? = null
) {
    val asignacionesVisibles: List<Asignacion>
        get() = if (filtroEstado.isEmpty()) {
            asignaciones
        } else {
            asignaciones.filter { it.estado == filtroEstado }
        }

    fun nombreEstudiante(codigo: String): String =
        estudiantes.firstOrNull { it.codigo == codigo }?.nombre ?: codigo

    fun respuestasDe(codigoTemporal: String): List<RespuestaEntregada> =
        respuestas.filter { it.codigoTemporal == codigoTemporal }
}

data class AsignarFormUiState(
    val actividadCodigo: String = "",
    val estudiante: String = "",
    val fecha: String = "",
    val errores: AsignarFormErrores = AsignarFormErrores()
)

data class AsignarFormErrores(
    val estudiante: String? = null,
    val fecha: String? = null
)