package com.example.mpaz_real_real.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mpaz_real_real.ui.screen.InstruccionesScreen
import com.example.mpaz_real_real.ui.screen.PreguntaScreen
import com.example.mpaz_real_real.ui.screen.RetroalimentacionScreen
import com.example.mpaz_real_real.viewmodel.ActividadViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val actividadViewModel: ActividadViewModel = viewModel()

    LaunchedEffect(Unit) {
        actividadViewModel.cargarActividad("LEC-001")
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Instrucciones.route
    ) {
        composable(Screen.Instrucciones.route) {
            InstruccionesScreen(navController = navController, viewModel = actividadViewModel)
        }
        composable(Screen.Pregunta.route) {
            PreguntaScreen(viewModel = actividadViewModel)
        }
        composable(Screen.Retroalimentacion.route) {
            RetroalimentacionScreen(viewModel = actividadViewModel)
        }
    }
}