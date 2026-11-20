package com.example.mpaz_real_real.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mpaz_real_real.ui.components.BotonCatalogo
import com.example.mpaz_real_real.ui.components.CajaSoloLectura
import com.example.mpaz_real_real.ui.components.SelectorFiltro
import com.example.mpaz_real_real.ui.theme.GrisBarra
import com.example.mpaz_real_real.viewmodel.CatalogoViewModel

import com.example.mpaz_real_real.navigation.Screen
import com.example.mpaz_real_real.ui.components.CampoFormulario

import com.example.mpaz_real_real.ui.components.DialogoMensaje
import com.example.mpaz_real_real.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsignarActividadScreen(
    navController: NavController,
    viewModel: CatalogoViewModel,
    loginViewModel: LoginViewModel
) {
    val estado by viewModel.state.collectAsState()
    val estadoLogin by loginViewModel.state.collectAsState()
    val asistente = estado.asistente
    val formulario = asistente.formulario
    val actividad = estado.actividadPorCodigo(formulario.actividadCodigo)
    val cursoDificultad = if (actividad != null) "${actividad.curso} · ${actividad.dificultad}" else ""

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Asistente de aula", style = MaterialTheme.typography.bodySmall)
                        Text("Asignar actividad", style = MaterialTheme.typography.titleLarge)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GrisBarra)
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CajaSoloLectura("Actividad asignada", actividad?.nombre ?: "")
            CajaSoloLectura("Curso/dificultad", cursoDificultad)
            CajaSoloLectura("Asistente responsable", estadoLogin.usuarioSesion)
            SelectorFiltro(
                "Estudiante ficticio", formulario.estudiante,
                estado.opcionesEstudiante,
                viewModel::onAsignarEstudianteChange, Modifier.fillMaxWidth(),
                formulario.errores.estudiante
            )
            CampoFormulario(
                etiqueta = "Hora límite (dd/MM/yyyy HH:mm)",
                valor = formulario.fecha,
                onValorChange = viewModel::onAsignarFechaChange,
                modifier = Modifier.fillMaxWidth(),
                error = formulario.errores.fecha
            )
            CampoFormulario(
                etiqueta = "Minutos de duración",
                valor = formulario.minutos,
                onValorChange = viewModel::onAsignarMinutosChange,
                modifier = Modifier.fillMaxWidth(),
                error = formulario.errores.minutos,
                numerico = true
            )
            BotonCatalogo(
                texto = "Asignar",
                onClick = {
                    if (viewModel.onAsignarClick(estadoLogin.usuarioSesion)) {
                        navController.navigate(Screen.Asignaciones.route) {
                            popUpTo(Screen.Asignaciones.route) { inclusive = true }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
    if (asistente.mensajeError != null) {
        DialogoMensaje("Error", asistente.mensajeError, viewModel::onCerrarErrorAsistente)
    }
}