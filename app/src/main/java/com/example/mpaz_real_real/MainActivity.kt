package com.example.mpaz_real_real

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mpaz_real_real.ui.screen.PreguntaScreen
import com.example.mpaz_real_real.ui.theme.Mpaz_real_realTheme
import com.example.mpaz_real_real.viewmodel.ActividadViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import com.example.mpaz_real_real.ui.screen.RetroalimentacionScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Mpaz_real_realTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel: ActividadViewModel = viewModel()
                    val estado by viewModel.estado.collectAsState()
                    LaunchedEffect(Unit){
                        viewModel.cargarActividad("LEC-001")
                    }
                    if (estado.intentosUsados == 0){
                        PreguntaScreen(modifier = Modifier.padding(innerPadding),viewModel = viewModel)
                    }else{
                        RetroalimentacionScreen(modifier = Modifier.padding(innerPadding),viewModel = viewModel)
                    }

                }
            }
        }
    }
}