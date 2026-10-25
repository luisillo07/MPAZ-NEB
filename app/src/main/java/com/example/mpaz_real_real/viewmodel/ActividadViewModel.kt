package com.example.mpaz_real_real.viewmodel

import com.example.mpaz_real_real.model.ActividadUiState
import com.example.mpaz_real_real.repository.actividadesDePrueba
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.room.Room
import com.example.mpaz_real_real.data.local.AppDatabase
import androidx.lifecycle.viewModelScope
import com.example.mpaz_real_real.data.local.entity.ResultadoEntity
import kotlinx.coroutines.launch
import android.net.Uri

import java.io.FileOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class ActividadViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database = Room.databaseBuilder(
        context = application,
        klass = AppDatabase::class.java,
        name = "mpazneb_database"
    ).fallbackToDestructiveMigration(true).build()
    private val resultadoDao = database.resultadoDao
    private val contentResolver = application.contentResolver
    private val _estado = MutableStateFlow(ActividadUiState())
    val estado: StateFlow<ActividadUiState> = _estado.asStateFlow()
    val resultados = resultadoDao.obtenerResultados().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    fun cargarActividad(codigo: String) {
        val actividad = actividadesDePrueba.find { it.codigo == codigo } ?: return
        _estado.value = ActividadUiState()
        viewModelScope.launch {
            val usados = resultadoDao.contarIntentos(codigo)
            _estado.value = ActividadUiState(
                actividad = actividad,
                respuestas = actividad.preguntas.map { null },
                intentosUsados = usados
            )
        }
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
        val actual = _estado.value
        val actividad = actual.actividad?: return
        viewModelScope.launch{
            resultadoDao.insertar(
                ResultadoEntity(
                    codigoActividad = actividad.codigo,
                    respuestas = actual.respuestas.toString(),
                    aciertos = (0 until actual.respuestas.size).count{esCorrecta(actual,it)},
                    totalPreguntas = actual.respuestas.size
                )
            )
        }
        _estado.update{
            it.copy(
                mostrarConfirmacion = false,
                intentosUsados = it.intentosUsados + 1,
                indiceRetroalimentacion = 0,
                mensajeRetroalimentacion = armarMensaje(it,0)
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

    fun onReintentar() {
        if (!_estado.value.quedanIntentos) return
        _estado.update {
            it.copy(
                indicePregunta = 0,
                respuestas = it.respuestas.map { null },
                errorRespuesta = null,
                indiceRetroalimentacion = 0,
                pantallazoPregunta = null,
                resumenGuardado = false
            )
        }
    }


    fun onPantallazoSeleccionado(uri: Uri?) {
        if (uri == null) return
        _estado.update { it.copy(pantallazoPregunta = uri) }
    }

    fun guardarResumen(uri: Uri?) {
        if (uri == null) return
        val texto = textoResumen()
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    contentResolver.openFileDescriptor(uri, "w")?.use { descriptor ->
                        FileOutputStream(descriptor.fileDescriptor).use { salida ->
                            salida.write(texto.toByteArray())
                        }
                    }
                }
                _estado.update { it.copy(resumenGuardado = true) }
            } catch (e: Exception) {
                // Si no se pudo escribir, la app no se cae
            }
        }
    }

    private fun textoResumen(): String {
        val actual = _estado.value
        val actividad = actual.actividad ?: return ""
        val aciertos = (0 until actual.respuestas.size).count { esCorrecta(actual, it) }
        return "MPAZ NEB - resumen del intento (datos ficticios)\n" +
                "Actividad: ${actividad.nombre} (${actividad.modulo}, ${actividad.curso})\n" +
                "Aciertos: $aciertos de ${actual.respuestas.size}\n" +
                "Intentos usados: ${actual.intentosUsados} de ${actividad.intentosPermitidos}\n"
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

