package com.example.mpaz_real_real.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mpaz_real_real.model.Actividad

// Tabla con los datos de una asignación, dentro de una sola tarjeta (mismo gris claro y borde
// negro fino que las tarjetas del resumen). Cada fila lleva la etiqueta a la izquierda y el
// valor a la derecha; entre fila y fila hay una línea negra delgada.
// Si se pasa `detalleActividad`, al final de la tabla aparece la fila "Ver datos de la actividad".
// Al tocarla se despliegan (con animación) el módulo, contenido, objetivo, instrucciones,
// preguntas e intentos. Empieza plegada. Si es null, la tabla queda corta y sin esa fila.
@Composable
fun TablaAsignacion(
    estudiante: String,
    actividad: String,
    cursoDificultad: String,
    asistente: String,
    fecha: String,
    codigoTemporal: String,
    estado: String,
    detalleActividad: Actividad? = null
) {
    val forma = MaterialTheme.shapes.medium

    // Si los datos de la actividad están a la vista. rememberSaveable (cap. 58 del profe) hace
    // que no se pliegue de nuevo si se gira el celular.
    var verDatos by rememberSaveable { mutableStateOf(false) }

    Surface(
        color = fondoTarjetaResumen(),
        shape = forma,
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = Color.Black, shape = forma)
    ) {
        Column {
            FilaDeTexto("Estudiante", estudiante)
            LineaSeparadora()
            FilaDeTexto("Actividad", actividad)
            LineaSeparadora()
            FilaDeTexto("Curso / dificultad", cursoDificultad)
            LineaSeparadora()
            FilaDeTexto("Asistente", asistente)
            LineaSeparadora()
            FilaDeTexto("Fecha", fecha)
            LineaSeparadora()
            FilaDeTexto("Código temporal", codigoTemporal)
            LineaSeparadora()

            // Esta fila no es texto: muestra el estado con su etiqueta de color.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Estado",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                EtiquetaEstado(estado = estado)
            }

            if (detalleActividad != null) {
                LineaSeparadora()

                // Fila que pliega y despliega los datos de la actividad.
                Text(
                    text = if (verDatos) "Ocultar datos de la actividad ▴" else "Ver datos de la actividad ▾",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { verDatos = !verDatos }
                        .padding(horizontal = 12.dp, vertical = 12.dp)
                )

                // AnimatedVisibility (cap. 34 del profe): el bloque se despliega con animación
                // y, al ocultarse, se quita de la pantalla y deja de ocupar espacio.
                AnimatedVisibility(visible = verDatos) {
                    Column {
                        LineaSeparadora()
                        FilaDeTexto("Módulo", detalleActividad.modulo)
                        LineaSeparadora()
                        FilaDeTexto("Contenido", detalleActividad.contenido)
                        LineaSeparadora()
                        // Textos largos: etiqueta arriba y texto debajo, a todo el ancho.
                        FilaDeTextoLargo("Objetivo", detalleActividad.objetivo)
                        LineaSeparadora()
                        FilaDeTextoLargo("Instrucciones", detalleActividad.instrucciones)
                        LineaSeparadora()
                        FilaDeTexto("Cantidad de preguntas", detalleActividad.cantidadPreguntas?.toString() ?: "")
                        LineaSeparadora()
                        FilaDeTexto("Intentos permitidos", detalleActividad.intentosPermitidos?.toString() ?: "")
                    }
                }
            }
        }
    }
}

// Tabla con los tres datos fijos de "Asignar actividad" (actividad, curso/dificultad y asistente
// responsable). Es la misma tarjeta de TablaAsignacion, pero solo con esas tres filas.
@Composable
fun TablaDatosActividad(
    actividad: String,
    cursoDificultad: String,
    asistente: String
) {
    val forma = MaterialTheme.shapes.medium

    Surface(
        color = fondoTarjetaResumen(),
        shape = forma,
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = Color.Black, shape = forma)
    ) {
        Column {
            FilaDeTexto("Actividad asignada", actividad)
            LineaSeparadora()
            FilaDeTexto("Curso / dificultad", cursoDificultad)
            LineaSeparadora()
            FilaDeTexto("Asistente responsable", asistente)
        }
    }
}

// Una fila de la tabla: etiqueta a la izquierda (en negrita) y valor a la derecha. Cada lado
// ocupa la mitad del ancho (weight) para que un valor largo baje de línea sin empujar al otro.
@Composable
private fun FilaDeTexto(etiqueta: String, valor: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}

// Fila para textos largos (objetivo, instrucciones): la etiqueta va arriba en negrita y el texto
// va debajo, ocupando todo el ancho, para que no quede en una columna angosta y muy alta.
@Composable
private fun FilaDeTextoLargo(etiqueta: String, texto: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 12.dp)
    ) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = texto,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

// Línea negra de 1 dp que separa las filas.
@Composable
private fun LineaSeparadora() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color.Black)
    )
}
