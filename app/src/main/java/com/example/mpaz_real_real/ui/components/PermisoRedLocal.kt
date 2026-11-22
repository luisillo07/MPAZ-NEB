package com.example.mpaz_real_real.ui.components

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun PermisoRedLocal(contenido: @Composable () -> Unit) {
    val context = LocalContext.current
    val permiso = Manifest.permission.ACCESS_LOCAL_NETWORK
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {}

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= 37 &&
            ContextCompat.checkSelfPermission(context, permiso) != PackageManager.PERMISSION_GRANTED
        ) {
            launcher.launch(permiso)
        }
    }
    contenido()
}