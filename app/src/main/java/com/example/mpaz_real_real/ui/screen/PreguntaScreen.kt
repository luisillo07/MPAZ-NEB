package com.example.mpaz_real_real.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mpaz_real_real.viewmodel.ActividadViewModel
import androidx.navigation.NavController
import com.example.mpaz_real_real.navigation.Screen
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

import com.example.mpaz_real_real.repository.actividadesDePrueba
import com.example.mpaz_real_real.ui.theme.Mpaz_real_realTheme

import androidx.compose.material3.Scaffold
import com.example.mpaz_real_real.ui.components.EncabezadoActividad
import com.example.mpaz_real_real.ui.utils.colorBoton
import com.example.mpaz_real_real.ui.utils.colorFondo
import com.example.mpaz_real_real.ui.utils.colorCabecera

import com.example.mpaz_real_real.ui.components.BotonActividad

import com.example.mpaz_real_real.ui.components.ContenedorTexto
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.foundation.layout.Column
import com.example.mpaz_real_real.ui.components.AlternativaPregunta

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import com.example.mpaz_real_real.ui.components.AvisoError
import com.example.mpaz_real_real.ui.components.IndicadorAvance
import com.example.mpaz_real_real.ui.components.VentanaConfirmacion
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import com.example.mpaz_real_real.ui.utils.obtenerWindowSizeClass

@Composable
fun PreguntaScreen(
    navController: NavController,
    viewModel: ActividadViewModel
) {
    val estado by viewModel.estado.collectAsState()
    val windowSizeClass = obtenerWindowSizeClass()
    val onAlternativa: (Int) -> Unit = viewModel::onAlternativaSeleccionada
    val onSiguiente: () -> Unit = {
        if (viewModel.validarRespuesta()) {
            viewModel.onSiguiente()
        }
    }
    val onEnviar: () -> Unit = {
        viewModel.onEnviar()
        val destino = if(viewModel.estado.value.quedanIntentos){
            Screen.QuedanIntentos.route
        }else{
            Screen.Retroalimentacion.route
        }
        navController.navigate(destino){
            popUpTo(Screen.Pregunta.route){inclusive = true}
        }
    }
    val onCambiar: () -> Unit = viewModel::onCambiarRespuestas
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> PreguntaCompacta(
            estado = estado,
            onAlternativa = onAlternativa,
            onSiguiente = onSiguiente,
            onEnviar = onEnviar,
            onCambiar = onCambiar
        )
        WindowWidthSizeClass.Medium -> PreguntaMediana(
            estado = estado,
            onAlternativa = onAlternativa,
            onSiguiente = onSiguiente,
            onEnviar = onEnviar,
            onCambiar = onCambiar
        )
        WindowWidthSizeClass.Expanded -> PreguntaExpandida(
            estado = estado,
            onAlternativa = onAlternativa,
            onSiguiente = onSiguiente,
            onEnviar = onEnviar,
            onCambiar = onCambiar
        )
    }
}

@Composable
fun PreguntaCompacta(
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
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                item {
                    IndicadorAvance(
                        actual = estado.indicePregunta + 1,
                        total = actividad.preguntas.size,
                        etiqueta = if (actividad.modulo == "Matemática") "Ejercicio" else "Pregunta",
                        colorBarra = colorCabecera(actividad.modulo)
                    )
                }
                if (actividad.contenidoTexto.isNotBlank()) {
                    item {
                        ContenedorTexto {
                            Text(
                                text = actividad.tituloTexto,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(text = actividad.contenidoTexto)
                        }
                    }
                }
                item {ContenedorTexto{Text(text = pregunta.enunciado)}}
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        pregunta.alternativas.forEachIndexed { posicion, texto ->
                            AlternativaPregunta(
                                letra = listOf("A", "B", "C", "D")[posicion], // hasta 4 alternativas
                                texto = texto,
                                seleccionada = estado.respuestas[estado.indicePregunta] == posicion,
                                onClick = { onAlternativa(posicion) },
                                colorMarcada = colorBoton(actividad.modulo)
                            )
                        }
                    }
                }
                estado.errorRespuesta?.let { mensaje ->
                    item {AvisoError(mensaje = mensaje)}
                }
                item {
                    BotonActividad(texto = if (estado.esUltimaPregunta) "Enviar" else "Siguiente", onClick = onSiguiente, colorFondo = colorBoton(actividad.modulo))
                }
            }
        }
        if (estado.mostrarConfirmacion){
            VentanaConfirmacion(
                respondidas = estado.respondidas,
                total = actividad.preguntas.size,
                onEnviar = onEnviar,
                onCambiar = onCambiar
            )
        }
    }
}

@Composable
fun PreguntaMediana(
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
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                item {
                    IndicadorAvance(
                        actual = estado.indicePregunta + 1,
                        total = actividad.preguntas.size,
                        etiqueta = if (actividad.modulo == "Matemática") "Ejercicio" else "Pregunta",
                        colorBarra = colorCabecera(actividad.modulo)
                    )
                }
                if (actividad.contenidoTexto.isNotBlank()){
                    item{
                        ContenedorTexto{
                            Text(
                                text = actividad.tituloTexto,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(text = actividad.contenidoTexto)
                        }
                    }
                }
                item {ContenedorTexto{Text(text = pregunta.enunciado)}}
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        pregunta.alternativas.forEachIndexed { posicion, texto ->
                            AlternativaPregunta(
                                letra = listOf("A", "B", "C", "D")[posicion], // hasta 4 alternativas
                                texto = texto,
                                seleccionada = estado.respuestas[estado.indicePregunta] == posicion,
                                onClick = { onAlternativa(posicion) },
                                colorMarcada = colorBoton(actividad.modulo)
                            )
                        }
                    }
                }
                estado.errorRespuesta?.let { mensaje ->
                    item {AvisoError(mensaje = mensaje)}
                }
                item {
                    Row {
                        Spacer(modifier = Modifier.weight(1f))
                        BotonActividad(
                            texto = if (estado.esUltimaPregunta) "Enviar" else "Siguiente",
                            onClick = onSiguiente,
                            colorFondo = colorBoton(actividad.modulo),
                            modifier = Modifier.weight(3f)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
        if (estado.mostrarConfirmacion){
            VentanaConfirmacion(
                respondidas = estado.respondidas,
                total = actividad.preguntas.size,
                onEnviar = onEnviar,
                onCambiar = onCambiar
            )
        }
    }
}

@Composable
fun PreguntaExpandida(
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
                IndicadorAvance(
                    actual = estado.indicePregunta + 1,
                    total = actividad.preguntas.size,
                    etiqueta = if (actividad.modulo == "Matemática") "Ejercicio" else "Pregunta",
                    colorBarra = colorCabecera(actividad.modulo)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        item {
                            ContenedorTexto {
                                Text(
                                    text = actividad.tituloTexto,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    text = actividad.contenidoTexto,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        item {
                            ContenedorTexto {
                                Text(
                                    text = pregunta.enunciado,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                        item {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                pregunta.alternativas.forEachIndexed { posicion, texto ->
                                    AlternativaPregunta(
                                        letra = listOf("A", "B", "C", "D")[posicion], // hasta 4 alternativas
                                        texto = texto,
                                        seleccionada = estado.respuestas[estado.indicePregunta] == posicion,
                                        onClick = { onAlternativa(posicion) },
                                        colorMarcada = colorBoton(actividad.modulo)
                                    )
                                }
                            }
                        }
                        estado.errorRespuesta?.let { mensaje ->
                            item {AvisoError(mensaje = mensaje)}
                        }
                        item {
                            BotonActividad(texto = if (estado.esUltimaPregunta) "Enviar" else "Siguiente", onClick = onSiguiente, colorFondo = colorBoton(actividad.modulo))
                        }
                    }
                }
            }
        }
        if (estado.mostrarConfirmacion){
            VentanaConfirmacion(
                respondidas = estado.respondidas,
                total = actividad.preguntas.size,
                onEnviar = onEnviar,
                onCambiar = onCambiar
            )
        }
    }
}

@Preview(name = "Compact", widthDp = 360, heightDp = 800)
@Composable
fun PreguntaCompactaPreview() {
    val actividad = actividadesDePrueba.first()
    Mpaz_real_realTheme {
        PreguntaCompacta(
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

@Preview(name = "Medium", widthDp = 700, heightDp = 1000)
@Composable
fun PreguntaMedianaPreview() {
    val actividad = actividadesDePrueba.first()
    Mpaz_real_realTheme {
        PreguntaMediana(
            estado = ActividadUiState(
                actividad = actividad,
                respuestas = actividad.preguntas.map {null}
            ),
            onAlternativa = {},
            onSiguiente = {},
            onEnviar = {},
            onCambiar = {}
        )
    }
}

@Preview(name = "Expanded", widthDp = 1000, heightDp = 700)
@Composable
fun PreguntaExpandidaPreview() {
    val actividad = actividadesDePrueba.first()
    Mpaz_real_realTheme {
        PreguntaExpandida(
            estado = ActividadUiState(
                actividad = actividad,
                respuestas = actividad.preguntas.map {null}
            ),
            onAlternativa = {},
            onSiguiente = {},
            onEnviar = {},
            onCambiar = {}
        )
    }
}