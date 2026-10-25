package com.example.mpaz_real_real.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mpaz_real_real.ui.theme.Mpaz_real_realTheme
import com.example.mpaz_real_real.ui.theme.RojoAviso

@Composable
fun AvisoError(
    mensaje: String,
    modifier: Modifier = Modifier
) {
    ContenedorTexto(modifier = modifier,colorBorde = RojoAviso) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = RojoAviso
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = mensaje, color = RojoAviso)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AvisoErrorPreview(){
    Mpaz_real_realTheme{
        AvisoError(mensaje = "Debes elegir una alternativa para continuar.")
    }
}