package com.example.mpaz_real_real.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mpaz_real_real.ui.theme.Beige

@Composable
fun CajaSoloLectura(
    etiqueta: String,
    texto: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(etiqueta)
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(2.dp, Color.Black))
                .padding(16.dp)
        ) {
            Text(texto)
        }
    }
}

@Composable
fun AlternativaSoloLectura(
    letra: String,
    texto: String,
    destacada: Boolean
) {
    val base = if (destacada) Modifier.fillMaxWidth().background(Beige) else Modifier.fillMaxWidth()
    Row(
        modifier = base
            .border(BorderStroke(2.dp, Color.Black))
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RadioButton(selected = destacada, onClick = null)
        Text(
            text = "$letra) $texto",
            style = if (destacada) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge
        )
    }
}