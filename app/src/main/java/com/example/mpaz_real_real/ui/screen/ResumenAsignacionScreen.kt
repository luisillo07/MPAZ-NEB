package com.example.mpaz_real_real.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mpaz_real_real.model.alternativas
import com.example.mpaz_real_real.ui.components.EncabezadoAsistente
import com.example.mpaz_real_real.ui.components.TablaAsignacion
import com.example.mpaz_real_real.ui.components.TarjetaCodigo
import com.example.mpaz_real_real.ui.components.TarjetaRespuesta
import com.example.mpaz_real_real.ui.components.TarjetaResumenEstado
import com.example.mpaz_real_real.ui.utils.colorFondoEstado
import com.example.mpaz_real_real.ui.utils.colorNumeroResumen
import com.example.mpaz_real_real.ui.utils.obtenerMedidasAsistente
import com.example.mpaz_real_real.viewmodel.CatalogoViewModel
import com.example.mpaz_real_real.navigation.Screen
import com.example.mpaz_real_real.ui.components.BotonCatalogo
import com.example.mpaz_real_real.ui.components.CampoFormulario
import com.example.mpaz_real_real.ui.components.DialogoAviso
import com.example.mpaz_real_real.ui.components.DialogoError
import com.example.mpaz_real_real.ui.utils.colorTextoEstado

@Composable
fun ResumenAsignacionScreen(
    navController: NavController,
    viewModel: CatalogoViewModel
) {
    val estado by viewModel.state.collectAsState()
    val asistente = estado.asistente
    val asignacion = estado.asignacionRevisada

    val medidas = obtenerMedidasAsistente()

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

        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                EncabezadoAsistente(titulo = "Asignación y resultados")

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = medidas.margen, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TituloDeSeccion("Datos de la asignación")

                    if (asignacion.estado != "finalizada") {
                        TarjetaCodigo(codigo = asignacion.codigoTemporal)
                    }

                    TablaAsignacion(
                        estudiante = asistente.nombreEstudiante(asignacion.estudianteCodigo),
                        actividad = actividad?.nombre ?: asignacion.actividadCodigo,
                        cursoDificultad = cursoDificultad,
                        asistente = asignacion.asistente,
                        fecha = asignacion.fecha,
                        codigoTemporal = asignacion.codigoTemporal,
                        estado = asignacion.estado,
                        detalleActividad = if (asignacion.estado == "en desarrollo") null else actividad
                    )

                    if (asignacion.estado != "pendiente") {
                        val titulo = if (asignacion.estado == "finalizada") {
                            "Respuestas entregadas"
                        } else if (terminada) {
                            "Respuestas del último intento"
                        } else {
                            "Respuestas hasta ahora"
                        }
                        TituloDeSeccion(titulo)
                        if (respuestas.isEmpty()) {
                            Text("No comenzó", style = MaterialTheme.typography.bodyMedium)
                        } else {
                            respuestas.forEach { respuesta ->
                                val indice = respuesta.numeroPregunta - 1
                                if (indice in 0..preguntas.size - 1) {
                                    val pregunta = preguntas[indice]
                                    val alternativas = pregunta.alternativas()
                                    val respondida = respuesta.alternativaElegida in 0..2
                                    val elegida = if (respondida) {
                                        alternativas[respuesta.alternativaElegida]
                                    } else if (terminada) {
                                        "No respondida (no válida)"
                                    } else {
                                        "Aún sin responder"
                                    }
                                    val textoCorrecta = if (pregunta.correcta in 0..2) {
                                        alternativas[pregunta.correcta]
                                    } else {
                                        ""
                                    }

                                    TarjetaRespuesta(
                                        titulo = "$etiqueta ${respuesta.numeroPregunta}",
                                        enunciado = pregunta.enunciado,
                                        elegida = elegida,
                                        intentos = respuesta.cantidadIntentos,
                                        correcta = respuesta.alternativaElegida == pregunta.correcta,
                                        respuestaCorrecta = textoCorrecta,
                                        respondida = respondida
                                    )
                                }
                            }

                            TituloDeSeccion("Resultados")
                            TarjetasDeResultados(
                                aciertos = aciertos,
                                errores = if (terminada) errores + noRespondidas else errores,
                                intentos = actividad?.intentosPermitidos,
                                separacion = medidas.separacion
                            )
                            Text(
                                text = if (terminada) {
                                    "No respondidas (incluidas en errores): $noRespondidas"
                                } else {
                                    "Aún sin responder: $noRespondidas"
                                },
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    TituloDeSeccion("Observación pedagógica")
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
        }

        if (asistente.mensajeExito != null) {
            DialogoAviso(
                titulo = "Listo",
                mensaje = asistente.mensajeExito,
                fondo = colorFondoEstado("finalizada"),
                onCerrar = {
                    viewModel.onCerrarMensajeAsistente()
                    navController.popBackStack(Screen.Asignaciones.route, false)
                },
                etiqueta = "Listo",
                colorEtiqueta = colorTextoEstado("finalizada")
            )
        }
        if (asistente.mensajeError != null) {
            DialogoError("Error", asistente.mensajeError, viewModel::onCerrarErrorAsistente)
        }
    }
}

@Composable
private fun TituloDeSeccion(texto: String) {
    Text(
        text = texto,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun TarjetasDeResultados(
    aciertos: Int?,
    errores: Int?,
    intentos: Int?,
    separacion: Dp
) {
    val colorNormal = MaterialTheme.colorScheme.onSurface

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(separacion)
    ) {
        TarjetaResumenEstado(
            titulo = "ACIERTOS",
            cantidad = aciertos,
            color = if (aciertos == null) colorNormal else colorNumeroResumen("finalizada"),
            modifier = Modifier.weight(1f),
            fondo = if (aciertos == null) null else colorFondoEstado("finalizada")
        )
        TarjetaResumenEstado(
            titulo = "ERRORES",
            cantidad = errores,
            color = if (errores == null) colorNormal else colorNumeroResumen("pendiente"),
            modifier = Modifier.weight(1f),
            fondo = if (errores == null) null else colorFondoEstado("pendiente")
        )
        TarjetaResumenEstado(
            titulo = "INTENTOS",
            cantidad = intentos,
            color = colorNormal,
            modifier = Modifier.weight(1f)
        )
    }
}