package com.example.mpaz_real_real.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.mpaz_real_real.data.local.AppDatabase
import com.example.mpaz_real_real.model.LoginErrores
import com.example.mpaz_real_real.model.LoginUiState
import com.example.mpaz_real_real.repository.usuariosDePrueba
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database = Room.databaseBuilder(
        context = application,
        klass = AppDatabase::class.java,
        name = "mpazneb_database"
    ).fallbackToDestructiveMigration(true).build()

    private val usuarioDao = database.usuarioDao

    private val _state = MutableStateFlow(LoginUiState())

    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            usuariosDePrueba.forEach { usuarioDao.insertar(it) }
        }
    }

    fun seleccionarTipoUsuario(tipoUsuario: String) {
        _state.update {
            it.copy(
                tipoUsuario = tipoUsuario,
                usuario = "",
                contrasenia = "",
                codigoTemporal = "",
                errores = LoginErrores(),
                ingresoCorrecto = false
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

    fun ingresar() {
        if (!validar()) return

        val estadoActual = _state.value

        viewModelScope.launch {
            val usuarioEncontrado = usuarioDao.buscar(
                usuario = estadoActual.usuario,
                contrasenia = estadoActual.contrasenia,
                tipoUsuario = estadoActual.tipoUsuario
            )

            if (usuarioEncontrado == null) {
                _state.update {
                    it.copy(
                        errores = it.errores.copy(contrasenia = "Usuario o contraseña incorrectos"),
                        ingresoCorrecto = false
                    )
                }
            } else {
                _state.update {
                    it.copy(ingresoCorrecto = true)
                }
            }
        }
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