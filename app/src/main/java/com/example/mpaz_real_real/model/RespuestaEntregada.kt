package com.example.mpaz_real_real.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "respuestas")
data class RespuestaEntregada(
    @PrimaryKey
    val id: String,
    val codigoTemporal: String,
    val numeroPregunta: Int,
    val alternativaElegida: Int,
    val cantidadIntentos: Int
)