package com.example.mpaz_real_real.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mpaz_real_real.ui.utils.obtenerMedidasAsistente

// Franja gris clara de arriba de las pantallas del asistente: etiqueta pequeña "ASISTENTE DE
// AULA", un título grande y, si se pasa, algo a la derecha (por ejemplo el botón de salir). Va
// dentro del contenido (debajo de la barra de estado del celular), no en una TopAppBar. Mide
// unos 66 dp, así su borde inferior queda a unos 88 dp del borde de la pantalla. El margen de los
// lados cambia según el tamaño de la pantalla (Compacta, Mediana o Expandida).
@Composable
fun EncabezadoAsistente(
    titulo: String,
    derecha: @Composable () -> Unit = {}
) {
    val medidas = obtenerMedidasAsistente()

    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = medidas.margen, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // bodySmall ya mide 12 sp y fija la altura de la línea en 16 dp; así el encabezado
                // no crece aunque la letra sea más grande que antes (10 sp).
                Text(
                    text = "ASISTENTE DE AULA",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            derecha()
        }
    }
}
