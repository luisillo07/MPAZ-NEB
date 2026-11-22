package com.example.mpaz_real_real.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mpaz_real_real.model.alternativas
import com.example.mpaz_real_real.ui.components.EncabezadoAsistente
import com.example.mpaz_real_real.ui.components.TablaAsignacion
import com.example.mpaz_real_real.ui.components.TarjetaCodigo
import com.example.mpaz_real_real.ui.components.TarjetaRespuesta
import com.example.mpaz_real_real.ui.components.TarjetaResumenEstado
import com.example.mpaz_real_real.ui.utils.colorFondoEstado
import com.example.mpaz_real_real.ui.utils.colorNumeroResumen
import com.example.mpaz_real_real.ui.utils.obtenerMedidasAsistente
import com.example.mpaz_real_real.viewmodel.CatalogoViewModel

@Composable
fun ResumenAsignacionScreen(
    navController: NavController,
    viewModel: CatalogoViewModel
) {
    val estado by viewModel.state.collectAsState()
    val asistente = estado.asistente
    val asignacion = estado.asignacionRevisada

    // Márgenes y separaciones según el tamaño de la pantalla (Compacta, Mediana o Expandida).
    val medidas = obtenerMedidasAsistente()

    if (asignacion != null) {
        val actividad = estado.actividadPorCodigo(asignacion.actividadCodigo)
        val preguntas = estado.preguntasDe(asignacion.actividadCodigo)
        val respuestas = asistente.respuestasDe(asignacion.codigoTemporal)
        val etiqueta = if (actividad?.modulo == "Matemática") "Ejercicio" else "Pregunta"
        val cursoDificultad = if (actividad != null) "${actividad.curso} · ${actividad.dificultad}" else ""

        var aciertos = 0
        var errores = 0
        // Intentos que el estudiante usó en total (la suma de los intentos de cada respuesta).
        var intentosUsados = 0
        respuestas.forEach { respuesta ->
            intentosUsados = intentosUsados + respuesta.intentos
            val indice = respuesta.numeroPregunta - 1
            if (indice in 0..preguntas.size - 1) {
                if (respuesta.alternativaElegida == preguntas[indice].correcta) {
                    aciertos = aciertos + 1
                } else {
                    errores = errores + 1
                }
            }
        }

        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Parte fija de arriba: el mismo encabezado que la pantalla de Asignaciones,
                // pero sin botón (para volver se usa la barra de navegación del celular).
                EncabezadoAsistente(titulo = "Asignación y resultados")

                // Parte de abajo: se desplaza si el contenido no cabe.
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = medidas.margen, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TituloDeSeccion("Datos de la asignación")

                    // Mientras el estudiante todavía no termina, la asistente necesita el código
                    // temporal a mano para dárselo: se muestra grande y se puede copiar.
                    if (asignacion.estado != "finalizada") {
                        TarjetaCodigo(codigo = asignacion.codigoTemporal)
                    }

                    TablaAsignacion(
                        estudiante = asistente.nombreEstudiante(asignacion.estudianteCodigo),
                        actividad = actividad?.nombre ?: asignacion.actividadCodigo,
                        cursoDificultad = cursoDificultad,
                        asistente = asignacion.asistente,
                        fecha = asignacion.fecha,
                        codigoTemporal = asignacion.codigoTemporal,
                        estado = asignacion.estado,
                        // Pendiente y finalizada llevan además los datos de la actividad (plegados
                        // hasta que se toque "Ver datos de la actividad"); en desarrollo no.
                        detalleActividad = if (asignacion.estado == "en desarrollo") null else actividad
                    )

                    // En desarrollo todavía no hay resultados: se muestran las tarjetas con "--".
                    if (asignacion.estado == "en desarrollo") {
                        TituloDeSeccion("Resultados")
                        TarjetasDeResultados(
                            aciertos = null,
                            errores = null,
                            intentos = null,
                            separacion = medidas.separacion
                        )
                    }

                    // Las respuestas y los resultados solo existen cuando ya se finalizó.
                    if (asignacion.estado == "finalizada") {
                        TituloDeSeccion("Respuestas entregadas")

                        respuestas.forEach { respuesta ->
                            val indice = respuesta.numeroPregunta - 1
                            if (indice in 0..preguntas.size - 1) {
                                val pregunta = preguntas[indice]
                                val alternativas = pregunta.alternativas()
                                val elegida = if (respuesta.alternativaElegida in 0..2) {
                                    alternativas[respuesta.alternativaElegida]
                                } else {
                                    ""
                                }
                                // Texto de la alternativa correcta, que la tarjeta muestra al expandirse.
                                val textoCorrecta = if (pregunta.correcta in 0..2) {
                                    alternativas[pregunta.correcta]
                                } else {
                                    ""
                                }

                                TarjetaRespuesta(
                                    titulo = "$etiqueta ${respuesta.numeroPregunta}",
                                    enunciado = pregunta.enunciado,
                                    elegida = elegida,
                                    intentos = respuesta.intentos,
                                    correcta = respuesta.alternativaElegida == pregunta.correcta,
                                    respuestaCorrecta = textoCorrecta
                                )
                            }
                        }

                        TituloDeSeccion("Resultados")
                        TarjetasDeResultados(
                            aciertos = aciertos,
                            errores = errores,
                            intentos = intentosUsados,
                            separacion = medidas.separacion
                        )
                    }
                }
            }
        }
    }
}

// Título en negrita que separa las secciones de la pantalla ("Respuestas entregadas", etc.).
@Composable
private fun TituloDeSeccion(texto: String) {
    Text(
        text = texto,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
}

// Las tres tarjetas pequeñas de resultados (aciertos, errores e intentos), iguales a las del
// resumen de Asignaciones: ocupan todo el ancho y entre ellas hay `separacion`. Si un valor es
// null todavía no hay datos y la tarjeta muestra "--" en el color normal.
@Composable
private fun TarjetasDeResultados(
    aciertos: Int?,
    errores: Int?,
    intentos: Int?,
    separacion: Dp
) {
    val colorNormal = MaterialTheme.colorScheme.onSurface

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(separacion)
    ) {
        // Con datos: ACIERTOS en verde claro y ERRORES en rojo claro (los mismos fondos que las
        // etiquetas de estado). Sin datos ("--") y INTENTOS quedan en el gris claro normal.
        TarjetaResumenEstado(
            titulo = "ACIERTOS",
            cantidad = aciertos,
            color = if (aciertos == null) colorNormal else colorNumeroResumen("finalizada"),
            modifier = Modifier.weight(1f),
            fondo = if (aciertos == null) null else colorFondoEstado("finalizada")
        )
        TarjetaResumenEstado(
            titulo = "ERRORES",
            cantidad = errores,
            color = if (errores == null) colorNormal else colorNumeroResumen("pendiente"),
            modifier = Modifier.weight(1f),
            fondo = if (errores == null) null else colorFondoEstado("pendiente")
        )
        TarjetaResumenEstado(
            titulo = "INTENTOS",
            cantidad = intentos,
            color = colorNormal,
            modifier = Modifier.weight(1f)
        )
    }
}
