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
        val actividad = actividadesDePrueba.find {it.codigo == codigo}?: return
        _estado.value = ActividadUiState(
            actividad = actividad,
            respuestas = actividad.preguntas.map {null}
        )
    }
    
    fun onAlternativaSeleccionada(posicion: Int) {
        _estado.update {
            val nuevas = it.respuestas.mapIndexed {indice,respuesta ->
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
}