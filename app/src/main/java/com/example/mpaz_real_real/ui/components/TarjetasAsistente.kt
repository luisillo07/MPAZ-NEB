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

// Fila de la lista de asignaciones: avatar con las iniciales del estudiante, su nombre, la
// actividad asignada y, a la derecha, una etiqueta con el estado.
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
            // Círculo beige con las iniciales.
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Beige),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = iniciales(estudiante),
                    color = Color.Black, // el fondo beige es fijo: el texto también, así se lee en modo oscuro
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

// Etiqueta redondeada con el estado ("Pendiente", "En desarrollo" o "Finalizada"). El color de
// fondo y el del texto dependen del estado (ver ColoresEstado.kt).
@Composable
fun EtiquetaEstado(estado: String) {
    // CircleShape en un rectángulo da los extremos totalmente redondeados (forma de píldora).
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

// Color de fondo de las tarjetas del resumen y de la tabla de la asignación. En modo claro es un
// gris más claro que el del tema; en modo oscuro se deja el gris oscuro normal del tema.
@Composable
fun fondoTarjetaResumen(): Color {
    return if (isSystemInDarkTheme()) {
        MaterialTheme.colorScheme.surfaceVariant
    } else {
        GrisClaroResumen
    }
}

// Etiqueta redondeada "Correcta" o "Incorrecta". Va sobre una tarjeta que ya es verde o roja
// clara, así que su fondo es blanco (si no, no se distinguiría) y la letra usa el verde oscuro de
// "finalizada" o el rojo oscuro de "pendiente".
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

// Tarjeta de una respuesta entregada: número de pregunta, enunciado, la alternativa que eligió el
// estudiante y cuántos intentos usó. A la derecha, la etiqueta Correcta/Incorrecta. El fondo es
// verde claro si es correcta y rojo claro si no (los mismos de las tarjetas ACIERTOS y ERRORES);
// como el fondo es fijo en modo claro y oscuro, el texto también es fijo (negro).
// Al tocarla se expande y muestra cuál era la respuesta correcta. Modifier.animateContentSize()
// (cap. 34 del profe) hace que la tarjeta crezca y se encoja con animación en vez de saltar.
@Composable
fun TarjetaRespuesta(
    titulo: String,
    enunciado: String,
    elegida: String,
    intentos: Int,
    correcta: Boolean,
    respuestaCorrecta: String
) {
    val forma = MaterialTheme.shapes.medium

    // Si la tarjeta está expandida (mostrando la respuesta correcta). rememberSaveable (cap. 58
    // del profe) hace que no se cierre de nuevo si se gira el celular.
    var expandida by rememberSaveable { mutableStateOf(false) }

    Surface(
        color = colorFondoEstado(if (correcta) "finalizada" else "pendiente"),
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
                Text(text = "Respondió: $elegida", color = Color.Black, style = MaterialTheme.typography.bodySmall)
                Text(text = "Intentos: $intentos", color = Color.Black, style = MaterialTheme.typography.bodySmall)
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

            Spacer(modifier = Modifier.width(12.dp))

            EtiquetaResultado(correcta = correcta)
        }
    }
}

// Tarjeta pequeña del resumen: un título en mayúsculas ("PENDIENTES") y debajo la cantidad con
// dos dígitos ("02") en el color del estado. Ambos van centrados en la tarjeta.
// Si la cantidad es null (todavía no hay datos), en su lugar se muestra "--".
// Si se pasa `fondo` (un color pastel), la tarjeta usa ese fondo y el título va en negro fijo,
// para que se lea igual en modo claro y oscuro. Sin `fondo` usa el gris claro de siempre.
// Si se pasa `onClick`, la tarjeta se puede tocar (sirve para filtrar la lista por ese estado).
// Cuando `seleccionada` es true, el borde se engrosa con una animación (animateDpAsState, cap.
// 34 del profe) para que se note cuál filtro está activo.
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
        // El relleno de los lados es mínimo (2 dp) a propósito: las tarjetas son angostas y
        // "EN DESARROLLO" (12 sp) mide unos 96 dp; con más relleno el texto se cortaría.
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

// Saca las iniciales de un nombre: la primera letra de las dos primeras palabras, en
// mayúsculas. "Estudiante ficticio 1" -> "EF". Si el nombre tiene una sola palabra, usa solo
// esa letra.
private fun iniciales(nombre: String): String {
    val palabras = nombre.split(" ")
    val primera = palabras[0].take(1)
    val segunda = if (palabras.size > 1) palabras[1].take(1) else ""
    return (primera + segunda).uppercase()
}

// Tarjeta de una actividad en "Seleccionar actividad": nombre, módulo y curso, contenido y
// dificultad, y a la derecha un recuadro que dice si está disponible. El fondo es el color de la
// materia (rojo suave para Lectura, azul suave para Matemática); si no está disponible, es el
// mismo color pero más oscuro y apagado, y el texto se ve más atenuado. Los colores son fijos
// (iguales en modo claro y oscuro), así que el texto también es fijo. Siempre se puede tocar:
// qué pasa al tocarla lo decide la pantalla (asignar la actividad o avisar que no está disponible).
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

// Etiqueta redondeada y plana (sin borde): "Disponible" con el mismo formato que "Finalizada" de
// Asignaciones (fondo verde claro y letra verde oscuro), o "No disponible" en gris con letra
// negra. Los colores son fijos, así que la letra también.
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
