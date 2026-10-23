package com.example.mpaz_real_real.model

data class Pregunta(
    val tipo: String,
    val enunciado: String,
    val alternativas: List<String>,
    val correcta: Int,
    val retroalimentacion: String
)
