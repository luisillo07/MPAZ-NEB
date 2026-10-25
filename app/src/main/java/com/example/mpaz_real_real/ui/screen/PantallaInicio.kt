package com.example.mpaz_real_real.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mpaz_real_real.ui.components.BotonCatalogo

@Composable
fun InicioScreen(
    onAdmin: () -> Unit,
    onAsistente: () -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            BotonCatalogo("Admin", onAdmin, Modifier.width(280.dp))
            Spacer(modifier = Modifier.height(16.dp))
            BotonCatalogo("Asistente", onAsistente, Modifier.width(280.dp))
        }
    }
}