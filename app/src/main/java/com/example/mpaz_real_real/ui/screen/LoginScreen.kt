package com.example.mpaz_real_real.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mpaz_real_real.viewmodel.LoginViewModel
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.input.PasswordVisualTransformation

import androidx.compose.foundation.layout.height
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.graphics.Color
import com.example.mpaz_real_real.ui.theme.FondoLogin
import com.example.mpaz_real_real.ui.theme.BotonLogin
import com.example.mpaz_real_real.ui.theme.CampoLogin
import com.example.mpaz_real_real.ui.theme.CampoErrorLogin
import com.example.mpaz_real_real.ui.theme.TextoErrorLogin
import androidx.compose.foundation.background
import com.example.mpaz_real_real.ui.theme.CabeceraLogin

import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.Alignment
import com.example.mpaz_real_real.ui.utils.obtenerWindowSizeClass
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import com.example.mpaz_real_real.navigation.Screen

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel
) {

    val estado by viewModel.state.collectAsState()

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

    val snackbarHostState = remember { SnackbarHostState() }

    if (estado.ingresoCorrecto) {
        LaunchedEffect(Unit) {
            val destino = when (estado.tipoUsuario) {
                "estudiante" -> Screen.MisActividades.route
                "docente" -> Screen.Catalogo.route
                else -> Screen.Asignaciones.route
            }
            navController.navigate(destino) {
                popUpTo(Screen.Perfil.route)
            }
        }
    }

    val coloresCampos = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = CampoLogin,
        unfocusedContainerColor = CampoLogin,
        errorContainerColor = CampoErrorLogin,

        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black,
        errorTextColor = Color.Black,

        focusedBorderColor = Color.Black,
        unfocusedBorderColor = Color.Black,
        errorBorderColor = Color.Black,

        focusedLabelColor = Color.Black,
        unfocusedLabelColor = Color.Black,
        errorLabelColor = TextoErrorLogin,

        cursorColor = Color.Black,
        errorCursorColor = TextoErrorLogin,

        focusedLeadingIconColor = Color.Black,
        unfocusedLeadingIconColor = Color.Black,
        errorLeadingIconColor = TextoErrorLogin
    )

    Scaffold(
        containerColor = FondoLogin,
        contentColor = Color.Black,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
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
            val estudiante = estado.tipoUsuario == "estudiante"

            val nombrePerfil = when (estado.tipoUsuario) {
                "docente" -> "Docente / UTP"
                "asistente" -> "Asistente de aula"
                "estudiante" -> "Estudiante"
                else -> ""
            }

            item {
                Text(
                    text = nombrePerfil,
                    color = Color.Black,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CabeceraLogin)
                        .padding(vertical = 20.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = estado.usuario,
                    onValueChange = viewModel::usuarioChanged,
                    label = {
                        Text(text = "Usuario")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null
                        )
                    },
                    isError = estado.errores.usuario != null,
                    supportingText = {
                        estado.errores.usuario?.let {
                            Text(
                                text = it,
                                color = TextoErrorLogin
                            )
                        }
                    },
                    singleLine = true,
                    colors = coloresCampos,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = estado.contrasenia,
                    onValueChange = viewModel::contraseniaChanged,
                    label = {
                        Text(text = "Contraseña")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = estado.errores.contrasenia != null,
                    supportingText = {
                        estado.errores.contrasenia?.let {
                            Text(
                                text = it,
                                color = TextoErrorLogin
                            )
                        }
                    },
                    singleLine = true,
                    colors = coloresCampos,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (estudiante) {
                item {
                    OutlinedTextField(
                        value = estado.codigoTemporal,
                        onValueChange = viewModel::codigoTemporalChanged,
                        label = {
                            Text(text = "Código temporal (opcional)")
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null
                            )
                        },
                        isError = estado.errores.codigoTemporal != null,
                        supportingText = {
                            estado.errores.codigoTemporal?.let {
                                Text(
                                    text = it,
                                    color = TextoErrorLogin
                                )
                            }
                        },
                        singleLine = true,
                        colors = coloresCampos,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            item {
                Button(
                    onClick = {
                        viewModel.ingresar()
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
                        text = "Ingresar",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            item {
                Button(
                    onClick = {
                        navController.popBackStack()
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
                        text = "Volver",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}