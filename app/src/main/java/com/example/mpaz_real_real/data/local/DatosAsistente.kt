package com.example.mpaz_real_real.data.local

import com.example.mpaz_real_real.model.Asignacion
import com.example.mpaz_real_real.model.Estudiante
import com.example.mpaz_real_real.model.RespuestaEntregada
import java.text.SimpleDateFormat
import java.util.Locale


private fun aMilisegundos(fecha: String): Long {
    val formato = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return formato.parse(fecha)?.time ?: 0L
}

val estudiantesIniciales = listOf(
    Estudiante("EST-001", "Estudiante ficticio 1", "estudiante1"),
    Estudiante("EST-002", "Estudiante ficticio 2", "estudiante2"),
    Estudiante("EST-003", "Estudiante ficticio 3", "estudiante3"),
    Estudiante("EST-004", "Estudiante ficticio 4", "estudiante4"),
    Estudiante("EST-005", "Estudiante ficticio 5", "estudiante5"),
    Estudiante("EST-006", "Estudiante ficticio 6", "estudiante6")
)

val asignacionesIniciales = listOf(
    Asignacion("100001", "EST-001", "LEC-001", "asistente1", "30/11/2026 18:00", "pendiente", aMilisegundos("30/11/2026 18:00"), 45, null, false),
    // Ejemplo de un estudiante con dos asignaciones
    Asignacion("100002", "EST-001", "MAT-001", "asistente1", "04/12/2026 18:00", "pendiente", aMilisegundos("04/12/2026 18:00"), 45, null, false),
    // Estudiante que salió a mitad del intento y se le cumplió la duración y se cierra con lo marcado
    Asignacion("100003", "EST-003", "LEC-001", "asistente1", "31/12/2026 23:59", "en desarrollo", aMilisegundos("31/12/2026 23:59"), 45, aMilisegundos("16/11/2026 10:00"), true),
    // Actividad vencida por la hora límite con un intento abierto y que se cierra con lo marcado
    Asignacion("100004", "EST-004", "MAT-001", "asistente2", "16/11/2026 18:00", "en desarrollo", aMilisegundos("16/11/2026 18:00"), 45, aMilisegundos("16/11/2026 17:45"), true),
    // Aignación ya terminada (2 intentos)
    Asignacion("100005", "EST-005", "LEC-001", "asistente2", "31/12/2026 23:59", "finalizada", aMilisegundos("31/12/2026 23:59"), 45, aMilisegundos("10/11/2026 10:00"), false),
    // Aignación ya terminada  (2 intentos)
    Asignacion("100006", "EST-006", "MAT-001", "asistente2", "31/12/2026 23:59", "finalizada", aMilisegundos("31/12/2026 23:59"), 45, aMilisegundos("10/11/2026 10:30"), false),
    // Actividad no disponible (LEC-003 es borrador)
    Asignacion("100007", "EST-002", "LEC-003", "asistente1", "11/12/2026 18:00", "pendiente", aMilisegundos("11/12/2026 18:00"), 45, null, false),
    // Estudiante envió el intento 1 y se le cumplió el plazo entre intentos, donde no se inventa un intento
    Asignacion("100008", "EST-002", "MAT-001", "asistente2", "31/12/2026 23:59", "en desarrollo", aMilisegundos("31/12/2026 23:59"), 45, aMilisegundos("16/11/2026 11:00"), false),
    // Actividad pendiente que venció sin comenzar por el estudiante
    Asignacion("100009", "EST-004", "LEC-001", "asistente1", "16/11/2026 23:59", "pendiente", aMilisegundos("16/11/2026 23:59"), 45, null, false))

val respuestasIniciales = listOf(
    // EST-005 · LEC-001 (4 aciertos)
    RespuestaEntregada(codigoTemporal = "100005", numeroPregunta = 1, alternativaElegida = 1, intentos = 1),
    RespuestaEntregada(codigoTemporal = "100005", numeroPregunta = 2, alternativaElegida = 1, intentos = 2),
    RespuestaEntregada(codigoTemporal = "100005", numeroPregunta = 3, alternativaElegida = 1, intentos = 1),
    RespuestaEntregada(codigoTemporal = "100005", numeroPregunta = 4, alternativaElegida = 0, intentos = 1),
    // EST-006 · MAT-001 (3 aciertos, 1 error)
    RespuestaEntregada(codigoTemporal = "100006", numeroPregunta = 1, alternativaElegida = 1, intentos = 1),
    RespuestaEntregada(codigoTemporal = "100006", numeroPregunta = 2, alternativaElegida = 0, intentos = 1),
    RespuestaEntregada(codigoTemporal = "100006", numeroPregunta = 3, alternativaElegida = 0, intentos = 2),
    RespuestaEntregada(codigoTemporal = "100006", numeroPregunta = 4, alternativaElegida = 1, intentos = 2)
)