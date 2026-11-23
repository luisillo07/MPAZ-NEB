package com.example.mpaz_real_real.model

import com.example.mpaz_real_real.data.local.actividadesIniciales
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ActividadUiStateTest : StringSpec({
    val actividad = actividadesIniciales.first { it.codigo == "LEC-001" }

    "con 1 intento usado de 2, todavía quedan intentos" {
        val estado = ActividadUiState(actividad = actividad, intentosUsados = 1)
        estado.quedanIntentos shouldBe true
    }

    "con 2 intentos usados de 2, ya no quedan intentos" {
        val estado = ActividadUiState(actividad = actividad, intentosUsados = 2)
        estado.quedanIntentos shouldBe false
    }

    "sin actividad cargada, no quedan intentos" {
        val estado = ActividadUiState()
        estado.quedanIntentos shouldBe false
    }

    "respondidas debe contar solo las preguntas con alternativa elegida" {
        val estado = ActividadUiState(respuestas = listOf(0, null, 2, null))
        estado.respondidas shouldBe 2
    }

    "en la cuarta de cuatro preguntas, es la última pregunta" {
        val estado = ActividadUiState(respuestas = listOf(0, 1, 2, null), indicePregunta = 3)
        estado.esUltimaPregunta shouldBe true
    }

    "en la tercera de cuatro retroalimentaciones, todavía no es la última" {
        val estado = ActividadUiState(respuestas = listOf(0, 1, 2, 1), indiceRetroalimentacion = 2)
        estado.esUltimaRetroalimentacion shouldBe false
    }
})