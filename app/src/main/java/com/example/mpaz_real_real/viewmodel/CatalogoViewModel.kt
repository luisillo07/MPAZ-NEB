package com.example.mpaz_real_real.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.mpaz_real_real.data.local.AppDatabase
import com.example.mpaz_real_real.model.Actividad
import com.example.mpaz_real_real.model.CatalogoUiState
import com.example.mpaz_real_real.model.FormularioActividadErrores
import com.example.mpaz_real_real.model.FormularioActividadUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CatalogoViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database = Room.databaseBuilder(
        context = application,
        klass = AppDatabase::class.java,
        name = "mpaz_database"
    ).build()

    private val dao = database.actividadDao

    private val actividadesIniciales = listOf(
        Actividad("IDENTIFICADOR1", "Actividad 1", "Lectura", "1.º básico", "[CONTENIDO LECTURA 1]", "[OBJETIVO PEDAGÓGICO 1]", "Fácil", "[INSTRUCCIONES 1]", 2, 2, "disponible", "[FECHA 1]"),
        Actividad("IDENTIFICADOR2", "Actividad 2", "Lectura", "2.º básico", "[CONTENIDO LECTURA 2]", "[OBJETIVO PEDAGÓGICO 2]", "Medio", "[INSTRUCCIONES 2]", 2, 2, "desactivada", "[FECHA 2]"),
        Actividad("IDENTIFICADOR3", "Actividad 3", "Matemática", "1.º básico", "[CONTENIDO MATEMÁTICA 1]", "[OBJETIVO PEDAGÓGICO 3]", "Difícil", "[INSTRUCCIONES 3]", 2, 2, "disponible", "[FECHA 1]"),
        Actividad("IDENTIFICADOR4", "Actividad 4", "Matemática", "3.º básico", "[CONTENIDO MATEMÁTICA 2]", "[OBJETIVO PEDAGÓGICO 4]", "Fácil", "[INSTRUCCIONES 4]", 2, 2, "disponible", "[FECHA 2]"),
        Actividad("IDENTIFICADOR5", "Actividad 5", "Lectura", "4.º básico", "[CONTENIDO LECTURA 2]", "[OBJETIVO PEDAGÓGICO 5]", "Medio", "[INSTRUCCIONES 5]", 2, 2, "disponible", "[FECHA 1]"),
        Actividad("IDENTIFICADOR6", "Actividad 6", "Lectura", "", "", "", "", "", null, null, "borrador", "")
    )

    private val _state = MutableStateFlow(CatalogoUiState())
    val state: StateFlow<CatalogoUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            if (dao.contar() == 0) {
                actividadesIniciales.forEach { dao.insertar(it) }
            }
        }
        viewModelScope.launch {
            dao.observarTodas().collect { lista ->
                _state.update { estado -> estado.copy(actividades = lista) }
            }
        }
    }

    fun onModuloChange(valor: String) { _state.update { it.copy(modulo = valor) } }
    fun onCursoChange(valor: String) { _state.update { it.copy(curso = valor) } }
    fun onContenidoChange(valor: String) { _state.update { it.copy(contenido = valor) } }
    fun onDificultadChange(valor: String) { _state.update { it.copy(dificultad = valor) } }

    fun onFiltrarClick() {
        _state.update {
            it.copy(
                moduloAplicado = it.modulo,
                cursoAplicado = it.curso,
                contenidoAplicado = it.contenido,
                dificultadAplicado = it.dificultad
            )
        }
    }

    fun reiniciarFiltros() {
        _state.update {
            it.copy(
                modulo = "Todos",
                curso = "Todos",
                contenido = "Todos",
                dificultad = "Todos",
                moduloAplicado = "Todos",
                cursoAplicado = "Todos",
                contenidoAplicado = "Todos",
                dificultadAplicado = "Todos"
            )
        }
    }

    fun onCambiarEstado(id: String) {
        val actividad = _state.value.actividades.firstOrNull { it.id == id } ?: return
        when (actividad.estado) {
            "disponible" -> actualizarEnBd(actividad.copy(estado = "desactivada"))
            "desactivada" -> actualizarEnBd(actividad.copy(estado = "disponible"))
            else -> {
                val faltantes = datosFaltantes(actividad)
                if (faltantes.isNotEmpty()) {
                    _state.update { it.copy(mensajeError = armarMensaje(faltantes)) }
                } else {
                    actualizarEnBd(actividad.copy(estado = "disponible"))
                }
            }
        }
    }

    fun onCerrarMensaje() {
        _state.update { it.copy(mensajeError = null) }
    }

    fun onCerrarMensajeExito() {
        _state.update { it.copy(mensajeExito = null) }
    }

    fun onAgregarClick() {
        _state.update { it.copy(formulario = FormularioActividadUiState(esNuevo = true)) }
    }

    fun onEditarClick(actividad: Actividad) {
        _state.update {
            it.copy(
                formulario = FormularioActividadUiState(
                    esNuevo = false,
                    id = actividad.id,
                    nombre = actividad.nombre,
                    modulo = actividad.modulo,
                    curso = actividad.curso,
                    contenido = actividad.contenido,
                    objetivo = actividad.objetivo,
                    dificultad = actividad.dificultad,
                    instrucciones = actividad.instrucciones,
                    cantidadPreguntas = actividad.cantidadPreguntas?.toString() ?: "",
                    intentosPermitidos = actividad.intentosPermitidos?.toString() ?: "",
                    estado = nombreEstadoFormulario(actividad.estado),
                    fecha = actividad.fecha
                )
            )
        }
    }

    fun onFormIdChange(valor: String) {
        actualizarFormulario { it.copy(id = valor, errores = it.errores.copy(id = null)) }
    }

    fun onFormNombreChange(valor: String) { actualizarFormulario { it.copy(nombre = valor) } }
    fun onFormModuloChange(valor: String) { actualizarFormulario { it.copy(modulo = valor) } }
    fun onFormCursoChange(valor: String) { actualizarFormulario { it.copy(curso = valor) } }
    fun onFormContenidoChange(valor: String) { actualizarFormulario { it.copy(contenido = valor) } }
    fun onFormObjetivoChange(valor: String) { actualizarFormulario { it.copy(objetivo = valor) } }
    fun onFormDificultadChange(valor: String) { actualizarFormulario { it.copy(dificultad = valor) } }
    fun onFormInstruccionesChange(valor: String) { actualizarFormulario { it.copy(instrucciones = valor) } }

    fun onFormCantidadPreguntasChange(valor: String) {
        actualizarFormulario { it.copy(cantidadPreguntas = valor, errores = it.errores.copy(cantidadPreguntas = null)) }
    }

    fun onFormIntentosPermitidosChange(valor: String) {
        actualizarFormulario { it.copy(intentosPermitidos = valor, errores = it.errores.copy(intentosPermitidos = null)) }
    }

    fun onFormEstadoChange(valor: String) { actualizarFormulario { it.copy(estado = valor) } }
    fun onFormFechaChange(valor: String) { actualizarFormulario { it.copy(fecha = valor) } }

    fun onGuardarFormulario(): Boolean {
        val formulario = _state.value.formulario
        val errores = validarFormulario(formulario)

        if (errores.id != null || errores.cantidadPreguntas != null || errores.intentosPermitidos != null) {
            _state.update { it.copy(formulario = it.formulario.copy(errores = errores)) }
            return false
        }

        val actividadBase = Actividad(
            id = formulario.id.trim(),
            nombre = formulario.nombre.trim(),
            modulo = formulario.modulo,
            curso = formulario.curso,
            contenido = formulario.contenido,
            objetivo = formulario.objetivo.trim(),
            dificultad = formulario.dificultad,
            instrucciones = formulario.instrucciones.trim(),
            cantidadPreguntas = formulario.cantidadPreguntas.toIntOrNull(),
            intentosPermitidos = formulario.intentosPermitidos.toIntOrNull(),
            estado = "borrador",
            fecha = formulario.fecha
        )

        val esBorrador = datosFaltantes(actividadBase).isNotEmpty() || formulario.estado.isBlank()
        val estadoFinal = if (esBorrador) {
            "borrador"
        } else if (formulario.estado == "Disponible") {
            "disponible"
        } else {
            "desactivada"
        }
        val actividad = actividadBase.copy(estado = estadoFinal)

        if (formulario.esNuevo) {
            insertarEnBd(actividad)
        } else {
            actualizarEnBd(actividad)
        }

        val textoBase = if (formulario.esNuevo) "Actividad creada" else "Actividad guardada"
        val mensaje = if (esBorrador) textoBase + " como borrador porque faltan datos" else textoBase

        _state.update { it.copy(mensajeExito = mensaje) }
        reiniciarFiltros()
        return true
    }

    private fun actualizarFormulario(cambio: (FormularioActividadUiState) -> FormularioActividadUiState) {
        _state.update { it.copy(formulario = cambio(it.formulario)) }
    }

    private fun nombreEstadoFormulario(estado: String): String = when (estado) {
        "disponible" -> "Disponible"
        "desactivada" -> "Desactivada"
        else -> ""
    }

    private fun validarFormulario(formulario: FormularioActividadUiState): FormularioActividadErrores {
        val errorId = if (!formulario.esNuevo) {
            null
        } else if (formulario.id.isBlank()) {
            "Completa este campo."
        } else if (_state.value.actividades.firstOrNull { it.id.uppercase() == formulario.id.trim().uppercase() } != null) {
            "Este identificador ya existe."
        } else {
            null
        }

        return FormularioActividadErrores(
            id = errorId,
            cantidadPreguntas = errorNumero(formulario.cantidadPreguntas),
            intentosPermitidos = errorNumero(formulario.intentosPermitidos)
        )
    }

    private fun errorNumero(texto: String): String? {
        if (texto.isBlank()) return null
        val numero = texto.toIntOrNull()
        return if (numero == null || numero <= 0) "Ingresa un número entero mayor que 0." else null
    }

    private fun actualizarEnBd(actividad: Actividad) {
        viewModelScope.launch {
            try {
                dao.actualizar(actividad)
            } catch (e: Exception) {
            }
        }
    }

    private fun insertarEnBd(actividad: Actividad) {
        viewModelScope.launch {
            try {
                dao.insertar(actividad)
            } catch (e: Exception) {
            }
        }
    }

    private fun datosFaltantes(actividad: Actividad): List<String> {
        val faltantes = mutableListOf<String>()
        if (actividad.nombre.isBlank()) faltantes.add("el nombre")
        if (actividad.modulo.isBlank()) faltantes.add("el módulo")
        if (actividad.curso.isBlank()) faltantes.add("el curso")
        if (actividad.contenido.isBlank()) faltantes.add("el contenido")
        if (actividad.objetivo.isBlank()) faltantes.add("el objetivo pedagógico")
        if (actividad.dificultad.isBlank()) faltantes.add("la dificultad")
        if (actividad.instrucciones.isBlank()) faltantes.add("las instrucciones")
        if (actividad.cantidadPreguntas == null) faltantes.add("la cantidad de preguntas")
        if (actividad.intentosPermitidos == null) faltantes.add("los intentos permitidos")
        if (actividad.fecha.isBlank()) faltantes.add("la fecha")
        return faltantes
    }

    private fun armarMensaje(faltantes: List<String>): String {
        var texto = "Error: Falta decidir "
        for (i in 0..faltantes.size - 1) {
            if (i > 0 && i == faltantes.size - 1) {
                texto = texto + " y "
            } else if (i > 0) {
                texto = texto + ", "
            }
            texto = texto + faltantes[i]
        }
        return texto
    }
}