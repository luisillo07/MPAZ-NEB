package com.example.mpaz_real_real.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mpaz_real_real.model.ActividadUiState
import com.example.mpaz_real_real.repository.actividadesDePrueba
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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
}