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
import com.example.mpaz_real_real.data.local.actividadesIniciales
import com.example.mpaz_real_real.ui.theme.Mpaz_real_realTheme
import androidx.navigation.NavController
import com.example.mpaz_real_real.navigation.Screen
import androidx.compose.material3.Scaffold
import com.example.mpaz_real_real.ui.components.EncabezadoActividad
import com.example.mpaz_real_real.ui.utils.colorBoton
import com.example.mpaz_real_real.ui.utils.colorFondo
import com.example.mpaz_real_real.ui.components.BotonActividad

import com.example.mpaz_real_real.ui.components.ContenedorTexto
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import com.example.mpaz_real_real.ui.utils.obtenerWindowSizeClass

@Composable
fun InstruccionesScreen(
    navController: NavController,
    viewModel: ActividadViewModel
) {
    val estado by viewModel.estado.collectAsState()
    val windowSizeClass = obtenerWindowSizeClass()
    val onComenzar: () -> Unit = {
        if (estado.intentosUsados > 0) viewModel.onReintentar()
        navController.navigate(Screen.Pregunta.route)
    }
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> InstruccionesCompacta(
            estado = estado,
            onComenzar = onComenzar
        )
        WindowWidthSizeClass.Medium -> InstruccionesMediana(
            estado = estado,
            onComenzar = onComenzar
        )
        WindowWidthSizeClass.Expanded -> InstruccionesExpandida(
            estado = estado,
            onComenzar = onComenzar
        )
    }
}
@Composable
fun InstruccionesCompacta(
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
            containerColor = colorFondo(actividad.modulo),
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
                    BotonActividad(texto = "Comenzar", onClick = onComenzar, colorFondo = colorBoton(actividad.modulo))
                } else {
                    Text(text = "Ya usaste todos los intentos de esta actividad")
                }
            }
        }
    }
}

@Composable
fun InstruccionesMediana(
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
            containerColor = colorFondo(actividad.modulo),
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
                    Row {
                        Spacer(modifier = Modifier.weight(1f))
                        BotonActividad(
                            texto = "Comenzar",
                            onClick = onComenzar,
                            colorFondo = colorBoton(actividad.modulo),
                            modifier = Modifier.weight(3f)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                } else {
                    Text(text = "Ya usaste todos los intentos de esta actividad")
                }
            }
        }
    }
}

@Composable
fun InstruccionesExpandida(
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
            containerColor = colorFondo(actividad.modulo),
            modifier = modifier.fillMaxSize()
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(32.dp)
            ){
                ContenedorTexto {
                    Text(
                        text = actividad.instrucciones,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                if (estado.quedanIntentos) {
                    Row {
                        Spacer(modifier = Modifier.weight(2f))
                        BotonActividad(
                            texto = "Comenzar",
                            onClick = onComenzar,
                            colorFondo = colorBoton(actividad.modulo),
                            modifier = Modifier.weight(5f)
                        )
                        Spacer(modifier = Modifier.weight(2f))
                    }
                } else {
                    Text(
                        text = "Ya usaste todos los intentos de esta actividad",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Preview(name = "Compact", widthDp = 360, heightDp = 800)
@Composable
fun InstruccionesCompactaPreview() {
    Mpaz_real_realTheme {
        InstruccionesCompacta(
            estado = ActividadUiState(actividad = actividadesIniciales.first()),
            onComenzar = {}
        )
    }
}

@Preview(name = "Medium", widthDp = 700, heightDp = 1000)
@Composable
fun InstruccionesMedianaPreview() {
    Mpaz_real_realTheme {
        InstruccionesMediana(
            estado = ActividadUiState(actividad = actividadesIniciales.first()),
            onComenzar = {}
        )
    }
}

@Preview(name = "Expanded", widthDp = 1000, heightDp = 700)
@Composable
fun InstruccionesExpandidaPreview() {
    Mpaz_real_realTheme {
        InstruccionesExpandida(
            estado = ActividadUiState(actividad = actividadesIniciales.first()),
            onComenzar = {}
        )
    }
}