package com.example.mpaz_real_real.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mpaz_real_real.ui.components.BotonCatalogo
import com.example.mpaz_real_real.ui.components.CampoFormulario
import com.example.mpaz_real_real.ui.components.FilaAlternativa
import com.example.mpaz_real_real.ui.components.MensajeError
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
    val errorPregunta = formulario.errores.nuevaPregunta

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
                valor = formulario.codigo,
                onValorChange = viewModel::onFormCodigoChange,
                error = formulario.errores.codigo,
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
                viewModel::onFormModuloChange, Modifier.fillMaxWidth(),
                formulario.errores.modulo
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

            Text("Preguntas", style = MaterialTheme.typography.titleLarge)

            for (i in 0..formulario.preguntas.size - 1) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(2.dp, Color.Black)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "${i + 1}. ${formulario.preguntas[i].enunciado}",
                            modifier = Modifier.weight(1f)
                        )
                        BotonCatalogo("Quitar", { viewModel.onQuitarPregunta(i) })
                    }
                }
            }

            CampoFormulario(
                etiqueta = "Pregunta",
                valor = formulario.nuevaPregunta,
                onValorChange = viewModel::onNuevaPreguntaChange,
                lineasMinimas = 2
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Alternativas")
                Text("Correcta")
            }
            FilaAlternativa("A", formulario.nuevaAlternativaA, viewModel::onNuevaAlternativaAChange, formulario.nuevaCorrecta == 0) {
                viewModel.onNuevaCorrectaChange(0)
            }
            FilaAlternativa("B", formulario.nuevaAlternativaB, viewModel::onNuevaAlternativaBChange, formulario.nuevaCorrecta == 1) {
                viewModel.onNuevaCorrectaChange(1)
            }
            FilaAlternativa("C", formulario.nuevaAlternativaC, viewModel::onNuevaAlternativaCChange, formulario.nuevaCorrecta == 2) {
                viewModel.onNuevaCorrectaChange(2)
            }
            if (errorPregunta != null) {
                MensajeError(errorPregunta)
            }
            BotonCatalogo("+ Agregar pregunta", viewModel::onAgregarPregunta, Modifier.fillMaxWidth())

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