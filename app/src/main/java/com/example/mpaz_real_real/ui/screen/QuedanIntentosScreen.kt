package com.example.mpaz_real_real.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mpaz_real_real.model.ActividadUiState
import com.example.mpaz_real_real.navigation.Screen
import com.example.mpaz_real_real.repository.actividadesDePrueba
import com.example.mpaz_real_real.ui.theme.Mpaz_real_realTheme
import com.example.mpaz_real_real.viewmodel.ActividadViewModel

import androidx.compose.material3.Scaffold
import com.example.mpaz_real_real.ui.components.EncabezadoActividad
import com.example.mpaz_real_real.ui.theme.RosaFondo

import com.example.mpaz_real_real.ui.components.BotonActividad
import com.example.mpaz_real_real.ui.components.ContenedorTexto
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
@Composable
fun QuedanIntentosScreen(
    navController: NavController,
    viewModel: ActividadViewModel
) {
    val estado by viewModel.estado.collectAsState()
    QuedanIntentosContenido(
        estado = estado,
        onReintentar = {
            viewModel.onReintentar()
            navController.navigate(Screen.Pregunta.route) {
                popUpTo(Screen.Instrucciones.route)
            }
        },
        onSiguiente = {
            navController.navigate(Screen.Retroalimentacion.route) {
                popUpTo(Screen.Instrucciones.route)
            }
        }
    )
}

@Composable
fun QuedanIntentosContenido(
    estado: ActividadUiState,
    onReintentar: () -> Unit,
    onSiguiente: () -> Unit,
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
        val restantes = actividad.intentosPermitidos - estado.intentosUsados
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
                ContenedorTexto {
                    Text(
                        text = "Enviaste tus respuestas.",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = if (restantes == 1) "Te queda 1 intento." else "Te quedan $restantes intentos.",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                BotonActividad(texto = "Reintentar", onClick = onReintentar)
                BotonActividad(texto = "Siguiente", onClick = onSiguiente)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuedanIntentosContenidoPreview() {
    Mpaz_real_realTheme {
        QuedanIntentosContenido(
            estado = ActividadUiState(
                actividad = actividadesDePrueba.first(),
                intentosUsados = 1
            ),
            onReintentar = {},
            onSiguiente = {}
        )
    }
}