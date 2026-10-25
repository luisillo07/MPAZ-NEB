package com.example.mpaz_real_real.model

data class FormularioActividadUiState(
    val esNuevo: Boolean = false,
    val codigo: String = "",
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
    val tituloTexto: String = "",
    val tipoTexto: String = "",
    val contenidoTexto: String = "",
    val nivelVocabulario: String = "",
    val preguntas: List<Pregunta> = emptyList(),
    val nuevaPregunta: String = "",
    val nuevaAlternativaA: String = "",
    val nuevaAlternativaB: String = "",
    val nuevaAlternativaC: String = "",
    val nuevaCorrecta: Int = -1,
    val errores: FormularioActividadErrores = FormularioActividadErrores()
)

data class FormularioActividadErrores(
    val codigo: String? = null,
    val cantidadPreguntas: String? = null,
    val intentosPermitidos: String? = null,
    val nuevaPregunta: String? = null
)