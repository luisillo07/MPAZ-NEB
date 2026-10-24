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

import androidx.compose.material3.Button

import androidx.compose.material3.OutlinedButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.rememberLauncherForActivityResult

import com.example.mpaz_real_real.ui.components.PantallazoPregunta

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

            if (estado.esUltimaRetroalimentacion){
                if (estado.pantallazoPregunta == null){
                    OutlinedButton(onClick = onAdjuntarPantallazo){
                        Text(text = "Adjuntar pantallazo de la pregunta que más te costó")
                    }
                }else{
                    Text(text = "¡Ya subiste la imagen!")
                }
                PantallazoPregunta(uri = estado.pantallazoPregunta)
                if (estado.resumenGuardado){
                    Text(text = "¡Ya guardaste el resumen!")
                }else{
                    OutlinedButton(onClick = onGuardarResumen){
                        Text(text = "Guardar resumen del intento")
                    }
                }
            }
            if (!estado.esUltimaRetroalimentacion) {
                Button(onClick = onSiguiente) {
                    Text(text = "Siguiente")
                }
            } else {
                Button(onClick = onVolver) {
                    Text(text = "Volver a Actividades")
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
