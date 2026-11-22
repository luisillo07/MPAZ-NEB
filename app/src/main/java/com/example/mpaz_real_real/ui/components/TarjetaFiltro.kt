package com.example.mpaz_real_real.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mpaz_real_real.ui.theme.CelesteFondo
import com.example.mpaz_real_real.ui.utils.colorFondoEstado

// Alto de la tarjeta en su versión grande (la del carrusel de "Seleccionar actividad"). Es fijo
// para que todas las tarjetas de la fila queden parejas, y alcanza justo para el título y una
// opción de hasta dos líneas ("Adición y sustracción hasta 100").
private val ALTO_TARJETA_GRANDE = 96.dp

// Selector en forma de tarjeta compacta: un título pequeño en mayúsculas ("MÓDULO"), debajo la
// opción elegida en negrita y una flechita. Al tocarla se abre un menú con las opciones. Tiene el
// mismo estilo que las demás tarjetas (gris claro, borde negro fino).
//
// - Si hay una opción elegida (distinta de `valorNeutro`) la tarjeta pasa a celeste suave para
//   que se note. El cambio de color es animado (animateColorAsState, cap. 34 del profe).
// - Si se pasa `error`, la tarjeta pasa a rojo claro y el mensaje aparece debajo deslizándose
//   (AnimatedVisibility, cap. 34 del profe).
// - Como esos colores son fijos en modo claro y oscuro, el texto también es fijo (negro).
//
// Para los filtros se usa tal cual ("Todos" es la opción neutra). Para un formulario donde al
// principio no hay nada elegido, se pasa valorNeutro = "" y textoVacio = "Seleccionar".
// Con grande = true la tarjeta es más alta y la opción elegida se ve con letra más grande.
@Composable
fun TarjetaFiltro(
    titulo: String,
    seleccion: String,
    opciones: List<String>,
    onSeleccion: (String) -> Unit,
    modifier: Modifier = Modifier,
    valorNeutro: String = "Todos",
    textoVacio: String = "",
    error: String? = null,
    grande: Boolean = false
) {
    var expandido by remember { mutableStateOf(false) }

    // Guarda el último mensaje de error para que no desaparezca de golpe mientras la animación
    // de salida todavía se está viendo.
    var ultimoError by remember { mutableStateOf("") }
    if (error != null) {
        ultimoError = error
    }

    val activo = seleccion != valorNeutro
    val hayError = error != null
    val forma = MaterialTheme.shapes.medium
    val colorTexto = if (activo || hayError) Color.Black else MaterialTheme.colorScheme.onSurface

    val colorObjetivo = if (hayError) {
        colorFondoEstado("pendiente")
    } else if (activo) {
        CelesteFondo
    } else {
        fondoTarjetaResumen()
    }
    val colorFondo by animateColorAsState(targetValue = colorObjetivo, label = "fondoFiltro")

    Column(modifier = modifier) {
        // El Box agrupa la tarjeta y su menú para que el menú se abra justo debajo de ella.
        Box {
            Surface(
                color = colorFondo,
                shape = forma,
                modifier = Modifier
                    .fillMaxWidth()
                    .then(if (grande) Modifier.height(ALTO_TARJETA_GRANDE) else Modifier)
                    .border(width = 1.dp, color = Color.Black, shape = forma)
                    .clickable { expandido = true }
            ) {
                // En la versión grande el título y la opción elegida (con letra más grande) quedan
                // juntos, pegados arriba de la tarjeta.
                Column(
                    modifier = Modifier.padding(
                        start = 12.dp,
                        top = if (grande) 12.dp else 10.dp,
                        end = 4.dp,
                        bottom = if (grande) 12.dp else 6.dp
                    )
                ) {
                    Text(
                        text = titulo.uppercase(),
                        color = colorTexto,
                        fontSize = 12.sp,
                        maxLines = 1
                    )
                    Row(
                        modifier = Modifier.padding(top = if (grande) 2.dp else 0.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (seleccion.isEmpty()) textoVacio else seleccion,
                            color = colorTexto,
                            style = if (grande) {
                                MaterialTheme.typography.titleMedium
                            } else {
                                MaterialTheme.typography.bodyLarge
                            },
                            fontWeight = FontWeight.Bold,
                            // En la versión grande caben dos líneas ("Adición y sustracción...").
                            maxLines = if (grande) 2 else 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = colorTexto
                        )
                    }
                }
            }

            DropdownMenu(
                expanded = expandido,
                onDismissRequest = { expandido = false }
            ) {
                opciones.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            onSeleccion(opcion)
                            expandido = false
                        }
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = hayError,
            enter = slideInVertically() + fadeIn()
        ) {
            Text(
                text = ultimoError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }
}
