package com.example.mpaz_real_real.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "preguntas")
data class Pregunta(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val actividadCodigo: String = "",
    val tipo: String = "",
    val enunciado: String = "",
    val alternativaA: String = "",
    val alternativaB: String = "",
    val alternativaC: String = "",
    val correcta: Int = -1,
    val datosNumericos: String = "",
    val operacion: String = "",
    val procedimiento: String = "",
    val retroalimentacion: String = ""
)

fun Pregunta.alternativas(): List<String> = listOf(alternativaA, alternativaB, alternativaC)