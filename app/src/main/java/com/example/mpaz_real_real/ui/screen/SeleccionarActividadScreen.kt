package com.example.mpaz_real_real.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mpaz_real_real.navigation.Screen
import com.example.mpaz_real_real.ui.components.DialogoAviso
import com.example.mpaz_real_real.ui.components.EncabezadoAsistente
import com.example.mpaz_real_real.ui.components.TarjetaActividadSeleccion
import com.example.mpaz_real_real.ui.components.TarjetaFiltro
import com.example.mpaz_real_real.ui.components.TarjetaVacia
import com.example.mpaz_real_real.ui.utils.colorFondoNoDisponible
import com.example.mpaz_real_real.ui.utils.obtenerMedidasAsistente
import com.example.mpaz_real_real.viewmodel.CatalogoViewModel

// Ancho de cada tarjeta de filtro. Con este ancho, en un celular en vertical se ven dos tarjetas
// completas y la tercera asoma cortada en el borde de la pantalla, avisando que hay más.
private val ANCHO_FILTRO = 150.dp

@Composable
fun SeleccionarActividadScreen(
    navController: NavController,
    viewModel: CatalogoViewModel
) {
    val estado by viewModel.state.collectAsState()
    val asistente = estado.asistente
    val visibles = estado.actividadesParaAsignar

    // Márgenes según el tamaño de la pantalla (Compacta, Mediana o Expandida).
    val medidas = obtenerMedidasAsistente()

    // Código de la actividad desactivada que se tocó (null = no hay aviso). Si no es null se
    // muestra el aviso de "Actividad no disponible". rememberSaveable (cap. 58 del profe) hace
    // que el aviso no desaparezca si se gira el celular.
    var actividadNoDisponible by rememberSaveable { mutableStateOf<String?>(null) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Parte fija de arriba: el mismo encabezado que las otras pantallas del asistente
            // (sin botón: para volver se usa la barra de navegación del celular).
            EncabezadoAsistente(titulo = "Seleccionar actividad")

            // Parte de abajo: los filtros y la lista se desplazan juntos. Esta lista NO lleva
            // margen a los lados, porque el carrusel de filtros tiene que llegar hasta el borde
            // de la pantalla; cada elemento se pone su propio margen.
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { Spacer(modifier = Modifier.height(4.dp)) }

                // Los filtros se aplican al instante: al elegir una opción la lista cambia. Las
                // tarjetas son altas, salvo en una pantalla muy baja (celular en horizontal),
                // donde quitarían demasiado espacio a la lista.
                item {
                    if (medidas.expandida) {
                        // Pantalla ancha (tableta o celular en horizontal): caben las cuatro
                        // tarjetas, así que se reparten el ancho y no hace falta deslizar.
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = medidas.margen),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            TarjetaFiltro("Módulo", asistente.modulo, estado.opcionesModuloAsistente, viewModel::onAsistenteModuloChange, Modifier.weight(1f), grande = !medidas.alturaCompacta)
                            TarjetaFiltro("Curso", asistente.curso, estado.opcionesCursoAsistente, viewModel::onAsistenteCursoChange, Modifier.weight(1f), grande = !medidas.alturaCompacta)
                            TarjetaFiltro("Contenido", asistente.contenido, estado.opcionesContenidoAsistente, viewModel::onAsistenteContenidoChange, Modifier.weight(1f), grande = !medidas.alturaCompacta)
                            TarjetaFiltro("Dificultad", asistente.dificultad, estado.opcionesDificultadAsistente, viewModel::onAsistenteDificultadChange, Modifier.weight(1f), grande = !medidas.alturaCompacta)
                        }
                    } else {
                        // Filtros en fila: se deslizan hacia la izquierda para ver los que no
                        // caben. El margen va dentro de la fila (contentPadding) para que al
                        // deslizar las tarjetas salgan por el borde de la pantalla.
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = medidas.margen),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                TarjetaFiltro("Módulo", asistente.modulo, estado.opcionesModuloAsistente, viewModel::onAsistenteModuloChange, Modifier.width(ANCHO_FILTRO), grande = !medidas.alturaCompacta)
                            }
                            item {
                                TarjetaFiltro("Curso", asistente.curso, estado.opcionesCursoAsistente, viewModel::onAsistenteCursoChange, Modifier.width(ANCHO_FILTRO), grande = !medidas.alturaCompacta)
                            }
                            item {
                                TarjetaFiltro("Contenido", asistente.contenido, estado.opcionesContenidoAsistente, viewModel::onAsistenteContenidoChange, Modifier.width(ANCHO_FILTRO), grande = !medidas.alturaCompacta)
                            }
                            item {
                                TarjetaFiltro("Dificultad", asistente.dificultad, estado.opcionesDificultadAsistente, viewModel::onAsistenteDificultadChange, Modifier.width(ANCHO_FILTRO), grande = !medidas.alturaCompacta)
                            }
                        }
                    }
                }

                if (visibles.isNotEmpty()) {
                    items(visibles) { actividad ->
                        Box(modifier = Modifier.padding(horizontal = medidas.margen)) {
                            TarjetaActividadSeleccion(
                                actividad = actividad,
                                onClick = {
                                    if (actividad.estado == "disponible") {
                                        viewModel.onSeleccionarActividad(actividad)
                                        navController.navigate(Screen.AsignarActividad.route)
                                    } else {
                                        // No se puede asignar: se avisa en vez de avanzar.
                                        actividadNoDisponible = actividad.codigo
                                    }
                                }
                            )
                        }
                    }
                } else {
                    item {
                        Box(modifier = Modifier.padding(horizontal = medidas.margen)) {
                            TarjetaVacia(
                                titulo = "No hay actividades",
                                mensaje = "Ninguna actividad cumple con los filtros elegidos.",
                                textoBoton = "Quitar filtros",
                                onBoton = viewModel::reiniciarFiltrosAsistente
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(4.dp)) }
            }
        }
    }

    // Aviso con el color de la materia (el mismo tono apagado de la tarjeta desactivada).
    val tocada = estado.actividadPorCodigo(actividadNoDisponible ?: "")
    if (tocada != null) {
        DialogoAviso(
            titulo = "Actividad no disponible",
            mensaje = "«${tocada.nombre}» está desactivada. Pide a un docente que la active.",
            fondo = colorFondoNoDisponible(tocada.modulo),
            onCerrar = { actividadNoDisponible = null }
        )
    }
}
