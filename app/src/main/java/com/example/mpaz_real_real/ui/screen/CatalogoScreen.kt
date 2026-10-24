package com.example.mpaz_real_real.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mpaz_real_real.ui.components.BotonCatalogo
import com.example.mpaz_real_real.ui.components.SelectorFiltro
import com.example.mpaz_real_real.ui.components.TarjetaActividad
import com.example.mpaz_real_real.viewmodel.CatalogoViewModel
import androidx.compose.material3.TopAppBarDefaults
import com.example.mpaz_real_real.ui.theme.GrisBarra

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    navController: NavController,
    viewModel: CatalogoViewModel
) {
    val estado by viewModel.state.collectAsState()

    val irAConstruccion: () -> Unit = { navController.navigate("construccion") }

    val opciones = listOf("Todos", "[OPCIÓN 1]", "[OPCIÓN 2]")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Administración pedagógica", style = MaterialTheme.typography.bodySmall)
                            Text("Catálogo de actividades", style = MaterialTheme.typography.titleLarge)
                        }
                        BotonCatalogo("Salir sesión", irAConstruccion)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GrisBarra)
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f), // ocupa todo el alto libre
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            SelectorFiltro("Módulo", estado.modulo, opciones, viewModel::onModuloChange, Modifier.weight(1f))
                            SelectorFiltro("Curso", estado.curso, opciones, viewModel::onCursoChange, Modifier.weight(1f))
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            SelectorFiltro("Contenido", estado.contenido, opciones, viewModel::onContenidoChange, Modifier.weight(1f))
                            SelectorFiltro("Dificultad", estado.dificultad, opciones, viewModel::onDificultadChange, Modifier.weight(1f))
                        }
                        BotonCatalogo("Filtrar", irAConstruccion, Modifier.fillMaxWidth())
                    }
                }

                items(estado.actividades) { actividad ->
                    TarjetaActividad(
                        actividad = actividad,
                        onRevisar = irAConstruccion,
                        onEditar = irAConstruccion,
                        onCambiarEstado = irAConstruccion
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            BotonCatalogo("Agregar", irAConstruccion, Modifier.fillMaxWidth())
        }
    }
}