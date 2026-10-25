package com.example.mpaz_real_real.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
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
import com.example.mpaz_real_real.ui.components.CampoFormulario
import com.example.mpaz_real_real.ui.components.SelectorFiltro
import com.example.mpaz_real_real.ui.theme.GrisBarra
import com.example.mpaz_real_real.viewmodel.CatalogoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioActividadScreen(
    navController: NavController,
    viewModel: CatalogoViewModel
) {
    val estado by viewModel.state.collectAsState()
    val formulario = estado.formulario

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Administración pedagógica", style = MaterialTheme.typography.bodySmall)
                        Text(
                            text = if (formulario.esNuevo) "Creando actividad" else "Editando actividad",
                            style = MaterialTheme.typography.titleLarge
                        )
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
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CampoFormulario(
                etiqueta = "Identificador",
                valor = formulario.id,
                onValorChange = viewModel::onFormIdChange,
                error = formulario.errores.id,
                soloLectura = !formulario.esNuevo
            )
            CampoFormulario(
                etiqueta = "Nombre",
                valor = formulario.nombre,
                onValorChange = viewModel::onFormNombreChange
            )
            SelectorFiltro(
                "Módulo", formulario.modulo,
                estado.opcionesModulo.filter { it != "Todos" },
                viewModel::onFormModuloChange, Modifier.fillMaxWidth()
            )
            SelectorFiltro(
                "Curso", formulario.curso,
                estado.opcionesCurso.filter { it != "Todos" },
                viewModel::onFormCursoChange, Modifier.fillMaxWidth()
            )
            SelectorFiltro(
                "Contenido", formulario.contenido,
                estado.opcionesContenido.filter { it != "Todos" },
                viewModel::onFormContenidoChange, Modifier.fillMaxWidth()
            )
            CampoFormulario(
                etiqueta = "Objetivo pedagógico",
                valor = formulario.objetivo,
                onValorChange = viewModel::onFormObjetivoChange,
                lineasMinimas = 4
            )
            SelectorFiltro(
                "Dificultad", formulario.dificultad,
                estado.opcionesDificultad.filter { it != "Todos" },
                viewModel::onFormDificultadChange, Modifier.fillMaxWidth()
            )
            CampoFormulario(
                etiqueta = "Instrucciones",
                valor = formulario.instrucciones,
                onValorChange = viewModel::onFormInstruccionesChange,
                lineasMinimas = 4
            )
            CampoFormulario(
                etiqueta = "Cantidad de preguntas",
                valor = formulario.cantidadPreguntas,
                onValorChange = viewModel::onFormCantidadPreguntasChange,
                error = formulario.errores.cantidadPreguntas,
                numerico = true
            )
            CampoFormulario(
                etiqueta = "Intentos permitidos",
                valor = formulario.intentosPermitidos,
                onValorChange = viewModel::onFormIntentosPermitidosChange,
                error = formulario.errores.intentosPermitidos,
                numerico = true
            )
            SelectorFiltro(
                "Estado", formulario.estado, estado.opcionesEstado,
                viewModel::onFormEstadoChange, Modifier.fillMaxWidth()
            )
            SelectorFiltro(
                "Fecha", formulario.fecha, estado.opcionesFecha,
                viewModel::onFormFechaChange, Modifier.fillMaxWidth()
            )
            BotonCatalogo(
                texto = if (formulario.esNuevo) "Crear" else "Guardar",
                onClick = {
                    if (viewModel.onGuardarFormulario()) {
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}