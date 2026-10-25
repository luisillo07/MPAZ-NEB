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
        name = "mpaz_database_v3"
    ).build()

    private val dao = database.actividadDao
    private val asignacionDao = database.asignacionDao

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
                    fecha = actividad.fecha,
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
    fun onFormFechaChange(valor: String) { actualizarFormulario { it.copy(fecha = valor) } }

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

    fun onGuardarFormulario(): Boolean {
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
            fecha = formulario.fecha,
            tituloTexto = formulario.tituloTexto,
            tipoTexto = formulario.tipoTexto,
            contenidoTexto = formulario.contenidoTexto,
            nivelVocabulario = formulario.nivelVocabulario
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

        guardarEnBd(actividad, formulario.preguntas, formulario.esNuevo)

        val textoBase = if (formulario.esNuevo) "Actividad creada" else "Actividad guardada"
        val mensaje = if (esBorrador) textoBase + " como borrador porque faltan datos" else textoBase

        _state.update { it.copy(mensajeExito = mensaje) }
        reiniciarFiltros()
        return true
    }
    // ---------- Asistente: filtros de "Seleccionar actividad" ----------

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
        actualizarAsistente { it.copy(codigoAsignacionRevision = asignacion.codigoTemporal) }
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

    fun onAsignarClick(): Boolean {
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
        val errorFecha = if (formulario.fecha.isBlank()) "Selecciona una fecha." else null

        if (errorEstudiante != null || errorFecha != null) {
            actualizarAsistente {
                it.copy(formulario = it.formulario.copy(errores = AsignarFormErrores(estudiante = errorEstudiante, fecha = errorFecha)))
            }
            return false
        }

        val codigoTemporal = generarCodigoTemporal()
        val asignacion = Asignacion(
            codigoTemporal = codigoTemporal,
            estudianteCodigo = codigoEstudiante,
            actividadCodigo = formulario.actividadCodigo,
            asistente = asistente.asistenteResponsable,
            fecha = formulario.fecha,
            estado = "pendiente"
        )

        viewModelScope.launch {
            try {
                asignacionDao.insertarAsignacion(asignacion)
            } catch (e: Exception) {
            }
        }

        actualizarAsistente { it.copy(mensajeExito = "Actividad asignada. Código temporal: $codigoTemporal") }
        return true
    }

    fun onCerrarMensajeAsistente() {
        actualizarAsistente { it.copy(mensajeExito = null) }
    }

    private fun actualizarAsistente(cambio: (AsistenteUiState) -> AsistenteUiState) {
        _state.update { it.copy(asistente = cambio(it.asistente)) }
    }

    private fun generarCodigoTemporal(): String {
        var numero = (System.currentTimeMillis() % 900000) + 100000
        while (_state.value.asistente.asignaciones.firstOrNull { it.codigoTemporal == numero.toString() } != null) {
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
        viewModelScope.launch {
            try {
                dao.actualizar(actividad)
            } catch (e: Exception) {
            }
        }
    }

    private fun guardarEnBd(actividad: Actividad, preguntas: List<Pregunta>, esNueva: Boolean) {
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