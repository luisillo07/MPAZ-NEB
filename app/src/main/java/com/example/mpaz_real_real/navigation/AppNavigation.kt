package com.example.mpaz_real_real.navigation
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mpaz_real_real.ui.screen.CargaScreen
import com.example.mpaz_real_real.ui.screen.LoginScreen
import com.example.mpaz_real_real.ui.screen.PerfilScreen
import com.example.mpaz_real_real.viewmodel.CargaViewModel
import com.example.mpaz_real_real.viewmodel.LoginViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val cargaViewModel: CargaViewModel = viewModel()
    val loginViewModel: LoginViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = Screen.Carga.route
    ) {
        composable(Screen.Carga.route) {
            CargaScreen(navController = navController, viewModel = cargaViewModel)
        }
        composable(Screen.Perfil.route) {
            PerfilScreen(navController = navController, viewModel = loginViewModel)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController = navController, viewModel = loginViewModel)
        }
    }
}