package com.example.mpaz_real_real.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mpaz_real_real.R
import com.example.mpaz_real_real.navigation.Screen
import com.example.mpaz_real_real.ui.theme.BotonLogin
import com.example.mpaz_real_real.ui.theme.FondoLogin
import com.example.mpaz_real_real.ui.utils.obtenerWindowSizeClass
import com.example.mpaz_real_real.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun PerfilScreen(
    navController: NavController,
    viewModel: LoginViewModel
) {

    val windowSizeClass = obtenerWindowSizeClass()

    val margenHorizontal = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 24.dp
        WindowWidthSizeClass.Medium -> 32.dp
        WindowWidthSizeClass.Expanded -> 48.dp
        else -> 24.dp
    }

    val anchoMaximo = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 600.dp
        WindowWidthSizeClass.Medium -> 600.dp
        WindowWidthSizeClass.Expanded -> 640.dp
        else -> 600.dp
    }

    Scaffold(
        containerColor = FondoLogin,
        contentColor = Color.Black,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .wrapContentWidth(Alignment.CenterHorizontally)
                .widthIn(max = anchoMaximo)
                .fillMaxWidth()
                .padding(
                    horizontal = margenHorizontal,
                    vertical = 24.dp
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Image(
                    painter = painterResource(id = R.drawable.logo_escuela),
                    contentDescription = "Logo de la Escuela Escritora Marcela Paz",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
            }

            item {
                Text(
                    text = "Elige el perfil de usuario",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Button(
                    onClick = {
                        viewModel.seleccionarTipoUsuario("docente")
                        navController.navigate(Screen.Login.route)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BotonLogin,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(2.dp, Color.Black),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                ) {
                    Text(
                        text = "Administración pedagógica",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            item {
                Button(
                    onClick = {
                        viewModel.seleccionarTipoUsuario("asistente")
                        navController.navigate(Screen.Login.route)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BotonLogin,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(2.dp, Color.Black),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                ) {
                    Text(
                        text = "Asistente de aula",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            item {
                Button(
                    onClick = {
                        viewModel.seleccionarTipoUsuario("estudiante")
                        navController.navigate(Screen.Login.route)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BotonLogin,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(2.dp, Color.Black),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                ) {
                    Text(
                        text = "Estudiante",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}