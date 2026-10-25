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
        dificultad = "Fácil",
        estado = "disponible",
        fecha = "[FECHA1]",
        instrucciones = "Lee el texto con calma y responde las preguntas. Puedes volver a leer el texto cuando quieras.",
        tituloTexto = "El perro Tito",
        contenidoTexto = "Tito es un perro café. Vive con Ana en una casa con un patio grande. Todas las tardes Tito juega con una pelota roja.",
        tipoTexto = "Narrativo",
        nivelVocabulario = "Básico",
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
        dificultad = "Medio",
        estado = "disponible",
        fecha = "[FECHA2]",
        instrucciones = "Lee el texto con calma y responde las preguntas. Puedes volver a leer el texto cuando quieras.",
        tituloTexto = "La casa de Ana",
        contenidoTexto = "Ana vive con su abuela en una casa amarilla. En la cocina hay una mesa de madera. Los domingos, Ana y su abuela preparan pan para el desayuno.",
        tipoTexto = "Descriptivo",
        nivelVocabulario = "Básico",
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
    ),
    Actividad(
        codigo = "MAT-001",
        nombre = "Sumas y restas en la feria",
        modulo = "Matemática",
        curso = "2.º básico",
        contenido = "Adición y sustracción hasta 100",
        objetivo = "Resolver problemas simples de adición y sustracción con números hasta 100",
        dificultad = "Fácil",
        estado = "disponible",
        fecha = "[FECHA1]",
        instrucciones = "Lee cada ejercicio con calma, haz el cálculo y marca la respuesta correcta. Puedes usar tu cuaderno para calcular.",
        tituloTexto = "",
        contenidoTexto = "",
        intentosPermitidos = 2,
        preguntas = listOf(
            Pregunta(
                tipo = "Problema",
                enunciado = "Ana tiene 12 manzanas y compra 5 más. ¿Cuántas manzanas tiene ahora?",
                alternativas = listOf("15", "17", "7"),
                correcta = 1,
                retroalimentacion = "Si juntas 12 manzanas y 5 manzanas, tienes 17.",
                procedimiento = "12 + 5 = 17",
                datosNumericos = "12 y 5",
                operacion = "Adición"
            ),
            Pregunta(
                tipo = "Problema",
                enunciado = "En la feria hay 20 globos y se venden 8. ¿Cuántos globos quedan?",
                alternativas = listOf("12", "28", "10"),
                correcta = 0,
                retroalimentacion = "Si a 20 globos les quitas los 8 que se vendieron, quedan 12.",
                procedimiento = "20 - 8 = 12",
                datosNumericos = "20 y 8",
                operacion = "Sustracción"
            ),
            Pregunta(
                tipo = "Cálculo",
                enunciado = "¿Cuánto es 25 + 10?",
                alternativas = listOf("30", "15", "35"),
                correcta = 2,
                retroalimentacion = "Si a 25 le sumas 10, llegas a 35.",
                procedimiento = "25 + 10 = 35",
                datosNumericos = "25 y 10",
                operacion = "Adición"
            ),
            Pregunta(
                tipo = "Cálculo",
                enunciado = "¿Cuánto es 30 - 14?",
                alternativas = listOf("44", "16", "14"),
                correcta = 1,
                retroalimentacion = "Si a 30 le quitas 14, quedan 16.",
                procedimiento = "30 - 14 = 16",
                datosNumericos = "30 y 14",
                operacion = "Sustracción"
            )
        )
    )
)