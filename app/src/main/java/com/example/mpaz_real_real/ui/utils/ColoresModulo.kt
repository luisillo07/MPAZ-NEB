package com.example.mpaz_real_real.ui.utils

import androidx.compose.ui.graphics.Color
import com.example.mpaz_real_real.ui.theme.AzulBoton
import com.example.mpaz_real_real.ui.theme.AzulCabecera
import com.example.mpaz_real_real.ui.theme.CelesteFondo
import com.example.mpaz_real_real.ui.theme.GrisBarra
import com.example.mpaz_real_real.ui.theme.RojoCabecera
import com.example.mpaz_real_real.ui.theme.RosaFondo
import com.example.mpaz_real_real.ui.theme.SalmonBoton

fun colorCabecera(modulo: String): Color {
    return when (modulo) {
        "Matemática" -> AzulCabecera
        "Lectura" -> RojoCabecera
        else -> GrisBarra
    }
}

fun colorFondo(modulo: String): Color {
    return if (modulo == "Matemática") CelesteFondo else RosaFondo
}

fun colorBoton(modulo: String): Color {
    return if (modulo == "Matemática") AzulBoton else SalmonBoton
}