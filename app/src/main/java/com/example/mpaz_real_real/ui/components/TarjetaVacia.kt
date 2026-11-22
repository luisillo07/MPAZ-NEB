package com.example.mpaz_real_real.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

// Tarjeta que se muestra cuando una lista está vacía ("No hay actividades con estos filtros").
// Tiene el mismo estilo que las demás tarjetas (gris claro, borde negro fino), el mensaje
// centrado y, si se pasa `textoBoton`, un botón que lleva a la solución (por ejemplo, quitar
// los filtros).
@Composable
fun TarjetaVacia(
    titulo: String,
    mensaje: String,
    textoBoton: String? = null,
    onBoton: () -> Unit = {}
) {
    val forma = MaterialTheme.shapes.medium

    Surface(
        color = fondoTarjetaResumen(),
        shape = forma,
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = Color.Black, shape = forma)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = mensaje,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )
            if (textoBoton != null) {
                BotonCatalogo(
                    texto = textoBoton,
                    onClick = onBoton,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }
    }
}
