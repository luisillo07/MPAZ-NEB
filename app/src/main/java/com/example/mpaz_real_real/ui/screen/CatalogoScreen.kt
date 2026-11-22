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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mpaz_real_real.ui.components.BotonCatalogo
import com.example.mpaz_real_real.ui.components.DialogoMensaje
import com.example.mpaz_real_real.ui.components.SelectorFiltro
import com.example.mpaz_real_real.ui.components.TarjetaActividad
import com.example.mpaz_real_real.ui.theme.GrisBarra
import com.example.mpaz_real_real.viewmodel.CatalogoViewModel
import com.example.mpaz_real_real.navigation.Screen
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.window.Dialog
import com.example.mpaz_real_real.ui.components.MensajeError

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    navController: NavController,
    viewModel: CatalogoViewModel
) {
    val estado by viewModel.state.collectAsState()
    val mensajeError = estado.mensajeError
    val mensajeExito = estado.mensajeExito
    val mensajeAlerta = estado.mensajeAlerta
    val visibles = estado.actividadesVisibles
    val entradaActual by navController.currentBackStackEntryAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarServidor()
    }

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
                        BotonCatalogo("Salir sesión", {
                            if (entradaActual?.destination?.route == Screen.Catalogo.route) {
                                viewModel.reiniciarFiltros()
                                navController.popBackStack()
                            }
                        })
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            SelectorFiltro("Módulo", estado.modulo, estado.opcionesModulo, viewModel::onModuloChange, Modifier.weight(1f))
                            SelectorFiltro("Curso", estado.curso, estado.opcionesCurso, viewModel::onCursoChange, Modifier.weight(1f))
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            SelectorFiltro("Contenido", estado.contenido, estado.opcionesContenido, viewModel::onContenidoChange, Modifier.weight(1f))
                            SelectorFiltro("Dificultad", estado.dificultad, estado.opcionesDificultad, viewModel::onDificultadChange, Modifier.weight(1f))
                        }
                        BotonCatalogo("Filtrar", viewModel::onFiltrarClick, Modifier.fillMaxWidth())
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (estado.servidorConectado) {
                                    "Actividades en el servidor: ${estado.actividadesServidor.size}"
                                } else {
                                    "Sin conexión con el servidor."
                                },
                                modifier = Modifier.weight(1f)
                            )
                            BotonCatalogo("Actualizar", viewModel::cargarServidor)
                        }
                        if (estado.pendientes.isNotEmpty()) {
                            MensajeError("${estado.errorServidor} Actividades sin enviar: ${estado.pendientes.size}.")
                            BotonCatalogo("Reintentar", viewModel::onReintentar, Modifier.fillMaxWidth())
                        }
                    }
                }

                if (visibles.isNotEmpty()) {
                    items(visibles) { actividad ->
                        TarjetaActividad(
                            actividad = actividad,
                            onRevisar = {
                                viewModel.onRevisarClick(actividad)
                                navController.navigate(Screen.Revisar.route)
                            },
                            onEditar = {
                                viewModel.onEditarClick(actividad)
                                navController.navigate(Screen.Formulario.route)
                            },
                            onCambiarEstado = { viewModel.onCambiarEstado(actividad.codigo) }
                        )
                    }
                } else {
                    item {
                        Text("No hay actividades", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            BotonCatalogo(
                texto = "Agregar",
                onClick = {
                    viewModel.onAgregarClick()
                    navController.navigate(Screen.Formulario.route)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    if (mensajeError != null) {
        DialogoMensaje("Error", mensajeError, viewModel::onCerrarMensaje)
    }
    if (mensajeExito != null && !estado.enviando) {
        DialogoMensaje("Listo", mensajeExito, viewModel::onCerrarMensajeExito)
    }
    if (mensajeAlerta != null && !estado.enviando) {
        DialogoMensaje("Alerta", mensajeAlerta, viewModel::onCerrarAlerta)
    }
    if (estado.enviando) {
        Dialog(onDismissRequest = {}) {
            CircularProgressIndicator()
        }
    }
}