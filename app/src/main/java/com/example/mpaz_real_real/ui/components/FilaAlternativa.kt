package com.example.mpaz_real_real.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FilaAlternativa(
    letra: String,
    valor: String,
    onValorChange: (String) -> Unit,
    esCorrecta: Boolean,
    onMarcarCorrecta: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = valor,
            onValueChange = onValorChange,
            label = { Text("Alternativa $letra") },
            singleLine = true,
            modifier = Modifier.weight(1f)
        )
        RadioButton(
            selected = esCorrecta,
            onClick = onMarcarCorrecta
        )
    }
}