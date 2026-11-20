package com.example.mpaz_real_real.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mpaz_real_real.R
import com.example.mpaz_real_real.model.LoginErrores
import com.example.mpaz_real_real.model.LoginUiState
import com.example.mpaz_real_real.navigation.Screen
import com.example.mpaz_real_real.ui.theme.Beige
import com.example.mpaz_real_real.ui.theme.BotonLogin
import com.example.mpaz_real_real.ui.theme.FondoLogin
import com.example.mpaz_real_real.ui.theme.Mpaz_real_realTheme
import com.example.mpaz_real_real.ui.utils.obtenerWindowSizeClass
import com.example.mpaz_real_real.viewmodel.LoginViewModel

// Pantalla "con lógica": observa el ViewModel y decide a dónde navegar.
// El dibujo en sí está en LoginContenido, que no sabe nada del ViewModel.
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel
) {
    val estado by viewModel.state.collectAsState()

    // Vista horizontal: un celular girado tiene poco alto (clase Compact en altura) y una
    // tablet horizontal tiene mucho ancho (clase Expanded). Se usa para elegir la imagen de fondo.
    val windowSizeClass = obtenerWindowSizeClass()
    val horizontal = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded ||
            windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact

    // Cuando el login es correcto, vamos a la sección que le corresponde a ese usuario.
    // No usamos popUpTo: así el login queda debajo y "Salir sesión" / atrás vuelve a él.
    // Apenas navega, avisa al ViewModel para que baje la marca ingresoCorrecto; si no, al
    // volver al login la pantalla volvería a navegar sola. (El ViewModel vacía los campos
    // un momento después, ver LoginViewModel.ingresar.)
    LaunchedEffect(estado.ingresoCorrecto) {
        if (estado.ingresoCorrecto) {
            val destino = when (estado.tipoUsuario) {
                "estudiante" -> Screen.MisActividades.route
                "docente" -> Screen.Catalogo.route
                else -> Screen.Asignaciones.route
            }
            navController.navigate(destino)
            viewModel.ingresoProcesado()
        }
    }

    LoginContenido(
        estado = estado,
        horizontal = horizontal,
        onUsuarioChange = viewModel::usuarioChanged,
        onContraseniaChange = viewModel::contraseniaChanged,
        onIngresar = viewModel::ingresar
    )
}

// Pantalla "de dibujo": recibe datos y avisa con funciones. Sirve también para la vista previa.
@Composable
fun LoginContenido(
    estado: LoginUiState,
    horizontal: Boolean,
    onUsuarioChange: (String) -> Unit,
    onContraseniaChange: (String) -> Unit,
    onIngresar: () -> Unit
) {
    Scaffold { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Capa de atrás: el fondo con las esquinas azules (Matemática) y rojas (Lenguaje).
            // Es una imagen que ocupa toda la pantalla, incluso detrás de las barras del sistema.
            Image(
                painter = painterResource(
                    id = if (horizontal) R.drawable.fondo_login_horizontal else R.drawable.fondo_login_vertical
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Capa de adelante: la tarjeta, centrada. innerPadding aparta el contenido de las
            // barras del sistema, e imePadding del teclado. Si el contenido no cabe (pantalla
            // girada, teclado abierto) la pantalla se puede desplazar.
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .imePadding()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TarjetaLogin(
                    estado = estado,
                    onUsuarioChange = onUsuarioChange,
                    onContraseniaChange = onContraseniaChange,
                    onIngresar = onIngresar
                )
            }
        }
    }
}

@Composable
private fun TarjetaLogin(
    estado: LoginUiState,
    onUsuarioChange: (String) -> Unit,
    onContraseniaChange: (String) -> Unit,
    onIngresar: () -> Unit
) {
    val forma = RoundedCornerShape(32.dp)

    Column(
        modifier = Modifier
            .width(280.dp)
            .clip(forma)
            .border(width = 2.dp, color = Color.Black, shape = forma)
    ) {
        // Parte de arriba: el logo dentro de un cuadrado blanco redondeado.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(Beige),
            contentAlignment = Alignment.Center
        ) {
            val formaLogo = RoundedCornerShape(20.dp)
            Image(
                painter = painterResource(id = R.drawable.logo_escuela),
                contentDescription = "Logo de la Escuela Escritora Marcela Paz",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(96.dp)
                    .clip(formaLogo)
                    .background(FondoLogin)
                    .border(width = 2.dp, color = Color.Black, shape = formaLogo)
                    .padding(6.dp)
            )
        }

        // Línea negra delgada (2 dp) que separa la parte beige de la de abajo. Es un Box de
        // 2 dp de alto pintado de negro.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(Color.Black)
        )

        // Parte de abajo: usuario, contraseña e Ingresar. El fondo es el color "surface" del
        // tema, así el texto se lee tanto en modo claro como en modo oscuro.
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
        ) {
            CampoLoginPildora(
                valor = estado.usuario,
                onCambio = onUsuarioChange,
                indicador = "Usuario",
                icono = Icons.Default.Person,
                error = estado.errores.usuario,
                imeAction = ImeAction.Next,
                // Si las credenciales no coinciden, Usuario también se pinta de rojo, pero sin
                // mensaje (el mensaje aparece solo bajo Contraseña).
                marcarError = estado.errores.usuarioIncorrecto
            )

            CampoLoginPildora(
                valor = estado.contrasenia,
                onCambio = onContraseniaChange,
                indicador = "Contraseña",
                icono = Icons.Default.Lock,
                error = estado.errores.contrasenia,
                imeAction = ImeAction.Done
            )

            // Excepción: ButtonDefaults.buttonColors y BorderStroke no aparecen en el material
            // del profe, pero dan el botón beige con borde negro de la app.
            Button(
                onClick = onIngresar,
                colors = ButtonDefaults.buttonColors(
                    containerColor = BotonLogin,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(50),
                border = BorderStroke(2.dp, Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = "Ingresar",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Campo de texto con forma de píldora y un ícono a la izquierda. Lo usan tanto "Usuario"
// como "Contraseña", así el estilo vive en un solo lugar. Usa los colores del tema.
@Composable
private fun CampoLoginPildora(
    valor: String,
    onCambio: (String) -> Unit,
    indicador: String,
    icono: ImageVector,
    error: String?,
    imeAction: ImeAction,
    marcarError: Boolean = false
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        placeholder = { Text(text = indicador) },
        leadingIcon = {
            Icon(imageVector = icono, contentDescription = null)
        },
        // El campo se pinta de rojo si tiene un mensaje de error o si se pidió marcarlo.
        isError = error != null || marcarError,
        // Solo se crea el espacio del mensaje cuando de verdad hay un error.
        supportingText = error?.let { mensaje ->
            { Text(text = mensaje, color = MaterialTheme.colorScheme.error) }
        },
        singleLine = true,
        // "Siguiente" pasa solo al campo de abajo; "Listo" cierra el teclado.
        keyboardOptions = KeyboardOptions(imeAction = imeAction),
        shape = RoundedCornerShape(50),
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true, widthDp = 411, heightDp = 891)
@Composable
fun LoginContenidoPreview() {
    Mpaz_real_realTheme {
        LoginContenido(
            estado = LoginUiState(),
            horizontal = false,
            onUsuarioChange = {},
            onContraseniaChange = {},
            onIngresar = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 411, heightDp = 891)
@Composable
fun LoginContenidoConErrorPreview() {
    Mpaz_real_realTheme {
        LoginContenido(
            estado = LoginUiState(
                usuario = "docente",
                contrasenia = "abc",
                errores = LoginErrores(contrasenia = "Usuario o contraseña incorrectos")
            ),
            horizontal = false,
            onUsuarioChange = {},
            onContraseniaChange = {},
            onIngresar = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 891, heightDp = 411)
@Composable
fun LoginContenidoHorizontalPreview() {
    Mpaz_real_realTheme {
        LoginContenido(
            estado = LoginUiState(),
            horizontal = true,
            onUsuarioChange = {},
            onContraseniaChange = {},
            onIngresar = {}
        )
    }
}
