package com.example.mpaz_real_real.model

data class Actividad(
    val codigo: String,
    val nombre: String,
    val modulo: String,
    val curso: String,
    val contenido: String,
    val objetivo: String,
    val dificultad: String,
    val estado: String,
    val fecha: String,
    val instrucciones: String,
    val tituloTexto: String,
    val contenidoTexto: String,
    val intentosPermitidos: Int,
    val preguntas: List<Pregunta>
) {
    val cantidadPreguntas: Int
        get() = preguntas.size
}
