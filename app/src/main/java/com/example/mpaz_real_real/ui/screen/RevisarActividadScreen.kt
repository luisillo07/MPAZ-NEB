package com.example.mpaz_real_real.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mpaz_real_real.model.alternativas
import com.example.mpaz_real_real.ui.components.AlternativaSoloLectura
import com.example.mpaz_real_real.ui.components.CajaSoloLectura
import com.example.mpaz_real_real.ui.theme.GrisBarra
import com.example.mpaz_real_real.viewmodel.CatalogoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RevisarActividadScreen(
    navController: NavController,
    viewModel: CatalogoViewModel
) {
    val estado by viewModel.state.collectAsState()
    val actividad = estado.actividadRevisada
    val preguntas = estado.preguntasRevisadas
    val letras = listOf("A", "B", "C")

    if (actividad != null) {
        val esMatematica = actividad.modulo == "Matemática"

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text("${actividad.modulo} · ${actividad.curso}", style = MaterialTheme.typography.bodySmall)
                            Text(actividad.nombre, style = MaterialTheme.typography.titleLarge)
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
                if (esMatematica) {
                    CajaSoloLectura("Contenido", actividad.contenido)
                } else {
                    CajaSoloLectura("Título", actividad.tituloTexto)
                    CajaSoloLectura("Tipo de texto", actividad.tipoTexto)
                    CajaSoloLectura("Contenido del texto", actividad.contenidoTexto)
                    CajaSoloLectura("Nivel de vocabulario", actividad.nivelVocabulario)
                }

                if (preguntas.isNotEmpty()) {
                    for (i in 0..preguntas.size - 1) {
                        val pregunta = preguntas[i]
                        val alternativas = pregunta.alternativas()

                        Text(
                            text = if (esMatematica) "Ejercicio ${i + 1}" else "Pregunta ${i + 1}",
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (esMatematica) {
                            if (pregunta.tipo.isNotBlank()) CajaSoloLectura("Tipo de ejercicio", pregunta.tipo)
                            CajaSoloLectura("Enunciado", pregunta.enunciado)
                            if (pregunta.datosNumericos.isNotBlank()) CajaSoloLectura("Datos numéricos", pregunta.datosNumericos)
                            if (pregunta.operacion.isNotBlank()) CajaSoloLectura("Operación", pregunta.operacion)
                        } else {
                            CajaSoloLectura("Pregunta", pregunta.enunciado)
                        }

                        Text("Alternativas")
                        for (j in 0..2) {
                            AlternativaSoloLectura(letras[j], alternativas[j], destacada = false)
                        }

                        if (pregunta.correcta in 0..2) {
                            Text("Respuesta correcta")
                            AlternativaSoloLectura(letras[pregunta.correcta], alternativas[pregunta.correcta], destacada = true)
                        }

                        if (esMatematica && pregunta.procedimiento.isNotBlank()) {
                            CajaSoloLectura("Procedimiento", pregunta.procedimiento)
                        }
                    }
                } else {
                    Text("Esta actividad aún no tiene preguntas", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}