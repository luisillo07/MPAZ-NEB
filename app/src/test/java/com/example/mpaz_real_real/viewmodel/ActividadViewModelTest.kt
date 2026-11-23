package com.example.mpaz_real_real.viewmodel

import android.app.Application
import android.net.Uri
import com.example.mpaz_real_real.data.local.actividadesIniciales
import com.example.mpaz_real_real.data.local.dao.ActividadDao
import com.example.mpaz_real_real.data.local.dao.ResultadoDao
import com.example.mpaz_real_real.data.local.entity.ResultadoEntity
import com.example.mpaz_real_real.data.model.Pokemon
import com.example.mpaz_real_real.model.ActividadUiState
import com.example.mpaz_real_real.model.Pregunta
import com.example.mpaz_real_real.repository.PokemonRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import java.io.IOException

class MocksActividad {
    val resultadoDao = mockk<ResultadoDao>(relaxed = true)
    val actividadDao = mockk<ActividadDao>(relaxed = true)
    val pokemonRepository = mockk<PokemonRepository>()

    init {
        every { resultadoDao.obtenerResultados() } returns flowOf(emptyList())
        every { actividadDao.observarDisponibles() } returns flowOf(emptyList())
    }
}

fun actividadViewModelDePrueba(m: MocksActividad): ActividadViewModel =
    object : ActividadViewModel(mockk<Application>(relaxed = true)) {
        override val resultadoDao get() = m.resultadoDao
        override val actividadDao get() = m.actividadDao
        override val pokemonRepository get() = m.pokemonRepository
    }

@OptIn(ExperimentalCoroutinesApi::class)
class ActividadViewModelTest : StringSpec({

    beforeTest { Dispatchers.setMain(UnconfinedTestDispatcher()) }
    afterTest { Dispatchers.resetMain() }

    val actividad = actividadesIniciales.first().copy(codigo = "LEC-050", cantidadPreguntas = 2, intentosPermitidos = 2)
    val preguntas = listOf(
        Pregunta(id = 1, actividadCodigo = "LEC-050", enunciado = "Uno", correcta = 0, retroalimentacion = "A era la correcta."),
        Pregunta(id = 2, actividadCodigo = "LEC-050", enunciado = "Dos", correcta = 1, retroalimentacion = "B era la correcta.")
    )

    fun preparar(m: MocksActividad, usados: Int = 0): ActividadViewModel {
        coEvery { m.actividadDao.buscar("LEC-050") } returns actividad
        coEvery { m.actividadDao.obtenerPreguntas("LEC-050") } returns preguntas
        coEvery { m.resultadoDao.contarIntentos("LEC-050") } returns usados
        val viewModel = actividadViewModelDePrueba(m)
        viewModel.cargarActividad("LEC-050")
        return viewModel
    }

    fun responderYEnviar(viewModel: ActividadViewModel) {
        viewModel.onAlternativaSeleccionada(0)
        viewModel.onSiguiente()
        viewModel.onAlternativaSeleccionada(2)
        viewModel.onSiguiente()
        viewModel.onEnviar()
    }

    "cargarActividad() debe traer de Room la actividad, sus preguntas y los intentos usados" {
        val viewModel = preparar(MocksActividad(), usados = 1)
        viewModel.estado.value.actividad shouldBe actividad
        viewModel.estado.value.preguntas.size shouldBe 2
        viewModel.estado.value.respuestas shouldBe listOf(null, null)
        viewModel.estado.value.intentosUsados shouldBe 1
    }

    "si la actividad no está en Room, el estado queda vacío" {
        val m = MocksActividad()
        coEvery { m.actividadDao.buscar("LEC-999") } returns null
        coEvery { m.actividadDao.obtenerPreguntas("LEC-999") } returns emptyList()
        val viewModel = actividadViewModelDePrueba(m)
        viewModel.cargarActividad("LEC-999")
        viewModel.estado.value shouldBe ActividadUiState()
    }

    "sin elegir alternativa, validarRespuesta() debe mostrar el aviso" {
        val viewModel = preparar(MocksActividad())
        viewModel.validarRespuesta() shouldBe false
        viewModel.estado.value.errorRespuesta shouldBe "Debes elegir una alternativa para continuar."
        viewModel.onAlternativaSeleccionada(1)
        viewModel.validarRespuesta() shouldBe true
        viewModel.estado.value.errorRespuesta shouldBe null
    }

    "en la última pregunta, Siguiente debe abrir la confirmación y Cambiar debe volver a la primera" {
        val viewModel = preparar(MocksActividad())
        viewModel.onSiguiente()
        viewModel.estado.value.indicePregunta shouldBe 1
        viewModel.onSiguiente()
        viewModel.estado.value.mostrarConfirmacion shouldBe true
        viewModel.onCambiarRespuestas()
        viewModel.estado.value.mostrarConfirmacion shouldBe false
        viewModel.estado.value.indicePregunta shouldBe 0
    }

    "al enviar, el intento debe guardarse en Room con sus aciertos y errores" {
        val m = MocksActividad()
        val viewModel = preparar(m)
        val guardado = slot<ResultadoEntity>()
        coEvery { m.resultadoDao.insertar(capture(guardado)) } returns Unit
        responderYEnviar(viewModel)
        guardado.captured.codigoActividad shouldBe "LEC-050"
        guardado.captured.respuestas shouldBe "[0, 2]"
        guardado.captured.aciertos shouldBe 1
        guardado.captured.errores shouldBe 1
        guardado.captured.noRespondidas shouldBe 0
        guardado.captured.totalPreguntas shouldBe 2
        viewModel.estado.value.mostrarConfirmacion shouldBe false
        viewModel.estado.value.intentosUsados shouldBe 1
        viewModel.estado.value.mensajeRetroalimentacion shouldBe "¡Muy bien! A era la correcta."
        viewModel.estado.value.mensajePokemon shouldBe null
    }

    "una pregunta sin responder cuenta como no respondida y no como error" {
        val m = MocksActividad()
        val viewModel = preparar(m)
        val guardado = slot<ResultadoEntity>()
        coEvery { m.resultadoDao.insertar(capture(guardado)) } returns Unit
        viewModel.onAlternativaSeleccionada(0)
        viewModel.onSiguiente()
        viewModel.onSiguiente()
        viewModel.onEnviar()
        guardado.captured.aciertos shouldBe 1
        guardado.captured.errores shouldBe 0
        guardado.captured.noRespondidas shouldBe 1
    }

    "sin actividad cargada, Enviar no guarda nada" {
        val m = MocksActividad()
        val viewModel = actividadViewModelDePrueba(m)
        viewModel.onEnviar()
        coVerify(exactly = 0) { m.resultadoDao.insertar(any()) }
        viewModel.estado.value.intentosUsados shouldBe 0
    }

    "la retroalimentación debe avanzar hasta la última y Reintentar debe limpiar las respuestas" {
        val viewModel = preparar(MocksActividad())
        responderYEnviar(viewModel)
        viewModel.onSiguienteRetroalimentacion()
        viewModel.estado.value.indiceRetroalimentacion shouldBe 1
        viewModel.estado.value.mensajeRetroalimentacion shouldBe "¡Casi! Revisemos juntos. B era la correcta."
        viewModel.onSiguienteRetroalimentacion()
        viewModel.estado.value.indiceRetroalimentacion shouldBe 1
        viewModel.onPantallazoSeleccionado(mockk<Uri>())
        viewModel.onReintentar()
        viewModel.estado.value.respuestas shouldBe listOf(null, null)
        viewModel.estado.value.indicePregunta shouldBe 0
        viewModel.estado.value.indiceRetroalimentacion shouldBe 0
        viewModel.estado.value.pantallazoPregunta shouldBe null
    }

    "sin intentos, Reintentar no debe hacer nada" {
        val m = MocksActividad()
        coEvery { m.pokemonRepository.obtenerPokemon(any()) } returns Pokemon(id = 25, name = "pikachu")
        val viewModel = preparar(m, usados = 1)
        responderYEnviar(viewModel)
        viewModel.onReintentar()
        viewModel.estado.value.respuestas shouldBe listOf(0, 2)
    }

    "el pantallazo elegido debe quedar en el estado" {
        val viewModel = preparar(MocksActividad())
        val uri = mockk<Uri>()
        viewModel.onPantallazoSeleccionado(null)
        viewModel.estado.value.pantallazoPregunta shouldBe null
        viewModel.onPantallazoSeleccionado(uri)
        viewModel.estado.value.pantallazoPregunta shouldBe uri
        viewModel.guardarResumen(null)
        viewModel.estado.value.resumenGuardado shouldBe false
    }

    "en el último intento, al final de la retroalimentación debe aparecer el Pokémon de la PokeAPI" {
        val m = MocksActividad()
        coEvery { m.pokemonRepository.obtenerPokemon(any()) } returns Pokemon(id = 25, name = "pikachu")
        val viewModel = preparar(m, usados = 1)
        responderYEnviar(viewModel)
        viewModel.estado.value.mensajePokemon shouldBe "Tu Pokémon de hoy: pikachu"
        coVerify(exactly = 1) { m.pokemonRepository.obtenerPokemon(match { it in 1..151 }) }
    }

    "sin conexión a internet, en vez del Pokémon debe aparecer el aviso" {
        val m = MocksActividad()
        coEvery { m.pokemonRepository.obtenerPokemon(any()) } throws IOException("Sin conexión")
        val viewModel = preparar(m, usados = 1)
        responderYEnviar(viewModel)
        viewModel.estado.value.mensajePokemon shouldBe "No pudimos traer tu Pokémon. Revisa tu conexión a internet."
    }

    "con intentos todavía, Siguiente en «Quedan intentos» también debe pedir el Pokémon" {
        val m = MocksActividad()
        coEvery { m.pokemonRepository.obtenerPokemon(any()) } returns Pokemon(id = 4, name = "charmander")
        val viewModel = preparar(m)
        responderYEnviar(viewModel)
        viewModel.estado.value.mensajePokemon shouldBe null
        viewModel.buscarPokemon()
        viewModel.estado.value.mensajePokemon shouldBe "Tu Pokémon de hoy: charmander"
        coEvery { m.pokemonRepository.obtenerPokemon(any()) } throws IOException("Sin conexión")
        viewModel.buscarPokemon()
        viewModel.estado.value.mensajePokemon shouldBe "No pudimos traer tu Pokémon. Revisa tu conexión a internet."
    }
})