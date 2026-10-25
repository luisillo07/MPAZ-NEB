package com.example.mpaz_real_real.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mpaz_real_real.model.Actividad
import com.example.mpaz_real_real.navigation.Screen
import com.example.mpaz_real_real.repository.actividadesDePrueba
import com.example.mpaz_real_real.ui.components.BotonActividad
import com.example.mpaz_real_real.ui.components.ContenedorTexto
import com.example.mpaz_real_real.ui.components.EncabezadoActividad
import com.example.mpaz_real_real.ui.theme.Mpaz_real_realTheme
import com.example.mpaz_real_real.ui.theme.RosaFondo
import com.example.mpaz_real_real.viewmodel.ActividadViewModel

@Composable
fun MisActividadesScreen(
    navController: NavController,
    viewModel: ActividadViewModel
) {
    MisActividadesContenido(
        actividades = actividadesDePrueba,
        onEntrar = { codigo ->
            viewModel.cargarActividad(codigo)
            navController.navigate(Screen.Instrucciones.route)
        }
    )
}

@Composable
fun MisActividadesContenido(
    actividades: List<Actividad>,
    onEntrar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            EncabezadoActividad(
                modulo = "Estudiante",
                curso = "2.º básico",
                nombre = "Mis actividades"
            )
        },
        containerColor = RosaFondo,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            item {
                Text(text = "Estas son tus actividades.")
            }
            items(items = actividades, key = { actividad -> actividad.codigo }) { actividad ->
                ContenedorTexto {
                    Text(
                        text = actividad.nombre,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "${actividad.modulo} · ${actividad.curso}")
                    Spacer(modifier = Modifier.height(12.dp))
                    BotonActividad(texto = "Comenzar", onClick = { onEntrar(actividad.codigo) })
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MisActividadesContenidoPreview() {
    Mpaz_real_realTheme {
        MisActividadesContenido(
            actividades = actividadesDePrueba,
            onEntrar = {}
        )
    }
}