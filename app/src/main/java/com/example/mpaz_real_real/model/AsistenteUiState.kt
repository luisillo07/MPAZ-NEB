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
    val asistenteResponsable: String = "ASIST-001",
    val estudiantes: List<Estudiante> = emptyList(),
    val asignaciones: List<Asignacion> = emptyList(),
    val respuestas: List<RespuestaEntregada> = emptyList(),
    val codigoAsignacionRevision: String = "",
    val formulario: AsignarFormUiState = AsignarFormUiState(),
    val mensajeExito: String? = null
) {
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