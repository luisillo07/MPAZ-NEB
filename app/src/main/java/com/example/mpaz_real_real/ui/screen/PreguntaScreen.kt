package com.example.mpaz_real_real.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mpaz_real_real.viewmodel.ActividadViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.mpaz_real_real.model.ActividadUiState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Button

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton

import com.example.mpaz_real_real.repository.actividadesDePrueba
import com.example.mpaz_real_real.ui.theme.Mpaz_real_realTheme

@Composable
fun PreguntaScreen(
    modifier: Modifier = Modifier,
    viewModel: ActividadViewModel = viewModel()
) {
    val estado by viewModel.estado.collectAsState()
    PreguntaContenido(
        estado = estado,
        onAlternativa = viewModel::onAlternativaSeleccionada,
        onSiguiente = {
            if (viewModel.validarRespuesta()){
                viewModel.onSiguiente()
            }
        },
        onEnviar = viewModel::onEnviar,
        onCambiar = viewModel::onCambiarRespuestas,
        modifier = modifier
    )
}

@Composable
fun PreguntaContenido(
    estado: ActividadUiState,
    onAlternativa: (Int) -> Unit,
    onSiguiente: () -> Unit,
    onEnviar: () -> Unit,
    onCambiar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val actividad = estado.actividad
    if (actividad == null) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    } else {
        val pregunta = actividad.preguntas[estado.indicePregunta]

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item { Text(text = actividad.tituloTexto, style = MaterialTheme.typography.titleLarge) }
            item { Text(text = actividad.contenidoTexto) }
            item {
                Text(
                    text = "Pregunta ${estado.indicePregunta + 1} de ${actividad.preguntas.size}",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            item { Text(text = pregunta.enunciado) }
            item {
                pregunta.alternativas.forEachIndexed { posicion, texto ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = estado.respuestas[estado.indicePregunta] == posicion,
                            onClick = { onAlternativa(posicion) }
                        )
                        Text(text = texto)
                    }
                }
            }
            estado.errorRespuesta?.let { mensaje ->
                item { Text(text = mensaje, color = MaterialTheme.colorScheme.error) }
            }
            item {
                Button(onClick = onSiguiente) {
                    Text(text = if (estado.esUltimaPregunta) "Enviar" else "Siguiente")
                }
            }
        }

        if (estado.mostrarConfirmacion) {
            AlertDialog(
                onDismissRequest = onCambiar,
                title = {Text(text = "¿Enviar tus respuestas?")},
                text = {Text(text = "Si quieres revisarlas antes, toca Cambiar.")},
                confirmButton = {
                    TextButton(onClick = onEnviar){Text(text = "Enviar")}
                },
                dismissButton = {
                    TextButton(onClick = onCambiar){Text(text = "Cambiar")}
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreguntaContenidoPreview() {
    val actividad = actividadesDePrueba.first()
    Mpaz_real_realTheme {
        PreguntaContenido(
            estado = ActividadUiState(
                actividad = actividad,
                respuestas = actividad.preguntas.map {null},
                errorRespuesta = "Debes elegir una alternativa para continuar."
            ),
            onAlternativa = {},
            onSiguiente = {},
            onEnviar = {},
            onCambiar = {}
        )
    }
}



