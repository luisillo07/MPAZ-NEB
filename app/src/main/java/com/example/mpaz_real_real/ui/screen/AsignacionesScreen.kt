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

@Composable
fun AsignacionesScreen(
    navController: NavController,
    viewModel: CatalogoViewModel
) {
    val estado by viewModel.state.collectAsState()
    val asistente = estado.asistente
    val codigoAsignado = asistente.codigoAsignado

    // Pantalla en la que estamos ahora mismo según el NavController (cap. 39 del profe).
    // Sirve para que "Salir sesión" vuelva atrás UNA sola vez aunque se pulse dos veces muy rápido.
    val entradaActual by navController.currentBackStackEntryAsState()

    // Márgenes y separaciones según el tamaño de la pantalla (Compacta, Mediana o Expandida).
    val medidas = obtenerMedidasAsistente()

    // Si se está preguntando "¿Salir de la sesión?". rememberSaveable (cap. 58 del profe) hace
    // que la pregunta no desaparezca si se gira el celular.
    var confirmarSalida by rememberSaveable { mutableStateOf(false) }

    // Cuántas asignaciones hay en total y en cada estado (para el resumen).
    val total = asistente.asignaciones.size
    val pendientes = asistente.asignaciones.count { it.estado == "pendiente" }
    val enDesarrollo = asistente.asignaciones.count { it.estado == "en desarrollo" }
    val finalizadas = asistente.asignaciones.count { it.estado == "finalizada" }

    // El botón de "+ Asignar actividad" se usa en dos lugares (según la altura de la pantalla),
    // por eso se guarda aquí lo que hace al tocarlo.
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
            // Parte fija de arriba: el encabezado.
            EncabezadoAsistente(titulo = "Asignaciones") {
                BotonCatalogo(
                    texto = "Salir sesión",
                    onClick = { confirmarSalida = true }
                )
            }

            // Parte del medio: se desplaza (resumen, tarjetas de conteo y lista).
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

                // Si hay un filtro activo, se avisa cuál es y se puede quitar tocándolo.
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
                                color = Color.Black, // el fondo beige es fijo: el texto también
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

                // En una pantalla muy baja (celular en horizontal) el botón no queda fijo abajo,
                // porque le quitaría casi todo el espacio a la lista: va al final de ella.
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

            // Parte fija de abajo: el botón (solo cuando la pantalla es lo bastante alta).
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

    // Después de asignar una actividad: mensaje verde con el código temporal en grande.
    if (codigoAsignado != null) {
        DialogoAsignada(codigoAsignado, viewModel::onCerrarMensajeAsistente)
    }

    if (confirmarSalida) {
        DialogoConfirmar(
            titulo = "¿Salir de la sesión?",
            mensaje = "Volverás a la pantalla de inicio de sesión.",
            textoConfirmar = "Salir",
            textoCancelar = "Cancelar",
            onConfirmar = {
                confirmarSalida = false
                // Solo se vuelve si todavía estamos en Asignaciones. Si ya se volvió
                // al login, un segundo toque no hace nada (si no, sacaría también el
                // login y la pantalla quedaría en blanco).
                if (entradaActual?.destination?.route == Screen.Asignaciones.route) {
                    navController.popBackStack()
                }
            },
            onCancelar = { confirmarSalida = false }
        )
    }
}

// Título "Resumen de actividades", la etiqueta con el total y las tres tarjetas de conteo. Las
// tarjetas se pueden tocar para filtrar la lista por ese estado; la elegida se marca con un
// borde más grueso.
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

            // Etiqueta beige con el total: "6 asignaciones".
            Surface(
                color = Beige,
                shape = CircleShape,
                modifier = Modifier.border(width = 1.dp, color = Color.Black, shape = CircleShape)
            ) {
                Text(
                    text = if (total == 1) "1 asignación" else "$total asignaciones",
                    color = Color.Black, // el fondo beige es fijo: el texto también, así se lee en modo oscuro
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }

        // Las tres tarjetas ocupan todo el ancho de la lista y entre ellas hay `separacion`
        // (16 dp en un celular en vertical). Con letra de 12 sp, "EN DESARROLLO" (el título más
        // largo) mide unos 96 dp: con más separación ya no cabría en una línea.
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
