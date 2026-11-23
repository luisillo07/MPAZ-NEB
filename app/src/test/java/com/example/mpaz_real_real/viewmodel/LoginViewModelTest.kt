package com.example.mpaz_real_real.viewmodel

import android.app.Application
import com.example.mpaz_real_real.data.local.dao.UsuarioDao
import com.example.mpaz_real_real.data.local.entity.UsuarioEntity
import com.example.mpaz_real_real.model.LoginUiState
import com.example.mpaz_real_real.repository.usuariosDePrueba
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

fun loginViewModelDePrueba(usuarioDaoPrueba: UsuarioDao): LoginViewModel =
    object : LoginViewModel(mockk<Application>(relaxed = true)) {
        override val usuarioDao get() = usuarioDaoPrueba
    }

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest : StringSpec({
    beforeTest { Dispatchers.setMain(UnconfinedTestDispatcher()) }
    afterTest { Dispatchers.resetMain() }
    val docente = UsuarioEntity(usuario = "docente", contrasenia = "1234", tipoUsuario = "docente")
    fun ingresar(viewModel: LoginViewModel, usuario: String, contrasenia: String) {
        viewModel.usuarioChanged(usuario)
        viewModel.contraseniaChanged(contrasenia)
        viewModel.ingresar()
    }

    "al abrir, debe guardar en Room los usuarios de prueba" {
        val usuarioDao = mockk<UsuarioDao>(relaxed = true)
        loginViewModelDePrueba(usuarioDao)
        coVerify { usuarioDao.insertar(usuariosDePrueba.first()) }
        coVerify(exactly = usuariosDePrueba.size) { usuarioDao.insertar(any()) }
    }

    "con los campos vacíos, validar() debe mostrar los errores y no buscar en Room" {
        val usuarioDao = mockk<UsuarioDao>(relaxed = true)
        val viewModel = loginViewModelDePrueba(usuarioDao)
        viewModel.ingresar()
        viewModel.state.value.errores.usuario shouldBe "Ingresa tu usuario"
        viewModel.state.value.errores.contrasenia shouldBe "Ingresa tu contraseña"
        coVerify(exactly = 0) { usuarioDao.buscar(any(), any()) }
    }

    "al escribir, el error del campo debe desaparecer" {
        val viewModel = loginViewModelDePrueba(mockk(relaxed = true))
        viewModel.validar() shouldBe false
        viewModel.usuarioChanged("docente")
        viewModel.state.value.errores.usuario shouldBe null
        viewModel.contraseniaChanged("1234")
        viewModel.state.value.errores.contrasenia shouldBe null
        viewModel.validar() shouldBe true
    }

    "con usuario o contraseña incorrectos, no se entra y aparece el aviso" {
        val usuarioDao = mockk<UsuarioDao>(relaxed = true)
        coEvery { usuarioDao.buscar("docente", "9999") } returns null
        val viewModel = loginViewModelDePrueba(usuarioDao)
        ingresar(viewModel, "docente", "9999")
        viewModel.state.value.errores.contrasenia shouldBe "Usuario o contraseña incorrectos"
        viewModel.state.value.errores.usuarioIncorrecto shouldBe true
        viewModel.state.value.ingresoCorrecto shouldBe false
        viewModel.usuarioChanged("docente")
        viewModel.state.value.errores.usuarioIncorrecto shouldBe false
    }

    "el docente con datos correctos debe entrar y quedar como quien inició sesión" {
        val usuarioDao = mockk<UsuarioDao>(relaxed = true)
        coEvery { usuarioDao.buscar("docente", "1234") } returns docente
        val viewModel = loginViewModelDePrueba(usuarioDao)
        ingresar(viewModel, "docente", "1234")
        viewModel.state.value.ingresoCorrecto shouldBe true
        viewModel.state.value.tipoUsuario shouldBe "docente"
        viewModel.state.value.usuarioSesion shouldBe "docente"
    }

    "1,5 segundos después de entrar, el formulario queda vacío pero recuerda quién inició sesión" {
        val usuarioDao = mockk<UsuarioDao>(relaxed = true)
        coEvery { usuarioDao.buscar("docente", "1234") } returns docente
        val viewModel = loginViewModelDePrueba(usuarioDao)
        ingresar(viewModel, "docente", "1234")

        runTest {
            viewModel.state.value.usuario shouldBe "docente"

            advanceUntilIdle()
            viewModel.state.value shouldBe LoginUiState(usuarioSesion = "docente")
        }
    }

    "ingresoProcesado() debe apagar el aviso de ingreso correcto" {
        val usuarioDao = mockk<UsuarioDao>(relaxed = true)
        coEvery { usuarioDao.buscar("docente", "1234") } returns docente
        val viewModel = loginViewModelDePrueba(usuarioDao)
        ingresar(viewModel, "docente", "1234")
        viewModel.ingresoProcesado()
        viewModel.state.value.ingresoCorrecto shouldBe false
    }
})