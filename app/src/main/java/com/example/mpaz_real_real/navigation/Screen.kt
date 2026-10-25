package com.example.mpaz_real_real.navigation


sealed class Screen(val route: String) {
    data object Carga : Screen("carga_page")
    data object Perfil : Screen("perfil_page")
    data object Login : Screen("login_page")
}