package com.example.mpaz_real_real.ui.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mpaz_real_real.navigation.Screen
import com.example.mpaz_real_real.ui.components.BotonCatalogo
import com.example.mpaz_real_real.ui.components.DialogoAsignada
import com.example.mpaz_real_real.ui.components.DialogoConfirmar
import com.example.mpaz_real_real.ui.components.EncabezadoAsistente
import com.example.mpaz_real_real.ui.components.TarjetaAsignacion
import com.example.mpaz_real_real.ui.components.TarjetaResumenEstado
import com.example.mpaz_real_real.ui.components.TarjetaVacia
import com.example.mpaz_real_real.ui.theme.Beige
import com.example.mpaz_real_real.ui.utils.colorFondoEstado
import com.example.mpaz_real_real.ui.utils.colorNumeroResumen
import com.example.mpaz_real_real.ui.utils.obtenerMedidasAsistente
import com.example.mpaz_real_real.ui.utils.textoEstado
import com.example.mpaz_real_real.viewmodel.CatalogoViewModel
import com.example.mpaz_real_real.ui.components.DialogoAviso
import com.example.mpaz_real_real.ui.components.DialogoError
import com.example.mpaz_real_real.ui.utils.colorTextoEstado

@Composable
fun AsignacionesScreen(
    navController: NavController,
    viewModel: CatalogoViewModel
) {
    val estado by viewModel.state.collectAsState()
    val asistente = estado.asistente
    val codigoAsignado = asistente.codigoAsignado
    val mensajeExito = asistente.mensajeExito
    val mensajeError = asistente.mensajeError

    val entradaActual by navController.currentBackStackEntryAsState()

    val medidas = obtenerMedidasAsistente()

    var confirmarSalida by rememberSaveable { mutableStateOf(false) }

    val total = asistente.asignaciones.size
    val pendientes = asistente.asignaciones.count { it.estado == "pendiente" }
    val enDesarrollo = asistente.asignaciones.count { it.estado == "en desarrollo" }
    val finalizadas = asistente.asignaciones.count { it.estado == "finalizada" }

    val onAsignarActividad = {
        viewModel.reiniciarFiltrosAsistente()
        navController.navigate(Screen.SeleccionarActividad.route)
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            EncabezadoAsistente(titulo = "Asignaciones") {
                BotonCatalogo(
                    texto = "Salir sesión",
                    onClick = { confirmarSalida = true }
                )
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = medidas.margen),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { Spacer(modifier = Modifier.height(4.dp)) }

                item {
                    ResumenDeActividades(
                        total = total,
                        pendientes = pendientes,
                        enDesarrollo = enDesarrollo,
                        finalizadas = finalizadas,
                        separacion = medidas.separacion,
                        filtroEstado = asistente.filtroEstado,
                        onFiltroEstadoClick = viewModel::onFiltroEstadoClick
                    )
                }

                item {
                    Text(
                        text = "Todas las asignaciones",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (asistente.filtroEstado.isNotEmpty()) {
                    item {
                        Surface(
                            color = Beige,
                            shape = CircleShape,
                            modifier = Modifier
                                .border(width = 1.dp, color = Color.Black, shape = CircleShape)
                                .clickable { viewModel.onFiltroEstadoClick(asistente.filtroEstado) }
                        ) {
                            Text(
                                text = "Mostrando: ${textoEstado(asistente.filtroEstado)}  ✕",
                                color = Color.Black,
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                if (asistente.asignacionesVisibles.isNotEmpty()) {
                    items(asistente.asignacionesVisibles) { asignacion ->
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
                } else if (asistente.filtroEstado.isNotEmpty()) {
                    item {
                        TarjetaVacia(
                            titulo = "No hay asignaciones",
                            mensaje = "Ninguna asignación tiene el estado «${textoEstado(asistente.filtroEstado)}».",
                            textoBoton = "Quitar filtro",
                            onBoton = { viewModel.onFiltroEstadoClick(asistente.filtroEstado) }
                        )
                    }
                } else {
                    item {
                        TarjetaVacia(
                            titulo = "No hay asignaciones",
                            mensaje = "Cuando asignes una actividad aparecerá aquí."
                        )
                    }
                }

                if (medidas.alturaCompacta) {
                    item {
                        BotonCatalogo(
                            texto = "+ Asignar actividad",
                            onClick = onAsignarActividad,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(4.dp)) }
            }

            if (!medidas.alturaCompacta) {
                BotonCatalogo(
                    texto = "+ Asignar actividad",
                    onClick = onAsignarActividad,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = medidas.margen, vertical = 12.dp)
                        .height(48.dp)
                )
            }
        }
    }

    if (codigoAsignado != null) {
        DialogoAsignada(codigoAsignado, viewModel::onCerrarMensajeAsistente)
    } else if (mensajeExito != null) {
        DialogoAviso(
            titulo = "Listo",
            mensaje = mensajeExito,
            fondo = colorFondoEstado("finalizada"),
            onCerrar = viewModel::onCerrarMensajeAsistente,
            etiqueta = "Listo",
            colorEtiqueta = colorTextoEstado("finalizada")
        )
    }

    if (mensajeError != null) {
        DialogoError("Error", mensajeError, viewModel::onCerrarErrorAsistente)
    }

    if (confirmarSalida) {
        DialogoConfirmar(
            titulo = "¿Salir de la sesión?",
            mensaje = "Volverás a la pantalla de inicio de sesión.",
            textoConfirmar = "Salir",
            textoCancelar = "Cancelar",
            onConfirmar = {
                confirmarSalida = false
                if (entradaActual?.destination?.route == Screen.Asignaciones.route) {
                    navController.popBackStack()
                }
            },
            onCancelar = { confirmarSalida = false }
        )
    }
}

@Composable
private fun ResumenDeActividades(
    total: Int,
    pendientes: Int,
    enDesarrollo: Int,
    finalizadas: Int,
    separacion: Dp,
    filtroEstado: String,
    onFiltroEstadoClick: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Resumen de actividades",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            Surface(
                color = Beige,
                shape = CircleShape,
                modifier = Modifier.border(width = 1.dp, color = Color.Black, shape = CircleShape)
            ) {
                Text(
                    text = if (total == 1) "1 asignación" else "$total asignaciones",
                    color = Color.Black,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(separacion)
        ) {
            TarjetaResumenEstado(
                titulo = "PENDIENTES",
                cantidad = pendientes,
                color = colorNumeroResumen("pendiente"),
                modifier = Modifier.weight(1f),
                fondo = colorFondoEstado("pendiente"),
                seleccionada = filtroEstado == "pendiente",
                onClick = { onFiltroEstadoClick("pendiente") }
            )
            TarjetaResumenEstado(
                titulo = "EN DESARROLLO",
                cantidad = enDesarrollo,
                color = colorNumeroResumen("en desarrollo"),
                modifier = Modifier.weight(1f),
                fondo = colorFondoEstado("en desarrollo"),
                seleccionada = filtroEstado == "en desarrollo",
                onClick = { onFiltroEstadoClick("en desarrollo") }
            )
            TarjetaResumenEstado(
                titulo = "FINALIZADAS",
                cantidad = finalizadas,
                color = colorNumeroResumen("finalizada"),
                modifier = Modifier.weight(1f),
                fondo = colorFondoEstado("finalizada"),
                seleccionada = filtroEstado == "finalizada",
                onClick = { onFiltroEstadoClick("finalizada") }
            )
        }
    }
}