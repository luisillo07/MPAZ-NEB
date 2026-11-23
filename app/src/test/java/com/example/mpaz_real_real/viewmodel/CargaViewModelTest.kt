package com.example.mpaz_real_real.viewmodel

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class CargaViewModelTest : StringSpec({

    beforeTest { Dispatchers.setMain(UnconfinedTestDispatcher()) }
    afterTest { Dispatchers.resetMain() }

    "la pantalla de carga debe terminar después de 1,5 segundos (sin esperar de verdad)" {
        val viewModel = CargaViewModel()

        runTest {
            viewModel.cargando.value shouldBe true

            advanceTimeBy(1499)
            viewModel.cargando.value shouldBe true

            advanceUntilIdle()
            viewModel.cargando.value shouldBe false
        }
    }
})