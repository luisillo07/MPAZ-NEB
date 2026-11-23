package com.example.mpaz_real_real.model

import com.example.mpaz_real_real.data.local.actividadesIniciales
import com.example.mpaz_real_real.data.local.asignacionesIniciales
import com.example.mpaz_real_real.data.local.estudiantesIniciales
import com.example.mpaz_real_real.data.local.preguntasIniciales
import com.example.mpaz_real_real.data.local.respuestasIniciales
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe

class CatalogoUiStateTest : StringSpec({
    "sin filtros, el Catálogo debe mostrar todas las actividades" {
        val estado = CatalogoUiState(actividades = actividadesIniciales)
        estado.actividadesVisibles shouldContainExactly actividadesIniciales
    }

    "el filtro de módulo Matemática debe dejar solo MAT-001" {
        val estado = CatalogoUiState(
            actividades = actividadesIniciales,
            moduloAplicado = "Matemática"
        )
        estado.actividadesVisibles.map { it.codigo } shouldContainExactly listOf("MAT-001")
    }

    "para asignar no deben aparecer los borradores, y las desactivadas van al final" {
        val desactivada = actividadesIniciales.first { it.codigo == "LEC-001" }.copy(estado = "desactivada")
        val estado = CatalogoUiState(actividades = listOf(desactivada) + actividadesIniciales.filter { it.codigo != "LEC-001" })
        estado.actividadesParaAsignar.map { it.codigo } shouldContainExactly listOf("LEC-002", "MAT-001", "LEC-001")
    }

    "los filtros del asistente también se aplican al asignar" {
        val estado = CatalogoUiState(
            actividades = actividadesIniciales,
            asistente = AsistenteUiState(moduloAplicado = "Lectura", dificultadAplicado = "Medio")
        )
        estado.actividadesParaAsignar.map { it.codigo } shouldContainExactly listOf("LEC-002")
    }

    "al revisar una actividad deben aparecer sus datos y sus preguntas" {
        val estado = CatalogoUiState(
            actividades = actividadesIniciales,
            preguntas = preguntasIniciales,
            codigoRevision = "LEC-002"
        )
        estado.actividadRevisada?.nombre shouldBe "La casa de Ana"
        estado.preguntasRevisadas.size shouldBe 4
    }

    "nombreActividad() debe devolver el nombre, o el código si la actividad no existe" {
        val estado = CatalogoUiState(actividades = actividadesIniciales, preguntas = preguntasIniciales)
        estado.nombreActividad("MAT-001") shouldBe "Sumas y restas en la feria"
        estado.nombreActividad("LEC-999") shouldBe "LEC-999"
        estado.preguntasDe("MAT-001").size shouldBe 4
        estado.preguntasDe("MAT-001").first().alternativas() shouldContainExactly listOf("15", "17", "7")
    }

    "el asistente debe ver a cada estudiante con su código y nombre, y la asignación que revisa" {
        val estado = CatalogoUiState(
            asistente = AsistenteUiState(
                estudiantes = estudiantesIniciales,
                asignaciones = asignacionesIniciales,
                respuestas = respuestasIniciales,
                codigoAsignacionRevision = "100005"
            )
        )

        estado.opcionesEstudiante.first() shouldBe "EST-001 · Estudiante ficticio 1"
        estado.asignacionRevisada?.estado shouldBe "finalizada"
        estado.asistente.nombreEstudiante("EST-005") shouldBe "Estudiante ficticio 5"
        estado.asistente.nombreEstudiante("EST-999") shouldBe "EST-999"
        estado.asistente.respuestasDe("100005").size shouldBe 4
        estado.asistente.respuestasDe("100001").size shouldBe 0
    }

    "los filtros del asistente solo ofrecen lo que existe en las actividades que puede asignar" {
        val estado = CatalogoUiState(actividades = actividadesIniciales)
        estado.opcionesCursoAsistente shouldContainExactly listOf("Todos", "2.º básico")
        estado.opcionesDificultadAsistente shouldContainExactly listOf("Todos", "Fácil", "Medio")
        estado.opcionesContenidoAsistente shouldContainExactly listOf("Todos", "Comprensión lectora", "Adición y sustracción hasta 100")
    }
})