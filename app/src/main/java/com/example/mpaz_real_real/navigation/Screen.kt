package com.example.mpaz_real_real.navigation

sealed class Screen(val route: String) {
    data object Carga : Screen("carga_page")
    data object Perfil : Screen("perfil_page")
    data object Login : Screen("login_page")
    data object MisActividades : Screen("mis_actividades_page")
    data object Instrucciones : Screen("instrucciones_page")
    data object Pregunta : Screen("pregunta_page")
    data object QuedanIntentos : Screen("quedan_intentos_page")
    data object Retroalimentacion : Screen("retroalimentacion_page")
    data object Catalogo : Screen("catalogo_page")
    data object Construccion : Screen("construccion_page")
    data object Formulario : Screen("formulario_page")
    data object Revisar : Screen("revisar_page")
}