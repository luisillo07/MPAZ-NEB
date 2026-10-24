package com.example.mpaz_real_real.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mpaz_real_real.ui.theme.Mpaz_real_realTheme
import com.example.mpaz_real_real.ui.theme.RojoCabecera
import com.example.mpaz_real_real.ui.theme.SalmonBoton
import androidx.compose.foundation.border

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncabezadoActividad(
    modulo: String,
    curso: String,
    nombre: String,
    mostrarSalir: Boolean = false
) {
    TopAppBar(
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ){
                Text(text = "$modulo · $curso", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = nombre,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        actions = {
            if (mostrarSalir){
                Button(
                    onClick = {}, // TEMPORAL: se conecta al login de Luis al unir las ramas
                    colors = ButtonDefaults.buttonColors(containerColor = SalmonBoton, contentColor = Color.Black),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.border(2.dp, Color.Black, MaterialTheme.shapes.medium)
                ) {
                    Text(text = "Salir sesión",fontWeight = FontWeight.Bold)
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = RojoCabecera,
            titleContentColor = Color.Black
        )
    )
}

@Preview(showBackground = true)
@Composable
fun EncabezadoActividadPreview(){
    Mpaz_real_realTheme{
        EncabezadoActividad(
            modulo = "Lectura",
            curso = "2.º básico",
            nombre = "El perro Tito",
            mostrarSalir = true
        )
    }
}