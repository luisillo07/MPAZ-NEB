package com.example.mpaz_real_real.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mpaz_real_real.ui.screen.CargaScreen
import com.example.mpaz_real_real.ui.screen.LoginScreen
import com.example.mpaz_real_real.ui.screen.PerfilScreen
import com.example.mpaz_real_real.ui.screen.MisActividadesScreen
import com.example.mpaz_real_real.ui.screen.InstruccionesScreen
import com.example.mpaz_real_real.ui.screen.PreguntaScreen
import com.example.mpaz_real_real.ui.screen.QuedanIntentosScreen
import com.example.mpaz_real_real.ui.screen.RetroalimentacionScreen
import com.example.mpaz_real_real.ui.screen.CatalogoScreen
import com.example.mpaz_real_real.ui.screen.EnConstruccionScreen
import com.example.mpaz_real_real.ui.screen.FormularioActividadScreen
import com.example.mpaz_real_real.ui.screen.RevisarActividadScreen
import com.example.mpaz_real_real.ui.screen.AsignacionesScreen
import com.example.mpaz_real_real.ui.screen.ResumenAsignacionScreen
import com.example.mpaz_real_real.ui.screen.SeleccionarActividadScreen
import com.example.mpaz_real_real.ui.screen.AsignarActividadScreen
import com.example.mpaz_real_real.viewmodel.CargaViewModel
import com.example.mpaz_real_real.viewmodel.LoginViewModel
import com.example.mpaz_real_real.viewmodel.ActividadViewModel
import com.example.mpaz_real_real.viewmodel.CatalogoViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val cargaViewModel: CargaViewModel = viewModel()
    val loginViewModel: LoginViewModel = viewModel()
    val actividadViewModel: ActividadViewModel = viewModel()
    val catalogoViewModel: CatalogoViewModel = viewModel()

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
        composable(Screen.Catalogo.route) {
            CatalogoScreen(navController = navController, viewModel = catalogoViewModel)
        }
        composable(Screen.Construccion.route) {
            EnConstruccionScreen(navController = navController)
        }
        composable(Screen.Formulario.route) {
            FormularioActividadScreen(navController = navController, viewModel = catalogoViewModel)
        }
        composable(Screen.Revisar.route) {
            RevisarActividadScreen(navController = navController, viewModel = catalogoViewModel)
        }
        composable(Screen.Asignaciones.route) {
            AsignacionesScreen(navController = navController, viewModel = catalogoViewModel)
        }
        composable(Screen.ResumenAsignacion.route) {
            ResumenAsignacionScreen(navController = navController, viewModel = catalogoViewModel)
        }
        composable(Screen.SeleccionarActividad.route) {
            SeleccionarActividadScreen(navController = navController, viewModel = catalogoViewModel)
        }
        composable(Screen.AsignarActividad.route) {
            AsignarActividadScreen(navController = navController, viewModel = catalogoViewModel)
        }
    }
}