package com.example.mpaz_real_real.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mpaz_real_real.viewmodel.ActividadViewModel
import androidx.navigation.NavController
import com.example.mpaz_real_real.navigation.Screen
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

import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.rememberLauncherForActivityResult

import com.example.mpaz_real_real.ui.components.PantallazoPregunta

import androidx.compose.material3.Scaffold
import com.example.mpaz_real_real.ui.components.EncabezadoActividad
import com.example.mpaz_real_real.ui.theme.RosaFondo

import com.example.mpaz_real_real.ui.components.BotonActividad

import com.example.mpaz_real_real.ui.components.ContenedorTexto
import androidx.compose.ui.text.font.FontWeight
import com.example.mpaz_real_real.ui.theme.Beige

@Composable
fun RetroalimentacionScreen(
    navController: NavController,
    viewModel: ActividadViewModel
) {
    val estado by viewModel.estado.collectAsState()
    val lanzadorGaleria = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ){uri -> viewModel.onPantallazoSeleccionado(uri)}
    val lanzadorArchivo = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/plain")
    ) { uri -> viewModel.guardarResumen(uri) }
    RetroalimentacionContenido(
        estado = estado,
        onSiguiente = viewModel::onSiguienteRetroalimentacion,
        onVolver = {
            navController.navigate(Screen.Instrucciones.route) {
                popUpTo(Screen.Instrucciones.route) { inclusive = true }
            }
        },
        onAdjuntarPantallazo = {lanzadorGaleria.launch("image/*")},
        onGuardarResumen = {lanzadorArchivo.launch("resumen_${estado.actividad?.codigo}.txt")}
    )
}

@Composable
fun RetroalimentacionContenido(
    estado: ActividadUiState,
    onSiguiente: () -> Unit,
    onVolver: () -> Unit,
    onAdjuntarPantallazo: () -> Unit,
    onGuardarResumen: () -> Unit,
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
        Scaffold(
            topBar = {
                EncabezadoActividad(
                    modulo = actividad.modulo,
                    curso = actividad.curso,
                    nombre = actividad.nombre
                )
            },
            containerColor = RosaFondo,
            modifier = modifier.fillMaxSize()
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Retroalimentación ${estado.indiceRetroalimentacion + 1} de ${actividad.preguntas.size}",
                    style = MaterialTheme.typography.titleLarge
                )
                ContenedorTexto {
                    Text(text = pregunta.enunciado, fontWeight = FontWeight.Bold)
                    Text(text = estado.mensajeRetroalimentacion)
                }

                if (estado.esUltimaRetroalimentacion) {
                    if (estado.pantallazoPregunta == null) {
                        Text(text = "Adjunta un pantallazo de la pregunta que más te costó")
                        BotonActividad(texto = "Adjuntar pantallazo", onClick = onAdjuntarPantallazo, colorFondo = Beige)
                    } else {
                        Text(text = "¡Ya subiste la imagen!")
                    }
                    PantallazoPregunta(uri = estado.pantallazoPregunta)
                    if (estado.resumenGuardado) {
                        Text(text = "¡Ya guardaste el resumen!")
                    } else {
                        BotonActividad(texto = "Guardar resumen del intento", onClick = onGuardarResumen, colorFondo = Beige)
                    }
                }
                if (!estado.esUltimaRetroalimentacion) {
                    BotonActividad(texto = "Siguiente", onClick = onSiguiente)
                } else {
                    BotonActividad(texto = "Volver a Actividades", onClick = onVolver)
                }
            }
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
            ),
            onSiguiente = {},
            onVolver = {},
            onAdjuntarPantallazo = {},
            onGuardarResumen = {}
        )
    }
}