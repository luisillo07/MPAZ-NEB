package com.example.mpaz_real_real.model

data class ActividadUiState(
    val actividad: Actividad? = null,
    val indicePregunta: Int = 0,
    val respuestas: List<Int?> = emptyList(),
    val errorRespuesta: String? = null,
    val mostrarConfirmacion: Boolean = false,
    val intentosUsados: Int = 0,
    val indiceRetroalimentacion: Int = 0,
    val mensajeRetroalimentacion: String = ""
) {
    val esUltimaPregunta: Boolean
        get() = indicePregunta == respuestas.size - 1
    val esUltimaRetroalimentacion: Boolean
        get() = indiceRetroalimentacion == respuestas.size - 1
}


