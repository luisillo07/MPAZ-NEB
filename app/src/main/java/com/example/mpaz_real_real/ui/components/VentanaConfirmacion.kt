package com.example.mpaz_real_real.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.mpaz_real_real.ui.theme.Beige
import com.example.mpaz_real_real.ui.theme.Mpaz_real_realTheme

@Composable
fun VentanaConfirmacion(
    respondidas: Int,
    total: Int,
    onEnviar: () -> Unit,
    onCambiar: () -> Unit
) {
    Dialog(onDismissRequest = onCambiar) {
        ContenedorTexto {
            Text(
                text = "¿Enviar tus respuestas?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$respondidas de $total preguntas respondidas",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "${total - respondidas} de $total sin responder",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                BotonActividad(texto = "Enviar", onClick = onEnviar, colorFondo = Beige, modifier = Modifier.weight(1f))
                BotonActividad(texto = "Cambiar", onClick = onCambiar, colorFondo = Beige, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VentanaConfirmacionPreview() {
    Mpaz_real_realTheme {
        VentanaConfirmacion(respondidas = 4, total = 4, onEnviar = {}, onCambiar = {})
    }
}