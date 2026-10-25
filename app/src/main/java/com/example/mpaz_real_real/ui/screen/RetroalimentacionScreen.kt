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

import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.rememberLauncherForActivityResult

import com.example.mpaz_real_real.ui.components.PantallazoPregunta

import androidx.compose.material3.Scaffold
import com.example.mpaz_real_real.ui.components.EncabezadoActividad
import com.example.mpaz_real_real.ui.utils.colorBoton
import com.example.mpaz_real_real.ui.utils.colorFondo

import com.example.mpaz_real_real.ui.components.BotonActividad

import com.example.mpaz_real_real.ui.components.ContenedorTexto
import androidx.compose.ui.text.font.FontWeight
import com.example.mpaz_real_real.ui.theme.Beige

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import com.example.mpaz_real_real.ui.utils.obtenerWindowSizeClass
import androidx.compose.animation.AnimatedVisibility

import com.example.mpaz_real_real.data.local.actividadesIniciales
import com.example.mpaz_real_real.data.local.preguntasIniciales

@Composable
fun RetroalimentacionScreen(
    navController: NavController,
    viewModel: ActividadViewModel
) {
    val estado by viewModel.estado.collectAsState()
    val windowSizeClass = obtenerWindowSizeClass()
    val lanzadorGaleria = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ){uri -> viewModel.onPantallazoSeleccionado(uri)}
    val lanzadorArchivo = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/plain")
    ) { uri -> viewModel.guardarResumen(uri) }
    val onSiguiente: () -> Unit = viewModel::onSiguienteRetroalimentacion
    val onVolver: () -> Unit = {
        navController.navigate(Screen.MisActividades.route) {
            popUpTo(Screen.MisActividades.route) { inclusive = true }
        }
    }
    val onAdjuntarPantallazo: () -> Unit = {lanzadorGaleria.launch("image/*")}
    val onGuardarResumen: () -> Unit = {lanzadorArchivo.launch("resumen_${estado.actividad?.codigo}.txt")}
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> RetroalimentacionCompacta(
            estado = estado,
            onSiguiente = onSiguiente,
            onVolver = onVolver,
            onAdjuntarPantallazo = onAdjuntarPantallazo,
            onGuardarResumen = onGuardarResumen
        )
        WindowWidthSizeClass.Medium -> RetroalimentacionMediana(
            estado = estado,
            onSiguiente = onSiguiente,
            onVolver = onVolver,
            onAdjuntarPantallazo = onAdjuntarPantallazo,
            onGuardarResumen = onGuardarResumen
        )
        WindowWidthSizeClass.Expanded -> RetroalimentacionExpandida(
            estado = estado,
            onSiguiente = onSiguiente,
            onVolver = onVolver,
            onAdjuntarPantallazo = onAdjuntarPantallazo,
            onGuardarResumen = onGuardarResumen
        )
    }
}

@Composable
fun RetroalimentacionCompacta(
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
        val pregunta = estado.preguntas[estado.indiceRetroalimentacion]
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
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                item{
                    Text(
                        text = "Retroalimentación ${estado.indiceRetroalimentacion + 1} de ${estado.preguntas.size}",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                item{
                    ContenedorTexto{
                        Text(text = pregunta.enunciado,fontWeight = FontWeight.Bold)
                        Text(text = estado.mensajeRetroalimentacion)
                        if (pregunta.procedimiento.isNotBlank()) {
                            Text(
                                text = "Procedimiento: ${pregunta.procedimiento}",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                if (estado.esUltimaRetroalimentacion){
                    item{
                        Column(verticalArrangement = Arrangement.spacedBy(20.dp)){
                            if (estado.pantallazoPregunta == null){
                                Text(text = "Adjunta un pantallazo de la pregunta que más te costó")
                                BotonActividad(
                                    texto = "Adjuntar pantallazo",
                                    onClick = onAdjuntarPantallazo,
                                    colorFondo = Beige
                                )
                            }
                            AnimatedVisibility(visible = estado.pantallazoPregunta != null) {
                                Text(text = "¡Ya subiste la imagen!")
                            }
                            PantallazoPregunta(uri = estado.pantallazoPregunta)
                            AnimatedVisibility(visible = estado.resumenGuardado) {
                                Text(text = "¡Ya guardaste el resumen!")
                            }
                            if (!estado.resumenGuardado){
                                BotonActividad(
                                    texto = "Guardar resumen del intento",
                                    onClick = onGuardarResumen,
                                    colorFondo = Beige
                                )
                            }
                        }
                    }
                }
                item{
                    if (!estado.esUltimaRetroalimentacion){
                        BotonActividad(texto = "Siguiente", onClick = onSiguiente, colorFondo = colorBoton(actividad.modulo))
                    }else{
                        BotonActividad(texto = "Volver a Actividades", onClick = onVolver, colorFondo = colorBoton(actividad.modulo))
                    }
                }
            }
        }
    }
}

@Composable
fun RetroalimentacionMediana(
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
        val pregunta = estado.preguntas[estado.indiceRetroalimentacion]
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
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                item{
                    Text(
                        text = "Retroalimentación ${estado.indiceRetroalimentacion + 1} de ${estado.preguntas.size}",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                item{
                    ContenedorTexto{
                        Text(text = pregunta.enunciado,fontWeight = FontWeight.Bold)
                        Text(text = estado.mensajeRetroalimentacion)
                        if (pregunta.procedimiento.isNotBlank()) {
                            Text(
                                text = "Procedimiento: ${pregunta.procedimiento}",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                item{
                    Row {
                        Spacer(modifier = Modifier.weight(1f))
                        Column(
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                            modifier = Modifier.weight(3f)
                        ){
                            if (estado.esUltimaRetroalimentacion){
                                if (estado.pantallazoPregunta == null){
                                    Text(text = "Adjunta un pantallazo de la pregunta que más te costó")
                                    BotonActividad(
                                        texto = "Adjuntar pantallazo",
                                        onClick = onAdjuntarPantallazo,
                                        colorFondo = Beige
                                    )
                                }
                                AnimatedVisibility(visible = estado.pantallazoPregunta != null) {
                                    Text(text = "¡Ya subiste la imagen!")
                                }
                                PantallazoPregunta(uri = estado.pantallazoPregunta)
                                AnimatedVisibility(visible = estado.resumenGuardado) {
                                    Text(text = "¡Ya guardaste el resumen!")
                                }
                                if (!estado.resumenGuardado){
                                    BotonActividad(
                                        texto = "Guardar resumen del intento",
                                        onClick = onGuardarResumen,
                                        colorFondo = Beige
                                    )
                                }
                            }
                            if (!estado.esUltimaRetroalimentacion){
                                BotonActividad(texto = "Siguiente", onClick = onSiguiente, colorFondo = colorBoton(actividad.modulo))
                            }else{
                                BotonActividad(texto = "Volver a Actividades", onClick = onVolver, colorFondo = colorBoton(actividad.modulo))
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun RetroalimentacionExpandida(
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
        val pregunta = estado.preguntas[estado.indiceRetroalimentacion]
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
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(32.dp)
            ) {
                item{
                    Text(
                        text = "Retroalimentación ${estado.indiceRetroalimentacion + 1} de ${estado.preguntas.size}",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                item{
                    ContenedorTexto{
                        Text(
                            text = pregunta.enunciado,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = estado.mensajeRetroalimentacion,
                            style = MaterialTheme.typography.titleMedium
                        )
                        if (pregunta.procedimiento.isNotBlank()) {
                            Text(
                                text = "Procedimiento: ${pregunta.procedimiento}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                item{
                    Row {
                        Spacer(modifier = Modifier.weight(2f))
                        Column(
                            verticalArrangement = Arrangement.spacedBy(24.dp),
                            modifier = Modifier.weight(5f)
                        ){
                            if (estado.esUltimaRetroalimentacion){
                                if (estado.pantallazoPregunta == null){
                                    Text(text = "Adjunta un pantallazo de la pregunta que más te costó")
                                    BotonActividad(
                                        texto = "Adjuntar pantallazo",
                                        onClick = onAdjuntarPantallazo,
                                        colorFondo = Beige
                                    )
                                }
                                AnimatedVisibility(visible = estado.pantallazoPregunta != null) {
                                    Text(text = "¡Ya subiste la imagen!")
                                }
                                PantallazoPregunta(uri = estado.pantallazoPregunta)
                                AnimatedVisibility(visible = estado.resumenGuardado) {
                                    Text(text = "¡Ya guardaste el resumen!")
                                }
                                if (!estado.resumenGuardado){
                                    BotonActividad(
                                        texto = "Guardar resumen del intento",
                                        onClick = onGuardarResumen,
                                        colorFondo = Beige
                                    )
                                }
                            }
                            if (!estado.esUltimaRetroalimentacion){
                                BotonActividad(texto = "Siguiente", onClick = onSiguiente, colorFondo = colorBoton(actividad.modulo))
                            }else{
                                BotonActividad(texto = "Volver a Actividades", onClick = onVolver, colorFondo = colorBoton(actividad.modulo))
                            }
                        }
                        Spacer(modifier = Modifier.weight(2f))
                    }
                }
            }
        }
    }
}

@Preview(name = "Compact", widthDp = 360, heightDp = 800)
@Composable
fun RetroalimentacionCompactaPreview() {
    Mpaz_real_realTheme {
        RetroalimentacionCompacta(
            estado = ActividadUiState(
                actividad = actividadesIniciales.first(),
                preguntas = preguntasIniciales.filter { it.actividadCodigo == "LEC-001" },
                mensajeRetroalimentacion = "¡Muy bien! El texto dice: «Tito es un perro café»."
            ),
            onSiguiente = {},
            onVolver = {},
            onAdjuntarPantallazo = {},
            onGuardarResumen = {}
        )
    }
}

@Preview(name = "Medium", widthDp = 700, heightDp = 1000)
@Composable
fun RetroalimentacionMedianaPreview() {
    Mpaz_real_realTheme {
        RetroalimentacionMediana(
            estado = ActividadUiState(
                actividad = actividadesIniciales.first(),
                preguntas = preguntasIniciales.filter { it.actividadCodigo == "LEC-001" },
                mensajeRetroalimentacion = "¡Muy bien! El texto dice: «Tito es un perro café»."
            ),
            onSiguiente = {},
            onVolver = {},
            onAdjuntarPantallazo = {},
            onGuardarResumen = {}
        )
    }
}

@Preview(name = "Expanded", widthDp = 1000, heightDp = 700)
@Composable
fun RetroalimentacionExpandidaPreview() {
    val actividad = actividadesIniciales.first()
    val preguntas = preguntasIniciales.filter { it.actividadCodigo == actividad.codigo }
    Mpaz_real_realTheme {
        RetroalimentacionExpandida(
            estado = ActividadUiState(
                actividad = actividad,
                preguntas = preguntas,
                respuestas = preguntas.map { null },
                indiceRetroalimentacion = preguntas.size - 1,
                mensajeRetroalimentacion = "¡Muy bien! Un patio es un espacio abierto dentro de una casa."
            ),
            onSiguiente = {},
            onVolver = {},
            onAdjuntarPantallazo = {},
            onGuardarResumen = {}
        )
    }
}