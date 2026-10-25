package com.example.mpaz_real_real.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mpaz_real_real.model.Actividad
import com.example.mpaz_real_real.model.CatalogoUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CatalogoViewModel : ViewModel() {

    private val actividadesIniciales = listOf(
        Actividad(1, "Actividad 1", "Lectura", "1.º básico", "[CONTENIDO LECTURA 1]", "Fácil", "disponible"),
        Actividad(2, "Actividad 2", "Lectura", "2.º básico", "[CONTENIDO LECTURA 2]", "Medio", "desactivada"),
        Actividad(3, "Actividad 3", "Matemática", "1.º básico", "[CONTENIDO MATEMÁTICA 1]", "Difícil", "disponible"),
        Actividad(4, "Actividad 4", "Matemática", "3.º básico", "[CONTENIDO MATEMÁTICA 2]", "Fácil", "disponible"),
        Actividad(5, "Actividad 5", "Lectura", "4.º básico", "[CONTENIDO LECTURA 2]", "Medio", "disponible"),
        Actividad(6, "Actividad 6", "Lectura", "", "", "", "borrador")
    )

    private val _state = MutableStateFlow(
        CatalogoUiState(
            actividades = actividadesIniciales,
            actividadesVisibles = actividadesIniciales
        )
    )

    val state: StateFlow<CatalogoUiState> = _state.asStateFlow()

    fun onModuloChange(valor: String) { _state.update { it.copy(modulo = valor) } }
    fun onCursoChange(valor: String) { _state.update { it.copy(curso = valor) } }
    fun onContenidoChange(valor: String) { _state.update { it.copy(contenido = valor) } }
    fun onDificultadChange(valor: String) { _state.update { it.copy(dificultad = valor) } }

    fun onFiltrarClick() {
        _state.update { estado ->
            estado.copy(
                actividadesVisibles = estado.actividades.filter { actividad ->
                    (estado.modulo == "Todos" || actividad.modulo == estado.modulo) &&
                            (estado.curso == "Todos" || actividad.curso == estado.curso) &&
                            (estado.contenido == "Todos" || actividad.contenido == estado.contenido) &&
                            (estado.dificultad == "Todos" || actividad.dificultad == estado.dificultad)
                }
            )
        }
    }

    fun reiniciarFiltros() {
        _state.update {
            it.copy(
                modulo = "Todos",
                curso = "Todos",
                contenido = "Todos",
                dificultad = "Todos",
                actividadesVisibles = it.actividades
            )
        }
    }

    fun onCambiarEstado(id: Int) {
        val actividad = _state.value.actividades.find { it.id == id }
        if (actividad != null) {
            when (actividad.estado) {
                "disponible" -> aplicarEstado(id, "desactivada")
                "desactivada" -> aplicarEstado(id, "disponible")
                else -> {
                    val faltantes = datosFaltantes(actividad)
                    if (faltantes.isNotEmpty()) {
                        _state.update { it.copy(mensajeError = armarMensaje(faltantes)) }
                    } else {
                        aplicarEstado(id, "disponible")
                    }
                }
            }
        }
    }

    fun onCerrarMensaje() {
        _state.update { it.copy(mensajeError = null) }
    }

    private fun aplicarEstado(id: Int, nuevoEstado: String) {
        _state.update {
            it.copy(
                actividades = cambiarEstado(it.actividades, id, nuevoEstado),
                actividadesVisibles = cambiarEstado(it.actividadesVisibles, id, nuevoEstado)
            )
        }
    }

    private fun cambiarEstado(lista: List<Actividad>, id: Int, nuevoEstado: String): List<Actividad> =
        lista.map { if (it.id == id) it.copy(estado = nuevoEstado) else it }

    private fun datosFaltantes(actividad: Actividad): List<String> {
        val faltantes = mutableListOf<String>()
        if (actividad.modulo.isBlank()) faltantes.add("el módulo")
        if (actividad.curso.isBlank()) faltantes.add("el curso")
        if (actividad.contenido.isBlank()) faltantes.add("el contenido")
        if (actividad.dificultad.isBlank()) faltantes.add("la dificultad")
        return faltantes
    }

    private fun armarMensaje(faltantes: List<String>): String {
        var texto = "Error: Falta decidir "
        for (i in 0..faltantes.size - 1) {
            if (i > 0 && i == faltantes.size - 1) {
                texto = texto + " y "
            } else if (i > 0) {
                texto = texto + ", "
            }
            texto = texto + faltantes[i]
        }
        return texto
    }
}