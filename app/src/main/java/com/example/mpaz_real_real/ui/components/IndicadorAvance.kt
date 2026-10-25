package com.example.mpaz_real_real.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mpaz_real_real.ui.theme.Mpaz_real_realTheme
import com.example.mpaz_real_real.ui.theme.RojoCabecera

@Composable
fun IndicadorAvance(
    actual: Int,
    total: Int,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Text(
            text = "Pregunta $actual de $total",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        LinearProgressIndicator(
            progress = { actual.toFloat() / total },
            color = RojoCabecera,
            trackColor = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(MaterialTheme.shapes.medium)
                .border(2.dp, Color.Black, MaterialTheme.shapes.medium)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun IndicadorAvancePreview() {
    Mpaz_real_realTheme {
        IndicadorAvance(actual = 2, total = 4)
    }
}