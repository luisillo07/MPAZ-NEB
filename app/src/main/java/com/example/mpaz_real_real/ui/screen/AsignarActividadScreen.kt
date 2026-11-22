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

@Composable
fun AsignarActividadScreen(
    navController: NavController,
    viewModel: CatalogoViewModel
) {
    val estado by viewModel.state.collectAsState()
    val asistente = estado.asistente
    val formulario = asistente.formulario
    val actividad = estado.actividadPorCodigo(formulario.actividadCodigo)
    val cursoDificultad = if (actividad != null) "${actividad.curso} · ${actividad.dificultad}" else ""

    // Márgenes según el tamaño de la pantalla (Compacta, Mediana o Expandida).
    val medidas = obtenerMedidasAsistente()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Parte fija de arriba: el mismo encabezado que las otras pantallas del asistente
            // (sin botón: para volver se usa la barra de navegación del celular).
            EncabezadoAsistente(titulo = "Asignar actividad")

            // Parte de abajo: se desplaza si el contenido no cabe.
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = medidas.margen, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Los tres datos que no se pueden cambiar, juntos en una tarjeta tipo tabla.
                TablaDatosActividad(
                    actividad = actividad?.nombre ?: "",
                    cursoDificultad = cursoDificultad,
                    asistente = asistente.asistenteResponsable
                )

                // Los dos datos que sí hay que elegir. valorNeutro = "" significa que, mientras no
                // se elija nada, la tarjeta muestra "Seleccionar" y queda gris; al elegir se
                // pone celeste, y si falta elegir al asignar, se pone roja y muestra el error.
                if (medidas.expandida) {
                    // Pantalla ancha: los dos selectores van uno al lado del otro.
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
                        TarjetaFiltro(
                            titulo = "Fecha",
                            seleccion = formulario.fecha,
                            opciones = estado.opcionesFechaAsignacion,
                            onSeleccion = viewModel::onAsignarFechaChange,
                            modifier = Modifier.weight(1f),
                            valorNeutro = "",
                            textoVacio = "Seleccionar",
                            error = formulario.errores.fecha
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
                    TarjetaFiltro(
                        titulo = "Fecha",
                        seleccion = formulario.fecha,
                        opciones = estado.opcionesFechaAsignacion,
                        onSeleccion = viewModel::onAsignarFechaChange,
                        modifier = Modifier.fillMaxWidth(),
                        valorNeutro = "",
                        textoVacio = "Seleccionar",
                        error = formulario.errores.fecha
                    )
                }

                BotonCatalogo(
                    texto = "Asignar",
                    onClick = {
                        // La pantalla vuelve a la lista solo cuando la asignación se guardó bien.
                        viewModel.onAsignarClick {
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

    // Si no se pudo guardar, se avisa y se queda en esta pantalla para poder intentarlo de nuevo.
    val mensajeError = asistente.mensajeError
    if (mensajeError != null) {
        DialogoError("No se pudo asignar", mensajeError, viewModel::onCerrarErrorAsistente)
    }
}
