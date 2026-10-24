package com.example.mpaz_real_real.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mpaz_real_real.model.LoginErrores
import com.example.mpaz_real_real.model.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())

    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    fun seleccionarTipoUsuario(tipoUsuario: String) {
        _state.update {
            it.copy(
                tipoUsuario = tipoUsuario,
                usuario = "",
                contrasenia = "",
                codigoTemporal = "",
                errores = LoginErrores()
            )
        }
    }

    fun usuarioChanged(usuario: String) {
        _state.update {
            it.copy(
                usuario = usuario,
                errores = it.errores.copy(usuario = null)
            )
        }
    }

    fun contraseniaChanged(contrasenia: String) {
        _state.update {
            it.copy(
                contrasenia = contrasenia,
                errores = it.errores.copy(contrasenia = null)
            )
        }
    }

    fun validar(): Boolean {
        val estadoActual = _state.value

        if (estadoActual.tipoUsuario.isBlank()) {
            return false
        }

        val errores = LoginErrores(
            usuario = if (estadoActual.usuario.isBlank()) {
                "Ingresa tu usuario"
            } else {
                null
            },

            contrasenia = if (estadoActual.contrasenia.isBlank()) {
                "Ingresa tu contraseña"
            } else {
                null
            },

            codigoTemporal = null
        )

        _state.update {
            it.copy(errores = errores)
        }

        return errores.usuario == null &&
                errores.contrasenia == null
    }

    fun codigoTemporalChanged(codigoTemporal: String) {
        _state.update {
            it.copy(
                codigoTemporal = codigoTemporal,
                errores = it.errores.copy(codigoTemporal = null)
            )
        }
    }

    fun volverASeleccion() {
        _state.update {
            LoginUiState()
        }
    }
}