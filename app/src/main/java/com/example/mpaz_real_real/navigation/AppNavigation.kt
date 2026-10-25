package com.example.mpaz_real_real.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mpaz_real_real.ui.screen.CatalogoScreen
import com.example.mpaz_real_real.ui.screen.EnConstruccionScreen
import com.example.mpaz_real_real.ui.screen.FormularioActividadScreen
import com.example.mpaz_real_real.ui.screen.InicioScreen
import com.example.mpaz_real_real.ui.screen.RevisarActividadScreen
import com.example.mpaz_real_real.viewmodel.CatalogoViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val catalogoViewModel: CatalogoViewModel = viewModel()

    NavHost(navController = navController, startDestination = "inicio") {
        composable("inicio") {
            InicioScreen(
                onAdmin = {
                    catalogoViewModel.reiniciarFiltros()
                    navController.navigate("catalogo")
                },
                onAsistente = { navController.navigate("construccion") }
            )
        }
        composable("catalogo") { CatalogoScreen(navController, catalogoViewModel) }
        composable("construccion") { EnConstruccionScreen(navController) }
        composable("formulario") { FormularioActividadScreen(navController, catalogoViewModel) }
        composable("revisar") { RevisarActividadScreen(navController, catalogoViewModel) }
    }
}