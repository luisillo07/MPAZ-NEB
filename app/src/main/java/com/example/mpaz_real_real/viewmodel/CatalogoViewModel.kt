package com.example.mpaz_real_real.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.mpaz_real_real.data.local.AppDatabase
import com.example.mpaz_real_real.data.local.actividadesIniciales
import com.example.mpaz_real_real.data.local.asignacionesIniciales
import com.example.mpaz_real_real.data.local.preguntasIniciales
import com.example.mpaz_real_real.data.local.estudiantesIniciales
import com.example.mpaz_real_real.data.local.respuestasIniciales
import com.example.mpaz_real_real.model.Asignacion
import com.example.mpaz_real_real.model.AsignarFormErrores
import com.example.mpaz_real_real.model.AsignarFormUiState
import com.example.mpaz_real_real.model.AsistenteUiState
import com.example.mpaz_real_real.model.Actividad
import com.example.mpaz_real_real.model.CatalogoUiState
import com.example.mpaz_real_real.model.FormularioActividadErrores
import com.example.mpaz_real_real.model.FormularioActividadUiState
import com.example.mpaz_real_real.model.Pregunta
import com.example.mpaz_real_real.repository.ActividadRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import com.example.mpaz_real_real.data.model.ActividadResponse
import retrofit2.HttpException

class CatalogoViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database = Room.databaseBuilder(
        context = application,
        klass = AppDatabase::class.java,
        name = "mpazneb_database"
    ).fallbackToDestructiveMigration(true).build()

    private val dao = database.actividadDao
    private val asignacionDao = database.asignacionDao
    private val actividadRepository = ActividadRepository()
    private val patronLectura = Regex("^LEC-[0-9]{3}$")
    private val patronMatematica = Regex("^MAT-[0-9]{3}$")

    private val _state = MutableStateFlow(CatalogoUiState())
    val state: StateFlow<CatalogoUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            if (dao.contar() == 0) {
                actividadesIniciales.forEach { dao.insertar(it) }
                preguntasIniciales.forEach { dao.insertarPregunta(it) }
            }
        }
        viewModelScope.launch {
            dao.observarTodas().collect { lista ->
                _state.update { estado -> estado.copy(actividades = lista) }
            }
        }
        viewModelScope.launch {
            dao.observarPreguntas().collect { lista ->
                _state.update { estado -> estado.copy(preguntas = lista) }
            }
        }
        viewModelScope.launch {
            if (asignacionDao.contarEstudiantes() == 0) {
                estudiantesIniciales.forEach { asignacionDao.insertarEstudiante(it) }
                asignacionesIniciales.forEach { asignacionDao.insertarAsignacion(it) }
                respuestasIniciales.forEach { asignacionDao.insertarRespuesta(it) }
            }
        }
        viewModelScope.launch {
            asignacionDao.observarEstudiantes().collect { lista ->
                actualizarAsistente { it.copy(estudiantes = lista) }
            }
        }
        viewModelScope.launch {
            asignacionDao.observarAsignaciones().collect { lista ->
                actualizarAsistente { it.copy(asignaciones = lista) }
            }
        }
        viewModelScope.launch {
            asignacionDao.observarRespuestas().collect { lista ->
                actualizarAsistente { it.copy(respuestas = lista) }
            }
        }
    }

    fun cargarServidor() {
        viewModelScope.launch {
            try {
                val lista = actividadRepository.obtenerActividades()
                _state.update { it.copy(actividadesServidor = lista, servidorConectado = true) }
            } catch (e: Exception) {
                println("Error al obtener datos: ${e.localizedMessage}")
                _state.update { it.copy(servidorConectado = false) }
            }
        }
    }

    fun onReintentar() {
        if (_state.value.enviando) return
        val codigos = _state.value.pendientes
        _state.update { it.copy(enviando = true) }
        viewModelScope.launch {
            enviarAlServidor(codigos)
            if (_state.value.pendientes.isEmpty()) {
                _state.update { it.copy(mensajeExito = "Actividad subida al servidor.") }
            } else {
                _state.update { it.copy(mensajeAlerta = "No tienes conexión, intenta más tarde.") }
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

    fun onRevisarClick(actividad: Actividad) {
        _state.update { it.copy(codigoRevision = actividad.codigo) }
    }

    fun onCambiarEstado(codigo: String) {
        if (_state.value.enviando) return
        val actividad = _state.value.actividades.firstOrNull { it.codigo == codigo } ?: return
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

    fun onEliminarClick(actividad: Actividad) {
        if (_state.value.enviando) return
        val asignada = _state.value.asistente.asignaciones.firstOrNull { it.actividadCodigo == actividad.codigo } != null
        if (asignada) {
            _state.update {
                it.copy(mensajeError = "No se puede eliminar «${actividad.nombre}» porque está asignada a un estudiante.")
            }
        } else {
            _state.update { it.copy(codigoPorEliminar = actividad.codigo) }
        }
    }

    fun onCancelarEliminar() {
        _state.update { it.copy(codigoPorEliminar = null) }
    }

    fun onConfirmarEliminar() {
        val codigo = _state.value.codigoPorEliminar ?: return
        if (_state.value.enviando) return
        _state.update { it.copy(codigoPorEliminar = null, enviando = true) }
        viewModelScope.launch {
            try {
                dao.borrarPreguntasDe(codigo)
                dao.borrarActividad(codigo)
                _state.update { it.copy(mensajeExito = "Actividad eliminada.") }
            } catch (e: Exception) {
                println("Error al eliminar $codigo: ${e.localizedMessage}")
            }
            enviarAlServidor(listOf(codigo))
        }
    }

    fun onCerrarMensaje() {
        _state.update { it.copy(mensajeError = null) }
    }

    fun onCerrarMensajeExito() {
        _state.update { it.copy(mensajeExito = null) }
    }

    fun onCerrarAlerta() {
        _state.update { it.copy(mensajeAlerta = null) }
    }

    fun onAgregarClick() {
        _state.update { it.copy(formulario = FormularioActividadUiState(esNuevo = true)) }
    }

    fun onEditarClick(actividad: Actividad) {
        _state.update {
            it.copy(
                formulario = FormularioActividadUiState(
                    esNuevo = false,
                    codigo = actividad.codigo,
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
                    tituloTexto = actividad.tituloTexto,
                    tipoTexto = actividad.tipoTexto,
                    contenidoTexto = actividad.contenidoTexto,
                    nivelVocabulario = actividad.nivelVocabulario,
                    preguntas = it.preguntas.filter { pregunta -> pregunta.actividadCodigo == actividad.codigo }
                )
            )
        }
    }

    fun onFormCodigoChange(valor: String) {
        actualizarFormulario { it.copy(codigo = valor, errores = it.errores.copy(codigo = null)) }
    }

    fun onFormNombreChange(valor: String) { actualizarFormulario { it.copy(nombre = valor) } }
    fun onFormModuloChange(valor: String) {
        actualizarFormulario { it.copy(modulo = valor, errores = it.errores.copy(modulo = null)) }
    }
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

    fun onNuevaPreguntaChange(valor: String) {
        actualizarFormulario { it.copy(nuevaPregunta = valor, errores = it.errores.copy(nuevaPregunta = null)) }
    }

    fun onNuevaAlternativaAChange(valor: String) {
        actualizarFormulario { it.copy(nuevaAlternativaA = valor, errores = it.errores.copy(nuevaPregunta = null)) }
    }

    fun onNuevaAlternativaBChange(valor: String) {
        actualizarFormulario { it.copy(nuevaAlternativaB = valor, errores = it.errores.copy(nuevaPregunta = null)) }
    }

    fun onNuevaAlternativaCChange(valor: String) {
        actualizarFormulario { it.copy(nuevaAlternativaC = valor, errores = it.errores.copy(nuevaPregunta = null)) }
    }

    fun onNuevaCorrectaChange(indice: Int) {
        actualizarFormulario { it.copy(nuevaCorrecta = indice, errores = it.errores.copy(nuevaPregunta = null)) }
    }

    fun onAgregarPregunta() {
        val formulario = _state.value.formulario
        val cantidad = formulario.cantidadPreguntas.toIntOrNull()

        if (formulario.nuevaPregunta.isBlank() ||
            formulario.nuevaAlternativaA.isBlank() ||
            formulario.nuevaAlternativaB.isBlank() ||
            formulario.nuevaAlternativaC.isBlank() ||
            formulario.nuevaCorrecta !in 0..2
        ) {
            actualizarFormulario {
                it.copy(errores = it.errores.copy(nuevaPregunta = "Completa la pregunta, las tres alternativas y marca la correcta."))
            }
            return
        }

        if (cantidad != null && cantidad > 0 && formulario.preguntas.size >= cantidad) {
            actualizarFormulario {
                it.copy(errores = it.errores.copy(cantidadPreguntas = "Ya agregaste las $cantidad preguntas indicadas. Aumenta la cantidad para agregar más."))
            }
            return
        }

        val nueva = Pregunta(
            enunciado = formulario.nuevaPregunta.trim(),
            alternativaA = formulario.nuevaAlternativaA.trim(),
            alternativaB = formulario.nuevaAlternativaB.trim(),
            alternativaC = formulario.nuevaAlternativaC.trim(),
            correcta = formulario.nuevaCorrecta
        )

        val lista = mutableListOf<Pregunta>()
        formulario.preguntas.forEach { lista.add(it) }
        lista.add(nueva)

        actualizarFormulario {
            it.copy(
                preguntas = lista,
                nuevaPregunta = "",
                nuevaAlternativaA = "",
                nuevaAlternativaB = "",
                nuevaAlternativaC = "",
                nuevaCorrecta = -1,
                errores = it.errores.copy(nuevaPregunta = null, cantidadPreguntas = null)
            )
        }
    }

    fun onQuitarPregunta(indice: Int) {
        val formulario = _state.value.formulario
        val lista = mutableListOf<Pregunta>()
        for (i in 0..formulario.preguntas.size - 1) {
            if (i != indice) {
                lista.add(formulario.preguntas[i])
            }
        }
        actualizarFormulario { it.copy(preguntas = lista, errores = it.errores.copy(cantidadPreguntas = null)) }
    }

    fun onGuardarFormulario(usuarioDocente: String): Boolean {
        if (_state.value.enviando) return false
        val usuario = usuarioDocente.trim()
        if (usuario.isBlank()) {
            _state.update { it.copy(mensajeError = "Vuelve a iniciar sesión.")}
            return false
        }
        val formulario = _state.value.formulario
        val errores = validarFormulario(formulario)

        if (errores.codigo != null || errores.modulo != null || errores.cantidadPreguntas != null || errores.intentosPermitidos != null) {
            _state.update { it.copy(formulario = it.formulario.copy(errores = errores)) }
            return false
        }

        val actividadBase = Actividad(
            codigo = formulario.codigo.trim(),
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
            tituloTexto = formulario.tituloTexto,
            tipoTexto = formulario.tipoTexto,
            contenidoTexto = formulario.contenidoTexto,
            nivelVocabulario = formulario.nivelVocabulario,
            docente = usuario
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

        val textoBase = if (formulario.esNuevo) "Actividad creada" else "Actividad guardada"
        val mensaje = if (esBorrador) textoBase + " como borrador porque faltan datos" else textoBase

        _state.update { it.copy(mensajeExito = mensaje) }
        guardarEnBd(actividad, formulario.preguntas, formulario.esNuevo)
        reiniciarFiltros()
        return true
    }

    fun onAsistenteModuloChange(valor: String) { actualizarAsistente { it.copy(modulo = valor) } }
    fun onAsistenteCursoChange(valor: String) { actualizarAsistente { it.copy(curso = valor) } }
    fun onAsistenteContenidoChange(valor: String) { actualizarAsistente { it.copy(contenido = valor) } }
    fun onAsistenteDificultadChange(valor: String) { actualizarAsistente { it.copy(dificultad = valor) } }

    fun onAsistenteFiltrarClick() {
        actualizarAsistente {
            it.copy(
                moduloAplicado = it.modulo,
                cursoAplicado = it.curso,
                contenidoAplicado = it.contenido,
                dificultadAplicado = it.dificultad
            )
        }
    }

    fun reiniciarFiltrosAsistente() {
        actualizarAsistente {
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

    fun onAsignacionClick(asignacion: Asignacion) {
        actualizarAsistente {
            it.copy(
                codigoAsignacionRevision = asignacion.codigoTemporal,
                observacion = asignacion.observacion,
                errorObservacion = null
            )
        }
    }

    fun onObservacionChange(valor: String) {
        actualizarAsistente { it.copy(observacion = valor, errorObservacion = null) }
    }

    fun onGuardarObservacion() {
        val asistente = _state.value.asistente
        val texto = asistente.observacion.trim()
        if (texto.isBlank()) {
            actualizarAsistente { it.copy(errorObservacion = "Escribe la observación.") }
            return
        }
        viewModelScope.launch {
            try {
                val asignacion = asignacionDao.buscarAsignacion(asistente.codigoAsignacionRevision)
                if (asignacion != null) {
                    asignacionDao.actualizarAsignacion(asignacion.copy(observacion = texto))
                    actualizarAsistente { it.copy(mensajeExito = "Observación guardada.") }
                } else {
                    actualizarAsistente { it.copy(mensajeError = "No se encontró la asignación.") }
                }
            } catch (e: Exception) {
                println("Error al guardar la observación: ${e.localizedMessage}")
                actualizarAsistente { it.copy(mensajeError = "No se pudo guardar la observación. Inténtalo de nuevo.") }
            }
        }
    }

    fun onSeleccionarActividad(actividad: Actividad) {
        actualizarAsistente { it.copy(formulario = AsignarFormUiState(actividadCodigo = actividad.codigo)) }
    }

    fun onAsignarEstudianteChange(valor: String) {
        actualizarAsistente {
            it.copy(formulario = it.formulario.copy(estudiante = valor, errores = it.formulario.errores.copy(estudiante = null)))
        }
    }

    fun onAsignarFechaChange(valor: String) {
        actualizarAsistente {
            it.copy(formulario = it.formulario.copy(fecha = valor, errores = it.formulario.errores.copy(fecha = null)))
        }
    }

    fun onAsignarMinutosChange(valor: String) {
        actualizarAsistente {
            it.copy(formulario = it.formulario.copy(minutos = valor, errores = it.formulario.errores.copy(minutos = null)))
        }
    }

    fun onAsignarClick(usuarioAsistente: String): Boolean {
        val usuario = usuarioAsistente.trim()
        if (usuario.isBlank()) {
            actualizarAsistente { it.copy(mensajeError = "Vuelve a iniciar sesión.") }
            return false
        }
        val asistente = _state.value.asistente
        val formulario = asistente.formulario
        val codigoEstudiante = formulario.estudiante.substringBefore(" · ")

        val yaAsignada = asistente.asignaciones.firstOrNull {
            it.estudianteCodigo == codigoEstudiante &&
                    it.actividadCodigo == formulario.actividadCodigo &&
                    it.estado != "finalizada"
        } != null

        val errorEstudiante = if (formulario.estudiante.isBlank()) {
            "Selecciona un estudiante."
        } else if (yaAsignada) {
            "Este estudiante ya tiene asignada esta actividad."
        } else {
            null
        }
        val fechaLimite = leerHoraLimite(formulario.fecha.trim())
        val errorFecha = if (formulario.fecha.isBlank()) {
            "Escribe la hora límite de la actividad"
        } else if (fechaLimite == null) {
            "La fecha está mal escrita o no existe. Escríbela como dd/mm/yyyy hh:mm"
        } else if (fechaLimite <= System.currentTimeMillis()) {
            "La hora límite ya pasó"
        } else {
            null
        }
        val minutosDuracion = formulario.minutos.toIntOrNull() ?: 0
        val errorMinutos = if (minutosDuracion < 1) "Ingresa los minutos que durará la actividad." else null

        if (errorEstudiante != null || errorFecha != null || errorMinutos != null || fechaLimite == null) {
            actualizarAsistente {
                it.copy(formulario = it.formulario.copy(errores = AsignarFormErrores(estudiante = errorEstudiante, fecha = errorFecha, minutos = errorMinutos)))
            }
            return false
        }

        viewModelScope.launch {
            val codigoTemporal = generarCodigoTemporal()
            val asignacion = Asignacion(
                codigoTemporal = codigoTemporal,
                estudianteCodigo = codigoEstudiante,
                actividadCodigo = formulario.actividadCodigo,
                asistente = usuario,
                fecha = formulario.fecha.trim(),
                estado = "pendiente",
                fechaLimite = fechaLimite,
                minutosDuracion = minutosDuracion,
                inicio = null,
                intentoAbierto = false,
                observacion = ""
            )
            try {
                asignacionDao.insertarAsignacion(asignacion)
                actualizarAsistente { it.copy(mensajeExito = "Actividad asignada. Código temporal: $codigoTemporal") }
            } catch (e: Exception) {
                println("Error al asignar: ${e.message}")
                actualizarAsistente { it.copy(mensajeError = "No se pudo asignar la actividad. Inténtalo de nuevo.") }
            }
        }
        return true
    }

    fun onCerrarMensajeAsistente() {
        actualizarAsistente { it.copy(mensajeExito = null) }
    }

    fun onCerrarErrorAsistente() {
        actualizarAsistente { it.copy(mensajeError = null) }
    }

    private fun actualizarAsistente(cambio: (AsistenteUiState) -> AsistenteUiState) {
        _state.update { it.copy(asistente = cambio(it.asistente)) }
    }
    private val patronHoraLimite = Regex("^[0-9]{2}/[0-9]{2}/[0-9]{4} [0-9]{2}:[0-9]{2}$")

    private fun leerHoraLimite(texto: String): Long? {
        if (!patronHoraLimite.matches(texto)) {
            return null
        }
        val formato = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        formato.isLenient = false
        return try {
            formato.parse(texto)?.time
        } catch (e: Exception) {
            null
        }
    }
    private suspend fun generarCodigoTemporal(): String {
        var numero = (System.currentTimeMillis() % 900000) + 100000
        while (asignacionDao.buscarAsignacion(numero.toString()) != null) {
            numero = numero + 1
            if (numero > 999999) {
                numero = 100000
            }
        }
        return numero.toString()
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
        val cantidad = formulario.cantidadPreguntas.toIntOrNull()
        val errorNumeroPreguntas = errorNumero(formulario.cantidadPreguntas)
        val errorCantidad = if (errorNumeroPreguntas != null) {
            errorNumeroPreguntas
        } else if (cantidad != null && cantidad != formulario.preguntas.size) {
            "Indicaste $cantidad preguntas, pero hay ${formulario.preguntas.size}. Agrega o quita preguntas."
        } else {
            null
        }

        return FormularioActividadErrores(
            codigo = errorCodigo(formulario),
            modulo = errorModulo(formulario),
            cantidadPreguntas = errorCantidad,
            intentosPermitidos = errorNumero(formulario.intentosPermitidos)
        )
    }

    private fun errorCodigo(formulario: FormularioActividadUiState): String? {
        if (!formulario.esNuevo) return null
        val codigo = formulario.codigo.trim()
        val esLectura = patronLectura.matches(codigo)
        val esMatematica = patronMatematica.matches(codigo)

        return if (codigo.isBlank()) {
            "Completa este campo."
        } else if (!esLectura && !esMatematica) {
            "Usa el formato LEC-### o MAT-### (por ejemplo, LEC-001)."
        } else if ((formulario.modulo == "Lectura" && !esLectura) || (formulario.modulo == "Matemática" && !esMatematica)) {
            "Para Lectura usa LEC-### y para Matemática usa MAT-###."
        } else if (_state.value.actividades.firstOrNull { it.codigo.uppercase() == codigo.uppercase() } != null) {
            "Este identificador ya existe."
        } else {
            null
        }
    }

    private fun errorModulo(formulario: FormularioActividadUiState): String? {
        if (formulario.esNuevo) return null
        val codigo = formulario.codigo.trim()
        return if (patronLectura.matches(codigo) && formulario.modulo == "Matemática") {
            "El identificador $codigo corresponde a Lectura. No puedes cambiar el módulo a Matemática."
        } else if (patronMatematica.matches(codigo) && formulario.modulo == "Lectura") {
            "El identificador $codigo corresponde a Matemática. No puedes cambiar el módulo a Lectura."
        } else {
            null
        }
    }

    private fun errorNumero(texto: String): String? {
        if (texto.isBlank()) return null
        val numero = texto.toIntOrNull()
        return if (numero == null || numero <= 0) "Ingresa un número entero mayor que 0." else null
    }

    private fun actualizarEnBd(actividad: Actividad) {
        _state.update { it.copy(enviando = true) }
        viewModelScope.launch {
            try {
                dao.actualizar(actividad)
            } catch (e: Exception) {
            }
            enviarAlServidor(listOf(actividad.codigo))
        }
    }

    private fun guardarEnBd(actividad: Actividad, preguntas: List<Pregunta>, esNueva: Boolean) {
        _state.update { it.copy(enviando = true) }
        viewModelScope.launch {
            try {
                if (esNueva) {
                    dao.insertar(actividad)
                } else {
                    dao.actualizar(actividad)
                }
                dao.borrarPreguntasDe(actividad.codigo)
                preguntas.forEach { dao.insertarPregunta(it.copy(id = 0, actividadCodigo = actividad.codigo)) }
            } catch (e: Exception) {
            }
            enviarAlServidor(listOf(actividad.codigo))
        }
    }

    private suspend fun enviarAlServidor(codigos: List<String>) {
        codigos.forEach { codigo ->
            try {
                enviarActividad(codigo)
                quitarPendiente(codigo)
            } catch (e: HttpException) {
                agregarPendiente(codigo, "El servidor no aceptó $codigo (error ${e.code()}).")
            } catch (e: Exception) {
                println("Error al enviar $codigo: ${e.localizedMessage}")
                agregarPendiente(codigo, "No se pudo enviar $codigo porque no hay conexión con el servidor.")
            }
        }
        _state.update { it.copy(enviando = false) }
        cargarServidor()
    }

    private suspend fun enviarActividad(codigo: String) {
        val actividad = dao.buscar(codigo)
        val enServidor = buscarEnServidor(codigo)
        if (actividad != null) {
            if (enServidor == null) {
                actividadRepository.crearActividad(actividad)
            } else {
                actividadRepository.actualizarActividad(enServidor.id, actividad)
            }
            val preguntasServidor = actividadRepository.obtenerPreguntas(codigo)
            preguntasServidor.forEach { actividadRepository.eliminarPregunta(it.id) }
            val preguntas = dao.obtenerPreguntas(codigo)
            preguntas.forEach { actividadRepository.crearPregunta(it) }
        } else if (enServidor != null) {
            val preguntasServidor = actividadRepository.obtenerPreguntas(codigo)
            preguntasServidor.forEach { actividadRepository.eliminarPregunta(it.id) }
            actividadRepository.eliminarActividad(enServidor.id)
        }
    }

    private suspend fun buscarEnServidor(codigo: String): ActividadResponse? {
        val encontradas = actividadRepository.buscarActividades(codigo)
        return encontradas.firstOrNull { it.codigo.uppercase() == codigo.uppercase() }
    }

    private suspend fun agregarPendiente(codigo: String, mensaje: String) {
        val lista = mutableListOf<String>()
        _state.value.pendientes.forEach { if (it != codigo) lista.add(it) }
        lista.add(codigo)
        val alerta = if (dao.buscar(codigo) != null) {
            "Actividad guardada en local. Reintenta guardarla en el servidor."
        } else {
            "Actividad eliminada en local. Reintenta eliminarla en el servidor."
        }
        _state.update {
            it.copy(
                pendientes = lista,
                errorServidor = mensaje,
                mensajeExito = null,
                mensajeAlerta = alerta
            )
        }
    }

    private fun quitarPendiente(codigo: String) {
        _state.update { it.copy(pendientes = it.pendientes.filter { pendiente -> pendiente != codigo }) }
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