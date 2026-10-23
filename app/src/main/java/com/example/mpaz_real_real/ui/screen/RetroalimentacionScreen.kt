package com.example.mpaz_real_real.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mpaz_real_real.viewmodel.ActividadViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.mpaz_real_real.model.ActividadUiState
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme

import androidx.compose.ui.tooling.preview.Preview
import com.example.mpaz_real_real.ui.theme.Mpaz_real_realTheme
import com.example.mpaz_real_real.repository.actividadesDePrueba

@Composable
fun RetroalimentacionScreen(
    modifier: Modifier = Modifier,
    viewModel: ActividadViewModel = viewModel()
) {
    val estado by viewModel.estado.collectAsState()
    RetroalimentacionContenido(estado = estado, modifier = modifier)
}

@Composable
fun RetroalimentacionContenido(
    estado: ActividadUiState,
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
        val pregunta = actividad.preguntas[estado.indiceRetroalimentacion]

        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Retroalimentación ${estado.indiceRetroalimentacion + 1} de ${actividad.preguntas.size}",
                style = MaterialTheme.typography.titleLarge
            )
            Text(text = pregunta.enunciado)
            Text(text = estado.mensajeRetroalimentacion)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RetroalimentacionContenidoPreview() {
    Mpaz_real_realTheme {
        RetroalimentacionContenido(
            estado = ActividadUiState(
                actividad = actividadesDePrueba.first(),
                mensajeRetroalimentacion = "¡Muy bien! El texto dice: «Tito es un perro café»."
            )
        )
    }
}
