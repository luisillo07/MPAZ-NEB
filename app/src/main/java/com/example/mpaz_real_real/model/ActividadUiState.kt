package com.example.mpaz_real_real.model
import android.net.Uri

data class ActividadUiState(
    val actividad: Actividad? = null,
    val indicePregunta: Int = 0,
    val respuestas: List<Int?> = emptyList(),
    val errorRespuesta: String? = null,
    val mostrarConfirmacion: Boolean = false,
    val intentosUsados: Int = 0,
    val indiceRetroalimentacion: Int = 0,
    val mensajeRetroalimentacion: String = "",
    val pantallazoPregunta: Uri? = null,
    val resumenGuardado: Boolean = false
) {
    val esUltimaPregunta: Boolean
        get() = indicePregunta == respuestas.size - 1
    val esUltimaRetroalimentacion: Boolean
        get() = indiceRetroalimentacion == respuestas.size - 1

    val quedanIntentos: Boolean
        get() = intentosUsados < (actividad?.intentosPermitidos ?: 0)

    val respondidas: Int
        get() = respuestas.count { it != null }
}



