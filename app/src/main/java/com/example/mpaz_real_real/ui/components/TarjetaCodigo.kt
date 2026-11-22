package com.example.mpaz_real_real.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mpaz_real_real.ui.theme.CelesteFondo

// Tarjeta que muestra el código temporal de una asignación en grande, para que la asistente se
// lo pueda dictar o mostrar al estudiante, con un botón para copiarlo. El fondo celeste es fijo
// en modo claro y oscuro, así que el texto también es fijo (negro).
@Composable
fun TarjetaCodigo(codigo: String) {
    val forma = MaterialTheme.shapes.medium

    Surface(
        color = CelesteFondo,
        shape = forma,
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = Color.Black, shape = forma)
    ) {
        CodigoGrande(
            codigo = codigo,
            modifier = Modifier.padding(12.dp)
        )
    }
}

// El contenido de la tarjeta del código: la etiqueta, el código con las cifras separadas (para
// que se lea mejor a distancia) y el botón "Copiar código". Se usa dentro de TarjetaCodigo y
// también en el mensaje que aparece al asignar una actividad. El texto es negro fijo porque
// siempre va sobre un fondo pastel.
@Composable
fun CodigoGrande(
    codigo: String,
    modifier: Modifier = Modifier
) {
    val portapapeles = LocalClipboardManager.current

    // Si ya se copió el código (el botón cambia de texto para avisarlo).
    var copiado by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "CÓDIGO PARA EL ESTUDIANTE",
            color = Color.Black,
            fontSize = 12.sp,
            maxLines = 1
        )
        Text(
            text = codigo,
            color = Color.Black,
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            letterSpacing = 8.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        BotonCatalogo(
            texto = if (copiado) "¡Código copiado!" else "Copiar código",
            onClick = {
                portapapeles.setText(AnnotatedString(codigo))
                copiado = true
            }
        )
    }
}
