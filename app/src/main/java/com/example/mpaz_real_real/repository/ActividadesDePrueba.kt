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
    ),
    Actividad(
        codigo = "LEC-002",
        nombre = "La casa de Ana",
        modulo = "Lectura",
        curso = "2.º básico",
        contenido = "Comprensión lectora",
        objetivo = "Identificar personajes, lugares y acciones en un texto descriptivo breve",
        dificultad = "Inicial",
        estado = "disponible",
        instrucciones = "Lee el texto con calma y responde las preguntas. Puedes volver a leer el texto cuando quieras.",
        tituloTexto = "La casa de Ana",
        contenidoTexto = "Ana vive con su abuela en una casa amarilla. En la cocina hay una mesa de madera. Los domingos, Ana y su abuela preparan pan para el desayuno.",
        intentosPermitidos = 2,
        preguntas = listOf(
            Pregunta(
                tipo = "explícita",
                enunciado = "¿De qué color es la casa de Ana?",
                alternativas = listOf("Azul", "Amarilla", "Verde"),
                correcta = 1,
                retroalimentacion = "El texto dice: «Ana vive con su abuela en una casa amarilla»."
            ),
            Pregunta(
                tipo = "explícita",
                enunciado = "¿Con quién vive Ana?",
                alternativas = listOf("Con su abuela", "Con su hermano", "Con su perro"),
                correcta = 0,
                retroalimentacion = "El texto dice: «Ana vive con su abuela en una casa amarilla»."
            ),
            Pregunta(
                tipo = "implícita",
                enunciado = "¿En qué parte de la casa preparan el pan?",
                alternativas = listOf("En el patio", "En la cocina", "En el dormitorio"),
                correcta = 1,
                retroalimentacion = "El texto no lo dice directo, pero el pan se prepara en la cocina, y el texto dice que su casa tiene una cocina con una mesa de madera."
            ),
            Pregunta(
                tipo = "vocabulario",
                enunciado = "¿Qué significa la palabra «madera»?",
                alternativas = listOf("Un tipo de comida", "Material de los árboles", "Un color"),
                correcta = 1,
                retroalimentacion = "La madera es el material que sale del tronco de los árboles."
            )
        )
    )
)
