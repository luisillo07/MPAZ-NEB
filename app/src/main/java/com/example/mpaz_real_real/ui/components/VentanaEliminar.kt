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
fun VentanaEliminar(
    nombre: String,
    onEliminar: () -> Unit,
    onCancelar: () -> Unit
) {
    Dialog(onDismissRequest = onCancelar) {
        ContenedorTexto {
            Text(
                text = "¿Eliminar la actividad?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Se eliminarán «$nombre» y sus preguntas. No se puede deshacer.",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                BotonActividad(texto = "Eliminar", onClick = onEliminar, colorFondo = Beige, modifier = Modifier.weight(1f))
                BotonActividad(texto = "Cancelar", onClick = onCancelar, colorFondo = Beige, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VentanaEliminarPreview() {
    Mpaz_real_realTheme {
        VentanaEliminar(nombre = "El perro Tito", onEliminar = {}, onCancelar = {})
    }
}