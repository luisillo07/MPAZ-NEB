package com.example.mpaz_real_real.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "actividades")
data class Actividad(
    @PrimaryKey
    val codigo: String,
    val nombre: String,
    val modulo: String,
    val curso: String,
    val contenido: String,
    val objetivo: String,
    val dificultad: String,
    val instrucciones: String,
    val cantidadPreguntas: Int?,
    val intentosPermitidos: Int?,
    val estado: String,
    val fecha: String,
    val tituloTexto: String,
    val tipoTexto: String,
    val contenidoTexto: String,
    val nivelVocabulario: String
)