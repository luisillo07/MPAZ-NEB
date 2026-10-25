package com.example.mpaz_real_real.data.local

import com.example.mpaz_real_real.model.Actividad
import com.example.mpaz_real_real.model.Pregunta

val actividadesIniciales = listOf(
    Actividad(
        codigo = "LEC-001",
        nombre = "El perro Tito",
        modulo = "Lectura",
        curso = "2.º básico",
        contenido = "Comprensión lectora",
        objetivo = "Extraer información explícita e implícita de un texto narrativo breve",
        dificultad = "Fácil",
        instrucciones = "Lee el texto con calma y responde las preguntas. Puedes volver a leer el texto cuando quieras.",
        cantidadPreguntas = 4,
        intentosPermitidos = 2,
        estado = "disponible",
        fecha = "[FECHA 1]",
        tituloTexto = "El perro Tito",
        tipoTexto = "Narrativo",
        contenidoTexto = "Tito es un perro café. Vive con Ana en una casa con un patio grande. Todas las tardes Tito juega con una pelota roja.",
        nivelVocabulario = "Básico"
    ),
    Actividad(
        codigo = "LEC-002",
        nombre = "La casa de Ana",
        modulo = "Lectura",
        curso = "2.º básico",
        contenido = "Comprensión lectora",
        objetivo = "Identificar personajes, lugares y acciones en un texto descriptivo breve",
        dificultad = "Medio",
        instrucciones = "Lee el texto con calma y responde las preguntas. Puedes volver a leer el texto cuando quieras.",
        cantidadPreguntas = 4,
        intentosPermitidos = 2,
        estado = "disponible",
        fecha = "[FECHA 2]",
        tituloTexto = "La casa de Ana",
        tipoTexto = "Descriptivo",
        contenidoTexto = "Ana vive con su abuela en una casa amarilla. En la cocina hay una mesa de madera. Los domingos, Ana y su abuela preparan pan para el desayuno.",
        nivelVocabulario = "Básico"
    ),
    Actividad(
        codigo = "MAT-001",
        nombre = "Sumas y restas en la feria",
        modulo = "Matemática",
        curso = "2.º básico",
        contenido = "Adición y sustracción hasta 100",
        objetivo = "Resolver problemas simples de adición y sustracción con números hasta 100",
        dificultad = "Fácil",
        instrucciones = "Lee cada ejercicio con calma, haz el cálculo y marca la respuesta correcta. Puedes usar tu cuaderno para calcular.",
        cantidadPreguntas = 4,
        intentosPermitidos = 2,
        estado = "disponible",
        fecha = "[FECHA 1]",
        tituloTexto = "",
        tipoTexto = "",
        contenidoTexto = "",
        nivelVocabulario = ""
    ),
    Actividad(
        codigo = "LEC-003",
        nombre = "Actividad 4",
        modulo = "Lectura",
        curso = "",
        contenido = "",
        objetivo = "",
        dificultad = "",
        instrucciones = "",
        cantidadPreguntas = null,
        intentosPermitidos = null,
        estado = "borrador",
        fecha = "",
        tituloTexto = "",
        tipoTexto = "",
        contenidoTexto = "",
        nivelVocabulario = ""
    )
)

val preguntasIniciales = listOf(
    // LEC-001
    Pregunta(actividadCodigo = "LEC-001", tipo = "explícita", enunciado = "¿De qué color es Tito?",
        alternativaA = "Negro", alternativaB = "Café", alternativaC = "Blanco", correcta = 1),
    Pregunta(actividadCodigo = "LEC-001", tipo = "explícita", enunciado = "¿Con qué juega Tito?",
        alternativaA = "Con un hueso", alternativaB = "Con una pelota roja", alternativaC = "Con Ana en la plaza", correcta = 1),
    Pregunta(actividadCodigo = "LEC-001", tipo = "implícita", enunciado = "¿Dónde juega Tito con su pelota?",
        alternativaA = "En la calle", alternativaB = "En el patio de la casa", alternativaC = "En la escuela", correcta = 1),
    Pregunta(actividadCodigo = "LEC-001", tipo = "vocabulario", enunciado = "¿Qué significa la palabra «patio»?",
        alternativaA = "Un espacio abierto de la casa", alternativaB = "Un tipo de perro", alternativaC = "Un juguete", correcta = 0),
    // LEC-002
    Pregunta(actividadCodigo = "LEC-002", tipo = "explícita", enunciado = "¿De qué color es la casa de Ana?",
        alternativaA = "Azul", alternativaB = "Amarilla", alternativaC = "Verde", correcta = 1),
    Pregunta(actividadCodigo = "LEC-002", tipo = "explícita", enunciado = "¿Con quién vive Ana?",
        alternativaA = "Con su abuela", alternativaB = "Con su hermano", alternativaC = "Con su perro", correcta = 0),
    Pregunta(actividadCodigo = "LEC-002", tipo = "implícita", enunciado = "¿En qué parte de la casa preparan el pan?",
        alternativaA = "En el patio", alternativaB = "En la cocina", alternativaC = "En el dormitorio", correcta = 1),
    Pregunta(actividadCodigo = "LEC-002", tipo = "vocabulario", enunciado = "¿Qué significa la palabra «madera»?",
        alternativaA = "Un tipo de comida", alternativaB = "Material de los árboles", alternativaC = "Un color", correcta = 1),
    // MAT-001
    Pregunta(actividadCodigo = "MAT-001", tipo = "suma",
        enunciado = "Ana tiene 12 manzanas y compra 5 más. ¿Cuántas manzanas tiene ahora?",
        alternativaA = "15", alternativaB = "17", alternativaC = "7", correcta = 1,
        datosNumericos = "12 y 5", operacion = "Adición", procedimiento = "12 + 5 = 17"),
    Pregunta(actividadCodigo = "MAT-001", tipo = "resta",
        enunciado = "En la feria hay 20 globos y se venden 8. ¿Cuántos globos quedan?",
        alternativaA = "12", alternativaB = "28", alternativaC = "10", correcta = 0,
        datosNumericos = "20 y 8", operacion = "Sustracción", procedimiento = "20 - 8 = 12"),
    Pregunta(actividadCodigo = "MAT-001", tipo = "suma",
        enunciado = "Tito tiene 25 láminas y su amigo le regala 10. ¿Cuántas láminas tiene Tito?",
        alternativaA = "30", alternativaB = "15", alternativaC = "35", correcta = 2,
        datosNumericos = "25 y 10", operacion = "Adición", procedimiento = "25 + 10 = 35"),
    Pregunta(actividadCodigo = "MAT-001", tipo = "resta",
        enunciado = "Un bus lleva 30 niños y en la escuela se bajan 14. ¿Cuántos niños siguen en el bus?",
        alternativaA = "44", alternativaB = "16", alternativaC = "14", correcta = 1,
        datosNumericos = "30 y 14", operacion = "Sustracción", procedimiento = "30 - 14 = 16")
)