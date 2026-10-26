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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mpaz_real_real.ui.components.BotonCatalogo
import com.example.mpaz_real_real.ui.components.DialogoMensaje
import com.example.mpaz_real_real.ui.components.TarjetaAsignacion
import com.example.mpaz_real_real.ui.theme.GrisBarra
import com.example.mpaz_real_real.viewmodel.CatalogoViewModel
import com.example.mpaz_real_real.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsignacionesScreen(
    navController: NavController,
    viewModel: CatalogoViewModel
) {
    val estado by viewModel.state.collectAsState()
    val asistente = estado.asistente
    val mensajeExito = asistente.mensajeExito

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
                            Text("Asistente de aula", style = MaterialTheme.typography.bodySmall)
                            Text("Asignaciones", style = MaterialTheme.typography.titleLarge)
                        }
                        BotonCatalogo("Salir sesión", { navController.popBackStack() })
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
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (asistente.asignaciones.isNotEmpty()) {
                    items(asistente.asignaciones) { asignacion ->
                        TarjetaAsignacion(
                            estudiante = asistente.nombreEstudiante(asignacion.estudianteCodigo),
                            actividad = estado.nombreActividad(asignacion.actividadCodigo),
                            estado = asignacion.estado,
                            onClick = {
                                viewModel.onAsignacionClick(asignacion)
                                navController.navigate(Screen.ResumenAsignacion.route)
                            }
                        )
                    }
                } else {
                    item {
                        Text("No hay asignaciones", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            BotonCatalogo(
                texto = "Asignar actividad",
                onClick = {
                    viewModel.reiniciarFiltrosAsistente()
                    navController.navigate(Screen.SeleccionarActividad.route)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    if (mensajeExito != null) {
        DialogoMensaje("Listo", mensajeExito, viewModel::onCerrarMensajeAsistente)
    }
}