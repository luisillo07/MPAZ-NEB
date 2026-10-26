package com.example.mpaz_real_real.data.local

import com.example.mpaz_real_real.model.Asignacion
import com.example.mpaz_real_real.model.Estudiante
import com.example.mpaz_real_real.model.RespuestaEntregada

val estudiantesIniciales = listOf(
    Estudiante("EST-001", "Estudiante ficticio 1"),
    Estudiante("EST-002", "Estudiante ficticio 2"),
    Estudiante("EST-003", "Estudiante ficticio 3"),
    Estudiante("EST-004", "Estudiante ficticio 4"),
    Estudiante("EST-005", "Estudiante ficticio 5"),
    Estudiante("EST-006", "Estudiante ficticio 6")
)

val asignacionesIniciales = listOf(
    Asignacion("100001", "EST-001", "LEC-001", "ASIST-001", "[FECHA1]", "pendiente"),
    Asignacion("100002", "EST-002", "MAT-001", "ASIST-001", "[FECHA2]", "pendiente"),
    Asignacion("100003", "EST-003", "LEC-001", "ASIST-001", "[FECHA1]", "en desarrollo"),
    Asignacion("100004", "EST-004", "MAT-001", "ASIST-001", "[FECHA2]", "en desarrollo"),
    Asignacion("100005", "EST-005", "LEC-001", "ASIST-001", "[FECHA1]", "finalizada"),
    Asignacion("100006", "EST-006", "MAT-001", "ASIST-001", "[FECHA2]", "finalizada")
)

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