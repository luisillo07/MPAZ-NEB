package com.example.mpaz_real_real.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mpaz_real_real.model.Actividad
import com.example.mpaz_real_real.model.CatalogoUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CatalogoViewModel : ViewModel() {
    private val _state = MutableStateFlow(
        CatalogoUiState(
            actividades = listOf(
                Actividad("[NOMBRE DE ACTIVIDAD]", "[MÓDULO]", "[CURSO]", "[CONTENIDO]", "[DIFICULTAD]", true),
                Actividad("[NOMBRE DE ACTIVIDAD]", "[MÓDULO]", "[CURSO]", "[CONTENIDO]", "[DIFICULTAD]", false)
            )
        )
    )

    val state: StateFlow<CatalogoUiState> = _state.asStateFlow()

    fun onModuloChange(valor: String) { _state.update { it.copy(modulo = valor) } }
    fun onCursoChange(valor: String) { _state.update { it.copy(curso = valor) } }
    fun onContenidoChange(valor: String) { _state.update { it.copy(contenido = valor) } }
    fun onDificultadChange(valor: String) { _state.update { it.copy(dificultad = valor) } }
}