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

        var aciertos = 0
        var errores = 0
        respuestas.forEach { respuesta ->
            val indice = respuesta.numeroPregunta - 1
            if (indice in 0..preguntas.size - 1) {
                if (respuesta.alternativaElegida == preguntas[indice].correcta) {
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

                if (asignacion.estado == "finalizada") {
                    Text("Respuestas entregadas")

                    respuestas.forEach { respuesta ->
                        val indice = respuesta.numeroPregunta - 1
                        if (indice in 0..preguntas.size - 1) {
                            val pregunta = preguntas[indice]
                            val alternativas = pregunta.alternativas()
                            val elegida = if (respuesta.alternativaElegida in 0..2) {
                                alternativas[respuesta.alternativaElegida]
                            } else {
                                ""
                            }

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                border = BorderStroke(2.dp, Color.Black)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("$etiqueta ${respuesta.numeroPregunta}", style = MaterialTheme.typography.titleMedium)
                                    Text(pregunta.enunciado)
                                    Text(elegida)
                                    Text("Intentos: ${respuesta.intentos}")
                                }
                            }
                        }
                    }

                    CajaSoloLectura("Cantidad de aciertos", aciertos.toString())
                    CajaSoloLectura("Cantidad de errores", errores.toString())
                    CajaSoloLectura("Cantidad de intentos", actividad?.intentosPermitidos?.toString() ?: "")
                }
            }
        }
    }
}