package com.example.mpaz_real_real.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.mpaz_real_real.ui.theme.CelesteFondo
import com.example.mpaz_real_real.ui.theme.RojoAviso
import com.example.mpaz_real_real.ui.theme.RosaFondo
import com.example.mpaz_real_real.ui.utils.colorFondoEstado
import com.example.mpaz_real_real.ui.utils.colorTextoEstado

// Mensajes (diálogos) de las pantallas del asistente, con el mismo estilo que las tarjetas: borde
// negro fino y esquinas iguales. Cada tipo de mensaje tiene su color de fondo (pastel fijo, igual
// en modo claro y oscuro, por eso el texto es siempre negro) y una etiqueta blanca arriba:
//   - Aviso (color de la materia) · Listo (verde) · Error (rosa) · Confirmar (celeste)
// Los de la pantalla del docente siguen usando DialogoMensaje.

// Base común: la tarjeta con la etiqueta, el título y el contenido que se le pase.
@Composable
private fun DialogoTarjeta(
    etiqueta: String,
    colorEtiqueta: Color,
    fondo: Color,
    titulo: String,
    onCerrar: () -> Unit,
    contenido: @Composable ColumnScope.() -> Unit
) {
    val forma = MaterialTheme.shapes.medium

    Dialog(onDismissRequest = onCerrar) {
        Surface(
            color = fondo,
            shape = forma,
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = Color.Black, shape = forma)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Etiqueta blanca (píldora) con el tipo de mensaje.
                Surface(color = Color.White, shape = CircleShape) {
                    Text(
                        text = etiqueta,
                        color = colorEtiqueta,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
                Text(
                    text = titulo,
                    color = Color.Black,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                contenido()
            }
        }
    }
}

// Mensaje corto con un botón "OK". Sirve para avisos (por ejemplo "Actividad no disponible", con
// el color de la materia como fondo) y para errores (fondo rosa, etiqueta roja).
@Composable
fun DialogoAviso(
    titulo: String,
    mensaje: String,
    fondo: Color,
    onCerrar: () -> Unit,
    etiqueta: String = "Aviso",
    colorEtiqueta: Color = Color.Black
) {
    DialogoTarjeta(
        etiqueta = etiqueta,
        colorEtiqueta = colorEtiqueta,
        fondo = fondo,
        titulo = titulo,
        onCerrar = onCerrar
    ) {
        Text(text = mensaje, color = Color.Black, style = MaterialTheme.typography.bodyMedium)
        BotonCatalogo("OK", onCerrar, Modifier.fillMaxWidth())
    }
}

// Mensaje de error: fondo rosa claro y etiqueta roja.
@Composable
fun DialogoError(
    titulo: String,
    mensaje: String,
    onCerrar: () -> Unit
) {
    DialogoAviso(
        titulo = titulo,
        mensaje = mensaje,
        fondo = RosaFondo,
        onCerrar = onCerrar,
        etiqueta = "Error",
        colorEtiqueta = RojoAviso
    )
}

// Mensaje de éxito al asignar una actividad: fondo verde claro y el código temporal en grande,
// con el botón para copiarlo.
@Composable
fun DialogoAsignada(
    codigo: String,
    onCerrar: () -> Unit
) {
    DialogoTarjeta(
        etiqueta = "Listo",
        colorEtiqueta = colorTextoEstado("finalizada"),
        fondo = colorFondoEstado("finalizada"),
        titulo = "Actividad asignada",
        onCerrar = onCerrar
    ) {
        CodigoGrande(codigo = codigo)
        BotonCatalogo("OK", onCerrar, Modifier.fillMaxWidth())
    }
}

// Pregunta con dos botones (por ejemplo "Cancelar" y "Salir"): fondo celeste. Tocar fuera del
// mensaje equivale a cancelar.
@Composable
fun DialogoConfirmar(
    titulo: String,
    mensaje: String,
    textoConfirmar: String,
    textoCancelar: String,
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {
    DialogoTarjeta(
        etiqueta = "Confirmar",
        colorEtiqueta = Color.Black,
        fondo = CelesteFondo,
        titulo = titulo,
        onCerrar = onCancelar
    ) {
        Text(text = mensaje, color = Color.Black, style = MaterialTheme.typography.bodyMedium)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BotonCatalogo(textoCancelar, onCancelar, Modifier.weight(1f))
            BotonCatalogo(textoConfirmar, onConfirmar, Modifier.weight(1f))
        }
    }
}
