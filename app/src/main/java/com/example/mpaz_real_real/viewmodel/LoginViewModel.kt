package com.example.mpaz_real_real.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.mpaz_real_real.data.local.AppDatabase
import com.example.mpaz_real_real.model.LoginErrores
import com.example.mpaz_real_real.model.LoginUiState
import com.example.mpaz_real_real.repository.usuariosDePrueba
import kotlinx.coroutines.delay
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

    fun usuarioChanged(usuario: String) {
        _state.update {
            it.copy(
                usuario = usuario,
                errores = it.errores.copy(usuario = null, usuarioIncorrecto = false)
            )
        }
    }

    fun contraseniaChanged(contrasenia: String) {
        _state.update {
            it.copy(
                contrasenia = contrasenia,
                errores = it.errores.copy(contrasenia = null, usuarioIncorrecto = false)
            )
        }
    }

    fun validar(): Boolean {
        val estadoActual = _state.value

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
            }
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
                contrasenia = estadoActual.contrasenia
            )

            if (usuarioEncontrado == null) {
                _state.update {
                    it.copy(
                        errores = it.errores.copy(
                            contrasenia = "Usuario o contraseña incorrectos",
                            usuarioIncorrecto = true
                        ),
                        ingresoCorrecto = false
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        tipoUsuario = usuarioEncontrado.tipoUsuario,
                        ingresoCorrecto = true
                    )
                }

                // La pantalla ya navega sola al ver ingresoCorrecto = true. Aquí se espera
                // a que termine la animación de cambio de pantalla y recién entonces se
                // vacía el login: así, al cerrar sesión y volver, los campos aparecen
                // limpios, y no se ve cómo se borran mientras la pantalla se va.
                delay(1500)
                _state.value = LoginUiState()
            }
        }
    }

    // La pantalla llama a esto justo después de navegar. Baja la marca "ingresoCorrecto" al
    // instante: si no, al volver al login (por ejemplo con "Salir sesión") antes de que
    // termine la espera de arriba, la pantalla vería la marca todavía en true y volvería a
    // navegar sola, y habría que pulsar "Salir sesión" dos veces.
    fun ingresoProcesado() {
        _state.update { it.copy(ingresoCorrecto = false) }
    }
}