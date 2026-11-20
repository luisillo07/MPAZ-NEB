package com.example.mpaz_real_real.ui.screen
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mpaz_real_real.R
import com.example.mpaz_real_real.navigation.Screen
import com.example.mpaz_real_real.ui.theme.Mpaz_real_realTheme
import com.example.mpaz_real_real.viewmodel.CargaViewModel
@Composable
fun CargaScreen(
    navController: NavController,
    viewModel: CargaViewModel
) {
    val cargando by viewModel.cargando.collectAsState()
    if (!cargando) {
        LaunchedEffect(Unit) {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Carga.route) { inclusive = true }
            }
        }
    }
    CargaContenido(cargando = cargando)
}
@Composable
fun CargaContenido(cargando: Boolean) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.logo_escuela),
                contentDescription = "Logo Escuela Escritora Marcela Paz",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Fit
            )
            if (cargando) {
                Spacer(modifier = Modifier.height(24.dp))
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Cargando...",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun CargaContenidoPreview() {
    Mpaz_real_realTheme {
        CargaContenido(cargando = true)
    }
}