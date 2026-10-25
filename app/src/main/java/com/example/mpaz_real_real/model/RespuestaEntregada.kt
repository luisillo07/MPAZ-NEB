package com.example.mpaz_real_real.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "respuestas")
data class RespuestaEntregada(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val codigoTemporal: String = "",
    val numeroPregunta: Int = 0,
    val alternativaElegida: Int = -1,
    val intentos: Int = 0
)