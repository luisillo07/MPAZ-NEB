package com.example.mpaz_real_real.ui.theme.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.mpaz_real_real.viewmodel.ActividadViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.material3.Button


@Composable
fun InstruccionesScreen(
    onComenzar: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ActividadViewModel = viewModel()
){
    val estado by viewModel.estado.collectAsState()
    val actividad = estado.actividad

    if (actividad == null) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            Text(text = actividad.nombre,style = MaterialTheme.typography.titleLarge)
            Text(text = actividad.instrucciones)
            Button(onClick = onComenzar){
                Text(text = "Comenzar")
            }
        }
    }

}