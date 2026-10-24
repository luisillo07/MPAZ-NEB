package com.example.mpaz_real_real.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.mpaz_real_real.ui.theme.Beige
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.unit.dp

@Composable
fun BotonCatalogo(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = Beige, contentColor = Color.Black),
        border = BorderStroke(2.dp, Color.Black)
    ) {
        Text(texto)
    }
}