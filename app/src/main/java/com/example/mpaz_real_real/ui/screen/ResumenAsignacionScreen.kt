package com.example.mpaz_real_real.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mpaz_real_real.model.alternativas
import com.example.mpaz_real_real.ui.components.CajaSoloLectura
import com.example.mpaz_real_real.ui.theme.GrisBarra
import com.example.mpaz_real_real.viewmodel.CatalogoViewModel


import com.example.mpaz_real_real.ui.components.BotonCatalogo
import com.example.mpaz_real_real.ui.components.CampoFormulario
import com.example.mpaz_real_real.ui.components.DialogoMensaje

import com.example.mpaz_real_real.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumenAsignacionScreen(
    navController: NavController,
    viewModel: CatalogoViewModel
) {
    val estado by viewModel.state.collectAsState()
    val asistente = estado.asistente
    val asignacion = estado.asignacionRevisada

    if (asignacion != null) {
        val actividad = estado.actividadPorCodigo(asignacion.actividadCodigo)
        val preguntas = estado.preguntasDe(asignacion.actividadCodigo)
        val respuestas = asistente.respuestasDe(asignacion.codigoTemporal)
        val etiqueta = if (actividad?.modulo == "Matemática") "Ejercicio" else "Pregunta"
        val cursoDificultad = if (actividad != null) "${actividad.curso} · ${actividad.dificultad}" else ""

        val terminada = asignacion.estado == "finalizada" || !asignacion.intentoAbierto
        var aciertos = 0
        var errores = 0
        var noRespondidas = 0
        respuestas.forEach { respuesta ->
            val indice = respuesta.numeroPregunta - 1
            if (indice in 0..preguntas.size - 1) {
                if (respuesta.alternativaElegida == -1) {
                    noRespondidas = noRespondidas + 1
                } else if (respuesta.alternativaElegida == preguntas[indice].correcta) {
                    aciertos = aciertos + 1
                } else {
                    errores = errores + 1
                }
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text("Asistente de aula", style = MaterialTheme.typography.bodySmall)
                            Text("Asignación y resultados", style = MaterialTheme.typography.titleLarge)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = GrisBarra)
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CajaSoloLectura("Estudiante ficticio", asistente.nombreEstudiante(asignacion.estudianteCodigo))
                CajaSoloLectura("Actividad asignada", actividad?.nombre ?: asignacion.actividadCodigo)
                CajaSoloLectura("Curso/dificultad", cursoDificultad)
                CajaSoloLectura("Asistente responsable", asignacion.asistente)
                CajaSoloLectura("Fecha", asignacion.fecha)
                CajaSoloLectura("Código temporal", asignacion.codigoTemporal)
                CajaSoloLectura("Estado", asignacion.estado)

                if (asignacion.estado != "pendiente") {
                    val titulo = if (asignacion.estado == "finalizada") {
                        "Respuestas entregadas"
                    } else if (terminada) {
                        "Respuestas del último intento"
                    } else {
                        "Respuestas hasta ahora"
                    }
                    Text(titulo)
                    if (respuestas.isEmpty()) {
                        Text("No comenzó")
                    } else {
                        respuestas.forEach { respuesta ->
                            val indice = respuesta.numeroPregunta - 1
                            if (indice in 0..preguntas.size - 1) {
                                val pregunta = preguntas[indice]
                                val alternativas = pregunta.alternativas()
                                val elegida = if (respuesta.alternativaElegida in 0..2) {
                                    alternativas[respuesta.alternativaElegida]
                                } else if (terminada) {
                                    "No respondida (no válida)"
                                } else {
                                    "Aún sin responder"
                                }

                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    border = BorderStroke(2.dp, Color.Black)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text("$etiqueta ${respuesta.numeroPregunta}", style = MaterialTheme.typography.titleMedium)
                                        Text(pregunta.enunciado)
                                        Text(elegida)
                                        if (respuesta.cantidadIntentos > 0) {
                                            Text("Intentos: ${respuesta.cantidadIntentos}")
                                        }
                                    }
                                }
                            }
                        }

                    CajaSoloLectura("Cantidad de aciertos", aciertos.toString())
                    if (terminada) {
                        CajaSoloLectura("Cantidad de errores (incluye no respondidas)", (errores + noRespondidas).toString())
                        CajaSoloLectura("No respondidas", noRespondidas.toString())
                    } else {
                        CajaSoloLectura("Cantidad de errores", errores.toString())
                        CajaSoloLectura("Aún sin responder", noRespondidas.toString())
                    }
                    CajaSoloLectura("Cantidad de intentos", actividad?.intentosPermitidos?.toString() ?: "")
                    }
                }

                Text("Observación pedagógica", style = MaterialTheme.typography.titleMedium)
                CampoFormulario(
                    etiqueta = "Observación",
                    valor = asistente.observacion,
                    onValorChange = viewModel::onObservacionChange,
                    error = asistente.errorObservacion,
                    lineasMinimas = 3
                )
                BotonCatalogo("Guardar observación", viewModel::onGuardarObservacion, Modifier.fillMaxWidth())
            }
        }

        if (asistente.mensajeExito != null) {
            DialogoMensaje("Listo", asistente.mensajeExito) {
                viewModel.onCerrarMensajeAsistente()
                navController.popBackStack(Screen.Asignaciones.route, false)
            }
        }
        if (asistente.mensajeError != null) {
            DialogoMensaje("Error", asistente.mensajeError, viewModel::onCerrarErrorAsistente)
        }
    }
}