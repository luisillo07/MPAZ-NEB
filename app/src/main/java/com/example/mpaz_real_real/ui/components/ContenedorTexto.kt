package com.example.mpaz_real_real.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mpaz_real_real.ui.theme.Mpaz_real_realTheme

@Composable
fun ContenedorTexto(
    modifier: Modifier = Modifier,
    colorBorde: Color = Color.Black,
    colorFondo: Color = Color.White,
    content: @Composable () -> Unit
){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colorFondo,MaterialTheme.shapes.medium)
            .border(2.dp, colorBorde, MaterialTheme.shapes.medium)
            .padding(16.dp)
    ){
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun ContenedorTextoPreview() {
    Mpaz_real_realTheme {
        ContenedorTexto {
            Text(text = "Lee el texto con calma y responde las preguntas.")
        }
    }
}