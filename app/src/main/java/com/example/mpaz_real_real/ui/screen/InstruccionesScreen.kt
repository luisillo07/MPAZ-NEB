package com.example.mpaz_real_real.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.mpaz_real_real.viewmodel.ActividadViewModel
import androidx.compose.ui.Alignment
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import com.example.mpaz_real_real.model.ActividadUiState
import com.example.mpaz_real_real.repository.actividadesDePrueba
import com.example.mpaz_real_real.ui.theme.Mpaz_real_realTheme
import androidx.navigation.NavController
import com.example.mpaz_real_real.navigation.Screen
import androidx.compose.material3.Scaffold
import com.example.mpaz_real_real.ui.components.EncabezadoActividad
import com.example.mpaz_real_real.ui.theme.RosaFondo
import com.example.mpaz_real_real.ui.components.BotonActividad

import com.example.mpaz_real_real.ui.components.ContenedorTexto

@Composable
fun InstruccionesScreen(
    navController: NavController,
    viewModel: ActividadViewModel
) {
    val estado by viewModel.estado.collectAsState()
    InstruccionesContenido(
        estado = estado,
        onComenzar = {
            if (estado.intentosUsados > 0) viewModel.onReintentar()
            navController.navigate(Screen.Pregunta.route)
        }
    )
}
@Composable
fun InstruccionesContenido(
    estado: ActividadUiState,
    onComenzar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val actividad = estado.actividad
    if (actividad == null){
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize()
        ){
            CircularProgressIndicator()
        }
    }else{
        Scaffold(
            topBar = {
                EncabezadoActividad(
                    modulo = actividad.modulo,
                    curso = actividad.curso,
                    nombre = actividad.nombre
                )
            },
            containerColor = RosaFondo,
            modifier = modifier.fillMaxSize()
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ){
                ContenedorTexto {
                    Text(text = actividad.instrucciones)
                }
                if (estado.quedanIntentos) {
                    BotonActividad(texto = "Comenzar", onClick = onComenzar)
                } else {
                    Text(text = "Ya usaste todos los intentos de esta actividad")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InstruccionesContenidoPreview() {
    Mpaz_real_realTheme {
        InstruccionesContenido(
            estado = ActividadUiState(actividad = actividadesDePrueba.first()),
            onComenzar = {}
        )
    }
}