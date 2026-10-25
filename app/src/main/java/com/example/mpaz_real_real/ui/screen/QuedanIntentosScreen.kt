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
import com.example.mpaz_real_real.ui.utils.colorBoton
import com.example.mpaz_real_real.ui.utils.colorFondo

import com.example.mpaz_real_real.ui.components.BotonActividad
import com.example.mpaz_real_real.ui.components.ContenedorTexto
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import com.example.mpaz_real_real.ui.utils.obtenerWindowSizeClass
@Composable
fun QuedanIntentosScreen(
    navController: NavController,
    viewModel: ActividadViewModel
) {
    val estado by viewModel.estado.collectAsState()
    val windowSizeClass = obtenerWindowSizeClass()
    val onReintentar: () -> Unit = {
        viewModel.onReintentar()
        navController.navigate(Screen.Pregunta.route) {
            popUpTo(Screen.Instrucciones.route)
        }
    }
    val onSiguiente: () -> Unit = {
        navController.navigate(Screen.Retroalimentacion.route) {
            popUpTo(Screen.Instrucciones.route)
        }
    }
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> QuedanIntentosCompacta(
            estado = estado,
            onReintentar = onReintentar,
            onSiguiente = onSiguiente
        )
        WindowWidthSizeClass.Medium -> QuedanIntentosMediana(
            estado = estado,
            onReintentar = onReintentar,
            onSiguiente = onSiguiente
        )
        WindowWidthSizeClass.Expanded -> QuedanIntentosExpandida(
            estado = estado,
            onReintentar = onReintentar,
            onSiguiente = onSiguiente
        )
    }
}

@Composable
fun QuedanIntentosCompacta(
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
            containerColor = colorFondo(actividad.modulo),
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
                BotonActividad(texto = "Reintentar", onClick = onReintentar, colorFondo = colorBoton(actividad.modulo))
                BotonActividad(texto = "Siguiente", onClick = onSiguiente, colorFondo = colorBoton(actividad.modulo))
            }
        }
    }
}

@Composable
fun QuedanIntentosMediana(
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
            containerColor = colorFondo(actividad.modulo),
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
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        modifier = Modifier.weight(3f)
                    ) {
                        BotonActividad(texto = "Reintentar", onClick = onReintentar, colorFondo = colorBoton(actividad.modulo))
                        BotonActividad(texto = "Siguiente", onClick = onSiguiente, colorFondo = colorBoton(actividad.modulo))
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun QuedanIntentosExpandida(
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
            containerColor = colorFondo(actividad.modulo),
            modifier = modifier.fillMaxSize()
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(32.dp)
            ) {
                ContenedorTexto {
                    Text(
                        text = "Enviaste tus respuestas.",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = if (restantes == 1) "Te queda 1 intento." else "Te quedan $restantes intentos.",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Row {
                    Spacer(modifier = Modifier.weight(2f))
                    Column(
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        modifier = Modifier.weight(5f)
                    ) {
                        BotonActividad(texto = "Reintentar", onClick = onReintentar, colorFondo = colorBoton(actividad.modulo))
                        BotonActividad(texto = "Siguiente", onClick = onSiguiente, colorFondo = colorBoton(actividad.modulo))
                    }
                    Spacer(modifier = Modifier.weight(2f))
                }
            }
        }
    }
}

@Preview(name = "Compact", widthDp = 360, heightDp = 800)
@Composable
fun QuedanIntentosCompactaPreview() {
    Mpaz_real_realTheme {
        QuedanIntentosCompacta(
            estado = ActividadUiState(
                actividad = actividadesDePrueba.first(),
                intentosUsados = 1
            ),
            onReintentar = {},
            onSiguiente = {}
        )
    }
}

@Preview(name = "Medium", widthDp = 700, heightDp = 1000)
@Composable
fun QuedanIntentosMedianaPreview() {
    Mpaz_real_realTheme {
        QuedanIntentosMediana(
            estado = ActividadUiState(
                actividad = actividadesDePrueba.first(),
                intentosUsados = 1
            ),
            onReintentar = {},
            onSiguiente = {}
        )
    }
}

@Preview(name = "Expanded", widthDp = 1000, heightDp = 700)
@Composable
fun QuedanIntentosExpandidaPreview() {
    Mpaz_real_realTheme {
        QuedanIntentosExpandida(
            estado = ActividadUiState(
                actividad = actividadesDePrueba.first(),
                intentosUsados = 1
            ),
            onReintentar = {},
            onSiguiente = {}
        )
    }
}