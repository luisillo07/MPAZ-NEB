package com.example.mpaz_real_real.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mpaz_real_real.ui.theme.RosadoError

@Composable
fun CampoFormulario(
    etiqueta: String,
    valor: String,
    onValorChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null,
    soloLectura: Boolean = false,
    numerico: Boolean = false,
    lineasMinimas: Int = 1
) {
    Column(modifier = modifier) {
        Text(etiqueta)
        OutlinedTextField(
            value = valor,
            onValueChange = onValorChange,
            readOnly = soloLectura,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (numerico) KeyboardType.Number else KeyboardType.Text
            ),
            singleLine = lineasMinimas == 1,
            minLines = lineasMinimas,
            maxLines = if (lineasMinimas == 1) 1 else lineasMinimas + 2,
            modifier = if (error != null) {
                Modifier
                    .fillMaxWidth()
                    .background(RosadoError)
                    .border(BorderStroke(2.dp, Color.Black))
            } else {
                Modifier.fillMaxWidth()
            }
        )
        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}