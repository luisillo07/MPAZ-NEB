package com.example.mpaz_real_real.navigation

sealed class Screen(val route: String) {
    data object MisActividades : Screen("mis_actividades_page")
    data object Instrucciones : Screen("instrucciones_page")
    data object Pregunta : Screen("pregunta_page")
    data object QuedanIntentos : Screen("quedan_intentos_page")
    data object Retroalimentacion : Screen("retroalimentacion_page")
}