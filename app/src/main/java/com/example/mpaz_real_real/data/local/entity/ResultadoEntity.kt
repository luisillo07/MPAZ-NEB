package com.example.mpaz_real_real.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "resultados")
data class ResultadoEntity(
    @PrimaryKey
    val id: String,
    val codigoActividad: String,
    val respuestas: String,
    val aciertos: Int,
    val errores: Int,
    val noRespondidas: Int,
    val totalPreguntas: Int,
    val fecha: Long
)