package com.example.mpaz_real_real.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mpaz_real_real.ui.components.BotonCatalogo
import com.example.mpaz_real_real.ui.components.SelectorFiltro
import com.example.mpaz_real_real.ui.components.TarjetaActividadSeleccion
import com.example.mpaz_real_real.ui.theme.GrisBarra
import com.example.mpaz_real_real.viewmodel.CatalogoViewModel

import com.example.mpaz_real_real.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeleccionarActividadScreen(
    navController: NavController,
    viewModel: CatalogoViewModel
) {
    val estado by viewModel.state.collectAsState()
    val asistente = estado.asistente
    val visibles = estado.actividadesParaAsignar

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Asistente de aula", style = MaterialTheme.typography.bodySmall)
                        Text("Seleccionar actividad", style = MaterialTheme.typography.titleLarge)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GrisBarra)
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        SelectorFiltro("Módulo", asistente.modulo, estado.opcionesModulo, viewModel::onAsistenteModuloChange, Modifier.weight(1f))
                        SelectorFiltro("Curso", asistente.curso, estado.opcionesCurso, viewModel::onAsistenteCursoChange, Modifier.weight(1f))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        SelectorFiltro("Contenido", asistente.contenido, estado.opcionesContenido, viewModel::onAsistenteContenidoChange, Modifier.weight(1f))
                        SelectorFiltro("Dificultad", asistente.dificultad, estado.opcionesDificultad, viewModel::onAsistenteDificultadChange, Modifier.weight(1f))
                    }
                    BotonCatalogo("Filtrar", viewModel::onAsistenteFiltrarClick, Modifier.fillMaxWidth())
                }
            }

            if (visibles.isNotEmpty()) {
                items(visibles) { actividad ->
                    TarjetaActividadSeleccion(
                        actividad = actividad,
                        onClick = {
                            viewModel.onSeleccionarActividad(actividad)
                            navController.navigate(Screen.AsignarActividad.route)
                        }
                    )
                }
            } else {
                item {
                    Text("No hay actividades", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}