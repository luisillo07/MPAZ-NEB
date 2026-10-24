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
import com.example.mpaz_real_real.ui.theme.RosaFondo

import com.example.mpaz_real_real.ui.components.BotonActividad

import com.example.mpaz_real_real.ui.components.ContenedorTexto
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.foundation.layout.Column
import com.example.mpaz_real_real.ui.components.AlternativaPregunta
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.border
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.example.mpaz_real_real.ui.theme.RojoCabecera

import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import com.example.mpaz_real_real.ui.theme.Beige

import com.example.mpaz_real_real.ui.components.AvisoError

@Composable
fun PreguntaScreen(
    navController: NavController,
    viewModel: ActividadViewModel
) {
    val estado by viewModel.estado.collectAsState()
    PreguntaContenido(
        estado = estado,
        onAlternativa = viewModel::onAlternativaSeleccionada,
        onSiguiente = {
            if (viewModel.validarRespuesta()) {
                viewModel.onSiguiente()
            }
        },
        onEnviar = {
            viewModel.onEnviar()
            val destino = if(viewModel.estado.value.quedanIntentos){
                Screen.QuedanIntentos.route
            }else{
                Screen.Retroalimentacion.route
            }
            navController.navigate(destino){
                popUpTo(Screen.Pregunta.route){inclusive = true}
            }
        },
        onCambiar = viewModel::onCambiarRespuestas
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
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Pregunta ${estado.indicePregunta + 1} de ${actividad.preguntas.size}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        LinearProgressIndicator(
                            progress = { (estado.indicePregunta + 1).toFloat() / actividad.preguntas.size },
                            color = RojoCabecera,
                            trackColor = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .clip(MaterialTheme.shapes.medium)
                                .border(2.dp, Color.Black, MaterialTheme.shapes.medium)
                        )
                    }
                }
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
                item {ContenedorTexto{Text(text = pregunta.enunciado)}}
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        pregunta.alternativas.forEachIndexed { posicion, texto ->
                            AlternativaPregunta(
                                letra = listOf("A", "B", "C", "D")[posicion], // hasta 4 alternativas
                                texto = texto,
                                seleccionada = estado.respuestas[estado.indicePregunta] == posicion,
                                onClick = { onAlternativa(posicion) }
                            )
                        }
                    }
                }
                estado.errorRespuesta?.let { mensaje ->
                    item {AvisoError(mensaje = mensaje)}
                }
                item {
                    BotonActividad(texto = if (estado.esUltimaPregunta) "Enviar" else "Siguiente", onClick = onSiguiente)
                }
            }
        }
        if (estado.mostrarConfirmacion){
            val total = actividad.preguntas.size
            Dialog(onDismissRequest = onCambiar){
                ContenedorTexto{
                    Text(
                        text = "¿Enviar tus respuestas?",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${estado.respondidas} de $total preguntas respondidas",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "${total - estado.respondidas} de $total sin responder",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        BotonActividad(texto = "Enviar", onClick = onEnviar,colorFondo = Beige,modifier = Modifier.weight(1f))
                        BotonActividad(texto = "Cambiar", onClick = onCambiar,colorFondo = Beige,modifier = Modifier.weight(1f))
                    }
                }
            }
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
