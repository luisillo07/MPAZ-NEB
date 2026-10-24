package com.example.mpaz_real_real.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "resultados")
data class ResultadoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val codigoActividad: String,
    val respuestas: String,
    val aciertos: Int,
    val totalPreguntas: Int,
    val fecha: Long = System.currentTimeMillis()
)