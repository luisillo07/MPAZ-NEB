package com.example.mpaz_real_real

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mpaz_real_real.navigation.AppNavigation
import com.example.mpaz_real_real.ui.theme.Mpaz_real_realTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Mpaz_real_realTheme {
                AppNavigation()
            }
        }
    }
}