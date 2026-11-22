package com.example.mpaz_real_real.ui.utils

import androidx.compose.ui.graphics.Color
import com.example.mpaz_real_real.ui.theme.Beige
import com.example.mpaz_real_real.ui.theme.OcreEstado
import com.example.mpaz_real_real.ui.theme.OcreOscuro
import com.example.mpaz_real_real.ui.theme.RojoAviso
import com.example.mpaz_real_real.ui.theme.RosaFondo
import com.example.mpaz_real_real.ui.theme.VerdeEstado
import com.example.mpaz_real_real.ui.theme.VerdeFondo

// El estado de una asignación se guarda en minúsculas ("pendiente", "en desarrollo",
// "finalizada"). Estas tres funciones lo traducen a lo que se ve en pantalla: el texto con
// mayúscula inicial, el color del texto (y del puntito) y el color de fondo de la etiqueta.
// Siguen la misma idea que ColoresModulo.kt, que hace lo mismo para Lectura y Matemática.

fun textoEstado(estado: String): String = when (estado) {
    "pendiente" -> "Pendiente"
    "en desarrollo" -> "En desarrollo"
    "finalizada" -> "Finalizada"
    else -> estado
}

fun colorTextoEstado(estado: String): Color = when (estado) {
    "pendiente" -> RojoAviso
    "en desarrollo" -> OcreEstado
    "finalizada" -> VerdeEstado
    else -> Color.Gray
}

// Color del número de las tarjetas del resumen: tonos oscuros, para que se lean sobre el fondo
// pastel de la tarjeta (colorFondoEstado). Son fijos, iguales en modo claro y oscuro.
fun colorNumeroResumen(estado: String): Color = when (estado) {
    "pendiente" -> RojoAviso
    "en desarrollo" -> OcreOscuro
    "finalizada" -> VerdeEstado
    else -> Color.Gray
}

fun colorFondoEstado(estado: String): Color = when (estado) {
    "pendiente" -> RosaFondo
    "en desarrollo" -> Beige
    "finalizada" -> VerdeFondo
    else -> Color.White
}
