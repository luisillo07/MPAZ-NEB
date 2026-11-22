package com.example.mpaz_real_real.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mpaz_real_real.navigation.Screen
import com.example.mpaz_real_real.ui.components.BotonCatalogo
import com.example.mpaz_real_real.ui.components.DialogoError
import com.example.mpaz_real_real.ui.components.EncabezadoAsistente
import com.example.mpaz_real_real.ui.components.TablaDatosActividad
import com.example.mpaz_real_real.ui.components.TarjetaFiltro
import com.example.mpaz_real_real.ui.utils.obtenerMedidasAsistente
import com.example.mpaz_real_real.viewmodel.CatalogoViewModel
import com.example.mpaz_real_real.ui.components.CampoFormulario
import com.example.mpaz_real_real.viewmodel.LoginViewModel

@Composable
fun AsignarActividadScreen(
    navController: NavController,
    viewModel: CatalogoViewModel,
    loginViewModel: LoginViewModel
) {
    val estado by viewModel.state.collectAsState()
    val estadoLogin by loginViewModel.state.collectAsState()
    val asistente = estado.asistente
    val formulario = asistente.formulario
    val actividad = estado.actividadPorCodigo(formulario.actividadCodigo)
    val cursoDificultad = if (actividad != null) "${actividad.curso} · ${actividad.dificultad}" else ""

    val medidas = obtenerMedidasAsistente()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            EncabezadoAsistente(titulo = "Asignar actividad")

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = medidas.margen, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TablaDatosActividad(
                    actividad = actividad?.nombre ?: "",
                    cursoDificultad = cursoDificultad,
                    asistente = estadoLogin.usuarioSesion
                )

                if (medidas.expandida) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TarjetaFiltro(
                            titulo = "Estudiante ficticio",
                            seleccion = formulario.estudiante,
                            opciones = estado.opcionesEstudiante,
                            onSeleccion = viewModel::onAsignarEstudianteChange,
                            modifier = Modifier.weight(1f),
                            valorNeutro = "",
                            textoVacio = "Seleccionar",
                            error = formulario.errores.estudiante
                        )
                        CampoFormulario(
                            etiqueta = "Hora límite (dd/MM/yyyy HH:mm)",
                            valor = formulario.fecha,
                            onValorChange = viewModel::onAsignarFechaChange,
                            modifier = Modifier.weight(1f),
                            error = formulario.errores.fecha
                        )
                        CampoFormulario(
                            etiqueta = "Minutos de duración",
                            valor = formulario.minutos,
                            onValorChange = viewModel::onAsignarMinutosChange,
                            modifier = Modifier.weight(1f),
                            error = formulario.errores.minutos,
                            numerico = true
                        )
                    }
                } else {
                    TarjetaFiltro(
                        titulo = "Estudiante ficticio",
                        seleccion = formulario.estudiante,
                        opciones = estado.opcionesEstudiante,
                        onSeleccion = viewModel::onAsignarEstudianteChange,
                        modifier = Modifier.fillMaxWidth(),
                        valorNeutro = "",
                        textoVacio = "Seleccionar",
                        error = formulario.errores.estudiante
                    )
                    CampoFormulario(
                        etiqueta = "Hora límite (dd/MM/yyyy HH:mm)",
                        valor = formulario.fecha,
                        onValorChange = viewModel::onAsignarFechaChange,
                        modifier = Modifier.fillMaxWidth(),
                        error = formulario.errores.fecha
                    )
                    CampoFormulario(
                        etiqueta = "Minutos de duración",
                        valor = formulario.minutos,
                        onValorChange = viewModel::onAsignarMinutosChange,
                        modifier = Modifier.fillMaxWidth(),
                        error = formulario.errores.minutos,
                        numerico = true
                    )
                }

                BotonCatalogo(
                    texto = "Asignar",
                    onClick = {
                        if (viewModel.onAsignarClick(estadoLogin.usuarioSesion)) {
                            navController.navigate(Screen.Asignaciones.route) {
                                popUpTo(Screen.Asignaciones.route) { inclusive = true }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    val mensajeError = asistente.mensajeError
    if (mensajeError != null) {
        DialogoError("No se pudo asignar", mensajeError, viewModel::onCerrarErrorAsistente)
    }
}