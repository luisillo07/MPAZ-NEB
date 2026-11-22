package com.example.mpaz_real_real.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mpaz_real_real.model.Actividad
import com.example.mpaz_real_real.ui.theme.Beige
import com.example.mpaz_real_real.ui.theme.GrisBarra
import com.example.mpaz_real_real.ui.theme.GrisClaroResumen
import com.example.mpaz_real_real.ui.theme.TextoNoDisponible
import com.example.mpaz_real_real.ui.utils.colorFondo
import com.example.mpaz_real_real.ui.utils.colorFondoEstado
import com.example.mpaz_real_real.ui.utils.colorFondoNoDisponible
import com.example.mpaz_real_real.ui.utils.colorTextoEstado
import com.example.mpaz_real_real.ui.utils.textoEstado

@Composable
fun TarjetaAsignacion(
    estudiante: String,
    actividad: String,
    estado: String,
    onClick: () -> Unit
) {
    val forma = MaterialTheme.shapes.medium

    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = forma,
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = Color.Black, shape = forma)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Beige),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = iniciales(estudiante),
                    color = Color.Black,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = estudiante,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(text = actividad, style = MaterialTheme.typography.bodySmall)
            }

            EtiquetaEstado(estado = estado)
        }
    }
}

@Composable
fun EtiquetaEstado(estado: String) {
    Surface(
        color = colorFondoEstado(estado),
        shape = CircleShape
    ) {
        Text(
            text = textoEstado(estado),
            color = colorTextoEstado(estado),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun fondoTarjetaResumen(): Color {
    return if (isSystemInDarkTheme()) {
        MaterialTheme.colorScheme.surfaceVariant
    } else {
        GrisClaroResumen
    }
}

@Composable
fun EtiquetaResultado(correcta: Boolean) {
    val estado = if (correcta) "finalizada" else "pendiente"

    Surface(
        color = Color.White,
        shape = CircleShape
    ) {
        Text(
            text = if (correcta) "Correcta" else "Incorrecta",
            color = colorTextoEstado(estado),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun TarjetaRespuesta(
    titulo: String,
    enunciado: String,
    elegida: String,
    intentos: Int,
    correcta: Boolean,
    respuestaCorrecta: String,
    respondida: Boolean = true
) {
    val forma = MaterialTheme.shapes.medium

    var expandida by rememberSaveable { mutableStateOf(false) }

    Surface(
        color = if (respondida) colorFondoEstado(if (correcta) "finalizada" else "pendiente") else GrisClaroResumen,
        shape = forma,
        modifier = Modifier
            .animateContentSize()
            .fillMaxWidth()
            .border(width = 1.dp, color = Color.Black, shape = forma)
            .clickable { expandida = !expandida }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = titulo,
                    color = Color.Black,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(text = enunciado, color = Color.Black, style = MaterialTheme.typography.bodyMedium)
                Text(text = if (respondida) "Respondió: $elegida" else elegida, color = Color.Black, style = MaterialTheme.typography.bodySmall)
                if (intentos > 0) {
                    Text(text = "Intentos: $intentos", color = Color.Black, style = MaterialTheme.typography.bodySmall)
                }
                if (expandida) {
                    Text(
                        text = "Respuesta correcta: $respuestaCorrecta",
                        color = Color.Black,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = if (expandida) "Toca para ocultar ▴" else "Toca para ver la respuesta correcta ▾",
                    color = Color.Black,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (respondida) {
                Spacer(modifier = Modifier.width(12.dp))

                EtiquetaResultado(correcta = correcta)
            }
        }
    }
}

@Composable
fun TarjetaResumenEstado(
    titulo: String,
    cantidad: Int?,
    color: Color,
    modifier: Modifier = Modifier,
    fondo: Color? = null,
    seleccionada: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val forma = MaterialTheme.shapes.medium
    val grosorBorde by animateDpAsState(
        targetValue = if (seleccionada) 3.dp else 1.dp,
        label = "bordeResumen"
    )

    Surface(
        color = fondo ?: fondoTarjetaResumen(),
        shape = forma,
        modifier = modifier
            .border(width = grosorBorde, color = Color.Black, shape = forma)
            .clickable(enabled = onClick != null) { onClick?.invoke() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = titulo,
                color = if (fondo != null) Color.Black else Color.Unspecified,
                fontSize = 12.sp,
                maxLines = 1
            )
            Text(
                text = if (cantidad == null) "--" else String.format("%02d", cantidad),
                color = color,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun iniciales(nombre: String): String {
    val palabras = nombre.split(" ")
    val primera = palabras[0].take(1)
    val segunda = if (palabras.size > 1) palabras[1].take(1) else ""
    return (primera + segunda).uppercase()
}

@Composable
fun TarjetaActividadSeleccion(
    actividad: Actividad,
    onClick: () -> Unit
) {
    val disponible = actividad.estado == "disponible"
    val forma = MaterialTheme.shapes.medium
    val colorTexto = if (disponible) Color.Black else TextoNoDisponible

    Surface(
        color = if (disponible) {
            colorFondo(actividad.modulo)
        } else {
            colorFondoNoDisponible(actividad.modulo)
        },
        shape = forma,
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = Color.Black, shape = forma)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = actividad.nombre,
                    color = colorTexto,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                EtiquetaDisponibilidad(disponible = disponible)
            }
            Text(
                text = "${actividad.modulo} · ${actividad.curso}",
                color = colorTexto,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "${actividad.contenido} · ${actividad.dificultad}",
                color = colorTexto,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun EtiquetaDisponibilidad(disponible: Boolean) {
    Surface(
        color = if (disponible) colorFondoEstado("finalizada") else GrisBarra,
        shape = CircleShape
    ) {
        Text(
            text = if (disponible) "Disponible" else "No disponible",
            color = if (disponible) colorTextoEstado("finalizada") else Color.Black,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}