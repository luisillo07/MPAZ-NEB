package com.example.mpaz_real_real.ui.utils

import androidx.activity.compose.LocalActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun obtenerWindowSizeClass(): WindowSizeClass {
    return calculateWindowSizeClass(LocalActivity.current as android.app.Activity)
}

// Medidas de las pantallas del asistente según el ancho de la ventana (Compacta, Mediana o
// Expandida, como en las demás pantallas):
// - margen: espacio entre el borde de la pantalla y el contenido.
// - separacion: espacio entre las tarjetas pequeñas que van en fila (resumen y resultados).
// - expandida: true en tabletas o celulares en horizontal, donde caben cosas en una sola fila.
// - alturaCompacta: true cuando la pantalla es muy baja (un celular girado). Ahí conviene no
//   dejar botones fijos que le quiten espacio a la lista.
data class MedidasAsistente(
    val margen: Dp,
    val separacion: Dp,
    val expandida: Boolean,
    val alturaCompacta: Boolean
)

@Composable
fun obtenerMedidasAsistente(): MedidasAsistente {
    val windowSizeClass = obtenerWindowSizeClass()
    val alturaCompacta = windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact

    return when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> MedidasAsistente(16.dp, 16.dp, false, alturaCompacta)
        WindowWidthSizeClass.Medium -> MedidasAsistente(24.dp, 20.dp, false, alturaCompacta)
        else -> MedidasAsistente(40.dp, 24.dp, true, alturaCompacta)
    }
}
