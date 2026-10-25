package com.example.mpaz_real_real.ui.components

import androidx.compose.runtime.Composable
import android.net.Uri
import coil3.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.foundation.layout.size

import androidx.compose.ui.tooling.preview.Preview
import com.example.mpaz_real_real.ui.theme.Mpaz_real_realTheme

@Composable
fun PantallazoPregunta(uri: Uri?) {
    if (uri != null) {
        AsyncImage(
            model = uri,
            contentDescription = "Pantallazo de la pregunta que más le costó",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    } else {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Sin pantallazo adjunto",
            modifier = Modifier.size(64.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PantallazoPreguntaPreview() {
    Mpaz_real_realTheme {
        PantallazoPregunta(uri = null)
    }
}