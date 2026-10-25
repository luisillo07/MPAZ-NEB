package com.example.mpaz_real_real.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mpaz_real_real.ui.theme.RosadoError

@Composable
fun MensajeError(
    texto: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = texto,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodySmall,
        modifier = modifier
            .fillMaxWidth()
            .background(RosadoError)
            .border(BorderStroke(2.dp, Color.Black))
            .padding(8.dp)
    )
}