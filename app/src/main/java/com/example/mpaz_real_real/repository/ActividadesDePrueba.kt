package com.example.mpaz_real_real.repository

import com.example.mpaz_real_real.model.Actividad
import com.example.mpaz_real_real.model.Pregunta

val actividadesDePrueba = listOf(
    Actividad(
        codigo = "LEC-001",
        nombre = "El perro Tito",
        modulo = "Lectura",
        curso = "2.º básico",
        contenido = "Comprensión lectora",
        objetivo = "Extraer información explícita e implícita de un texto narrativo breve",
        dificultad = "Inicial",
        estado = "disponible",
        instrucciones = "Lee el texto con calma y responde las preguntas. Puedes volver a leer el texto cuando quieras.",
        tituloTexto = "El perro Tito",
        contenidoTexto = "Tito es un perro café. Vive con Ana en una casa con un patio grande. Todas las tardes Tito juega con una pelota roja.",
        intentosPermitidos = 2,
        preguntas = listOf(
            Pregunta(
                tipo = "explícita",
                enunciado = "¿De qué color es Tito?",
                alternativas = listOf("Negro", "Café", "Blanco"),
                correcta = 1,
                retroalimentacion = "El texto dice: «Tito es un perro café»."
            ),
            Pregunta(
                tipo = "explícita",
                enunciado = "¿Con qué juega Tito?",
                alternativas = listOf("Con un hueso", "Con una pelota roja", "Con Ana en la plaza"),
                correcta = 1,
                retroalimentacion = "Todas las tardes Tito juega con una pelota roja."
            ),
            Pregunta(
                tipo = "implícita",
                enunciado = "¿Dónde juega Tito con su pelota?",
                alternativas = listOf("En la calle", "En el patio de la casa", "En la escuela"),
                correcta = 1,
                retroalimentacion = "El texto no lo dice directo, pero Tito vive en una casa con un patio grande y juega ahí todas las tardes."
            ),
            Pregunta(
                tipo = "vocabulario",
                enunciado = "¿Qué significa la palabra «patio»?",
                alternativas = listOf("Un espacio abierto de la casa", "Un tipo de perro", "Un juguete"),
                correcta = 0,
                retroalimentacion = "Un patio es un espacio abierto dentro de una casa."
            )
        )
    )
)

