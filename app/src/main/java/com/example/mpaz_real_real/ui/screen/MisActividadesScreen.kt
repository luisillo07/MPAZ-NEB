package com.example.mpaz_real_real.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mpaz_real_real.data.local.entity.ResultadoEntity
import com.example.mpaz_real_real.model.Actividad
import com.example.mpaz_real_real.navigation.Screen
import com.example.mpaz_real_real.repository.actividadesDePrueba
import com.example.mpaz_real_real.ui.components.BotonActividad
import com.example.mpaz_real_real.ui.components.ContenedorTexto
import com.example.mpaz_real_real.ui.components.EncabezadoActividad
import com.example.mpaz_real_real.ui.theme.Mpaz_real_realTheme
import com.example.mpaz_real_real.ui.theme.RosaFondo
import com.example.mpaz_real_real.ui.utils.obtenerWindowSizeClass
import com.example.mpaz_real_real.viewmodel.ActividadViewModel

@Composable
fun MisActividadesScreen(
    navController: NavController,
    viewModel: ActividadViewModel
) {
    val resultados by viewModel.resultados.collectAsState()
    val windowSizeClass = obtenerWindowSizeClass()
    val onEntrar: (String) -> Unit = { codigo ->
        viewModel.cargarActividad(codigo)
        navController.navigate(Screen.Instrucciones.route)
    }
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> MisActividadesCompacta(
            actividades = actividadesDePrueba,
            resultados = resultados,
            onEntrar = onEntrar
        )
        WindowWidthSizeClass.Medium -> MisActividadesMediana(
            actividades = actividadesDePrueba,
            resultados = resultados,
            onEntrar = onEntrar
        )
        WindowWidthSizeClass.Expanded -> MisActividadesExpandida(
            actividades = actividadesDePrueba,
            resultados = resultados,
            onEntrar = onEntrar
        )
    }
}

@Composable
fun MisActividadesCompacta(
    actividades: List<Actividad>,
    resultados: List<ResultadoEntity>,
    onEntrar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            EncabezadoActividad(
                modulo = "Estudiante",
                curso = "2.º básico",
                nombre = "Mis actividades",
                mostrarSalir = true
            )
        },
        containerColor = RosaFondo,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            item {
                Text(text = "Estas son tus actividades.")
            }
            items(items = actividades, key = { actividad -> actividad.codigo }) { actividad ->
                val usados = resultados.count { it.codigoActividad == actividad.codigo }
                ContenedorTexto {
                    Text(
                        text = actividad.nombre,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "${actividad.modulo} · ${actividad.curso}")
                    Text(
                        text = textoProgreso(usados, actividad.intentosPermitidos),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    if (usados < actividad.intentosPermitidos) {
                        BotonActividad(
                            texto = if (usados == 0) "Comenzar" else "Continuar",
                            onClick = { onEntrar(actividad.codigo) }
                        )
                    } else {
                        Text(text = "Ya usaste todos los intentos.")
                    }
                }
            }
        }
    }
}

@Composable
fun MisActividadesMediana(
    actividades: List<Actividad>,
    resultados: List<ResultadoEntity>,
    onEntrar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            EncabezadoActividad(
                modulo = "Estudiante",
                curso = "2.º básico",
                nombre = "Mis actividades",
                mostrarSalir = true
            )
        },
        containerColor = RosaFondo,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            item {
                Text(text = "Estas son tus actividades.")
            }
            items(items = actividades, key = { actividad -> actividad.codigo }) { actividad ->
                val usados = resultados.count { it.codigoActividad == actividad.codigo }
                ContenedorTexto {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(2f)) {
                            Text(
                                text = actividad.nombre,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(text = "${actividad.modulo} · ${actividad.curso}")
                            Text(
                                text = textoProgreso(usados, actividad.intentosPermitidos),
                                fontWeight = FontWeight.Bold
                            )
                        }
                        if (usados < actividad.intentosPermitidos) {
                            BotonActividad(
                                texto = if (usados == 0) "Comenzar" else "Continuar",
                                onClick = { onEntrar(actividad.codigo) },
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            Text(
                                text = "Ya usaste todos los intentos.",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MisActividadesExpandida(
    actividades: List<Actividad>,
    resultados: List<ResultadoEntity>,
    onEntrar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            EncabezadoActividad(
                modulo = "Estudiante",
                curso = "2.º básico",
                nombre = "Mis actividades",
                mostrarSalir = true
            )
        },
        containerColor = RosaFondo,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(32.dp)
        ) {
            item {
                Text(
                    text = "Estas son tus actividades.",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            items(items = actividades, key = { actividad -> actividad.codigo }) { actividad ->
                val usados = resultados.count { it.codigoActividad == actividad.codigo }
                ContenedorTexto {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(2f)) {
                            Text(
                                text = actividad.nombre,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${actividad.modulo} · ${actividad.curso}",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = textoProgreso(usados, actividad.intentosPermitidos),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        if (usados < actividad.intentosPermitidos) {
                            BotonActividad(
                                texto = if (usados == 0) "Comenzar" else "Continuar",
                                onClick = { onEntrar(actividad.codigo) },
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            Text(
                                text = "Ya usaste todos los intentos.",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun textoProgreso(usados: Int, permitidos: Int): String {
    val progreso = when {
        usados == 0 -> "Pendiente"
        usados < permitidos -> "En desarrollo"
        else -> "Finalizada"
    }
    return "$progreso · Intentos: $usados de $permitidos"
}

@Preview(name = "Compact", widthDp = 360, heightDp = 800)
@Composable
fun MisActividadesCompactaPreview() {
    Mpaz_real_realTheme {
        MisActividadesCompacta(
            actividades = actividadesDePrueba,
            resultados = emptyList(),
            onEntrar = {}
        )
    }
}

@Preview(name = "Medium", widthDp = 700, heightDp = 1000)
@Composable
fun MisActividadesMedianaPreview() {
    Mpaz_real_realTheme {
        MisActividadesMediana(
            actividades = actividadesDePrueba,
            resultados = emptyList(),
            onEntrar = {}
        )
    }
}

@Preview(name = "Expanded", widthDp = 1000, heightDp = 700)
@Composable
fun MisActividadesExpandidaPreview() {
    Mpaz_real_realTheme {
        MisActividadesExpandida(
            actividades = actividadesDePrueba,
            resultados = emptyList(),
            onEntrar = {}
        )
    }
}