package com.example.mpaz_real_real.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mpaz_real_real.ui.screen.InstruccionesScreen
import com.example.mpaz_real_real.ui.screen.PreguntaScreen
import com.example.mpaz_real_real.ui.screen.RetroalimentacionScreen
import com.example.mpaz_real_real.viewmodel.ActividadViewModel
import com.example.mpaz_real_real.ui.screen.QuedanIntentosScreen

import com.example.mpaz_real_real.ui.screen.MisActividadesScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val actividadViewModel: ActividadViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.MisActividades.route
    ) {
        composable(Screen.MisActividades.route) {
            MisActividadesScreen(navController = navController, viewModel = actividadViewModel)
        }
        composable(Screen.Instrucciones.route) {
            InstruccionesScreen(navController = navController, viewModel = actividadViewModel)
        }
        composable(Screen.Pregunta.route) {
            PreguntaScreen(navController = navController, viewModel = actividadViewModel)
        }
        composable(Screen.QuedanIntentos.route) {
            QuedanIntentosScreen(navController = navController, viewModel = actividadViewModel)
        }
        composable(Screen.Retroalimentacion.route) {
            RetroalimentacionScreen(navController = navController, viewModel = actividadViewModel)
        }
    }
}