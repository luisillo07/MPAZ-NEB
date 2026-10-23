package com.example.mpaz_real_real.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mpaz_real_real.model.ActividadUiState
import com.example.mpaz_real_real.repository.actividadesDePrueba
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ActividadViewModel : ViewModel() {
    private val _estado = MutableStateFlow(ActividadUiState())
    val estado: StateFlow<ActividadUiState> = _estado.asStateFlow()
    fun cargarActividad(codigo: String) {
        val actividad = actividadesDePrueba.find { it.codigo == codigo } ?: return
        _estado.value = ActividadUiState(
            actividad = actividad,
            respuestas = actividad.preguntas.map { null }
        )
    }

    fun onAlternativaSeleccionada(posicion: Int) {
        _estado.update {
            val nuevas = it.respuestas.mapIndexed { indice, respuesta ->
                if (indice == it.indicePregunta) posicion else respuesta
            }
            it.copy(respuestas = nuevas, errorRespuesta = null)
        }
    }


    fun validarRespuesta(): Boolean {
        val actual = _estado.value
        val respondio = actual.respuestas[actual.indicePregunta] != null
        if (respondio) return true
        _estado.update {
            it.copy(errorRespuesta = "Debes elegir una alternativa para continuar.")
        }
        return false
    }

    fun onSiguiente() {
        _estado.update {
            if (it.esUltimaPregunta) {
                it.copy(mostrarConfirmacion = true, errorRespuesta = null)
            } else {
                it.copy(indicePregunta = it.indicePregunta + 1, errorRespuesta = null)
            }
        }
    }


    fun onCambiarRespuestas() {
        _estado.update { it.copy(mostrarConfirmacion = false, indicePregunta = 0) }
    }


    fun onEnviar() {
        _estado.update {
            it.copy(
                mostrarConfirmacion = false,
                intentosUsados = it.intentosUsados + 1,
                indiceRetroalimentacion = 0,
                mensajeRetroalimentacion = armarMensaje(it, 0)
            )
        }
    }

    fun onSiguienteRetroalimentacion() {
        if (_estado.value.esUltimaRetroalimentacion) return
        _estado.update {
            val siguiente = it.indiceRetroalimentacion + 1
            it.copy(
                indiceRetroalimentacion = siguiente,
                mensajeRetroalimentacion = armarMensaje(it, siguiente)
            )
        }
    }

    private fun esCorrecta(estado: ActividadUiState, indice: Int): Boolean {
        return estado.respuestas[indice] == estado.actividad?.preguntas?.get(indice)?.correcta
    }

    private fun armarMensaje(estado: ActividadUiState, indice: Int): String {
        val pregunta = estado.actividad?.preguntas?.get(indice) ?: return ""
        val inicio = if (esCorrecta(estado, indice)) "¡Muy bien!" else "¡Casi! Revisemos juntos."
        return "$inicio ${pregunta.retroalimentacion}"
    }
}

