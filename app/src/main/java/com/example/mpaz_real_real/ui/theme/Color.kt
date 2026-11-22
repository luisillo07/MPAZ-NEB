package com.example.mpaz_real_real.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val FondoLogin = Color(0xFFFFFFFF)
val BotonLogin = Color(0xFFF7E4B9)
val CabeceraLogin = Color(0xFF9E9E9E)
val CampoLogin = Color(0xFFEEEEEE)
val CampoErrorLogin = Color(0xFFE6CAC6)
val TextoErrorLogin = Color(0xFF904136)

val RojoCabecera = Color(0xFFCE5D4D)
val SalmonBoton = Color(0xFFE29E94)
val RosaFondo = Color(0xFFF5DFDB)
val RojoAviso = Color(0xFF904136)

val Beige = Color(0xFFF8E3B9)

val AzulCabecera = Color(0xFF4D7FCE)
val CelesteFondo = Color(0xFFDBE6F5)
val AzulBoton = Color(0xFF94B4E2)

val GrisBarra = Color(0xFF9E9E9E)

val RosadoError = Color(0xFFE8CBC6)

// Colores de estado de las asignaciones (pantalla del asistente).
// El rojo (RojoAviso) y los fondos rosa (RosaFondo) y beige (Beige) ya existían arriba.
val OcreEstado = Color(0xFF9A6A14)   // texto y punto de "En desarrollo"
val VerdeEstado = Color(0xFF2F6B3C)  // texto y punto de "Finalizada"
val VerdeFondo = Color(0xFFDDEFDD)   // fondo de la etiqueta "Finalizada"

// Versiones más claras de los tres colores de estado, solo para el modo oscuro: los originales
// son oscuros y no se distinguen sobre las tarjetas oscuras del resumen.
val RojoEstadoClaro = Color(0xFFF2948A)
val OcreEstadoClaro = Color(0xFFE9B94F)
val VerdeEstadoClaro = Color(0xFF8FD69A)

// Gris más claro para el fondo de las tres tarjetas del resumen, solo en modo claro
// (el gris normal del tema es #E1E2ED; este es un poco más claro).
val GrisClaroResumen = Color(0xFFEFF0F6)

// Pantalla "Seleccionar actividad": verde brillante del recuadro "Disponible" (con letra negra,
// contraste 12 : 1). Las tarjetas disponibles usan los fondos suaves de cada materia (RosaFondo
// y CelesteFondo); las no disponibles usan estos tonos más oscuros y apagados de los mismos
// colores, con un texto gris oscuro. Todos son fijos (iguales en modo claro y oscuro).
val VerdeDisponible = Color(0xFF4ADE80)
val RojoNoDisponible = Color(0xFFD4A59E)
val AzulNoDisponible = Color(0xFFA3B7D3)
val TextoNoDisponible = Color(0xFF333333)

// Ocre oscuro para el número "En desarrollo" sobre su fondo beige (el OcreEstado normal daba
// solo 3,8 : 1 de contraste; este da 5,5 : 1).
val OcreOscuro = Color(0xFF7A5200)

// Colores brillantes para los números del resumen en modo claro: rojo, amarillo y verde.
val RojoVivo = Color(0xFFF01E2C)
val AmarilloVivo = Color(0xFFF2B600)
val VerdeVivo = Color(0xFF1E9E3A)