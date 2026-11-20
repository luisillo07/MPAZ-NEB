package com.example.mpaz_real_real.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "asignaciones")
data class Asignacion(
    @PrimaryKey
    val codigoTemporal: String,
    val estudianteCodigo: String,
    val actividadCodigo: String,
    val asistente: String,
    val fecha: String,
    val estado: String,
    val fechaLimite: Long,
    val minutosDuracion: Int,
    val inicio: Long?,
    val intentoAbierto: Boolean,
    val observacion: String
)