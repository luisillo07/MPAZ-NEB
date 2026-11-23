package com.example.mpaz_real_real.viewmodel

import android.app.Application
import com.example.mpaz_real_real.data.local.actividadesIniciales
import com.example.mpaz_real_real.data.local.asignacionesIniciales
import com.example.mpaz_real_real.data.local.dao.ActividadDao
import com.example.mpaz_real_real.data.local.dao.AsignacionDao
import com.example.mpaz_real_real.data.local.estudiantesIniciales
import com.example.mpaz_real_real.data.local.preguntasIniciales
import com.example.mpaz_real_real.data.local.respuestasIniciales
import com.example.mpaz_real_real.data.model.ActividadResponse
import com.example.mpaz_real_real.model.Asignacion
import com.example.mpaz_real_real.model.Pregunta
import com.example.mpaz_real_real.repository.ActividadRepository
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
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class MocksCatalogo {
    val dao = mockk<ActividadDao>(relaxed = true)
    val asignacionDao = mockk<AsignacionDao>(relaxed = true)
    val actividadRepository = mockk<ActividadRepository>(relaxed = true)

    val actividadesServidor = listOf(
        ActividadResponse(1, "LEC-001"),
        ActividadResponse(2, "LEC-002"),
        ActividadResponse(3, "MAT-001"),
        ActividadResponse(4, "LEC-003")
    )

    init {
        every { dao.observarTodas() } returns flowOf(actividadesIniciales)
        every { dao.observarPreguntas() } returns flowOf(preguntasIniciales)
        every { asignacionDao.observarEstudiantes() } returns flowOf(estudiantesIniciales)
        every { asignacionDao.observarAsignaciones() } returns flowOf(asignacionesIniciales)
        every { asignacionDao.observarRespuestas() } returns flowOf(respuestasIniciales)
        coEvery { dao.contar() } returns 0
        coEvery { asignacionDao.contarEstudiantes() } returns 0
        coEvery { asignacionDao.buscarAsignacion(any()) } returns null
        coEvery { dao.buscar(any()) } answers { actividadesIniciales.firstOrNull { it.codigo == firstArg<String>() } }
        coEvery { dao.obtenerPreguntas(any()) } answers { preguntasIniciales.filter { it.actividadCodigo == firstArg<String>() } }
        coEvery { actividadRepository.obtenerActividades() } returns actividadesServidor
        coEvery { actividadRepository.buscarActividades(any()) } answers { actividadesServidor.filter { it.codigo == firstArg<String>() } }
        coEvery { actividadRepository.obtenerPreguntas(any()) } returns emptyList()
    }
}

fun catalogoViewModelDePrueba(m: MocksCatalogo): CatalogoViewModel =
    object : CatalogoViewModel(mockk<Application>(relaxed = true)) {
        override val dao get() = m.dao
        override val asignacionDao get() = m.asignacionDao
        override val actividadRepository get() = m.actividadRepository
    }

fun errorHttp(codigo: Int) = HttpException(Response.error<Any>(codigo, ResponseBody.create(null, "{}")))

@OptIn(ExperimentalCoroutinesApi::class)
class CatalogoViewModelTest : StringSpec({

    beforeTest { Dispatchers.setMain(UnconfinedTestDispatcher()) }
    afterTest { Dispatchers.resetMain() }

    fun llenarNueva(viewModel: CatalogoViewModel, codigo: String = "LEC-090", estado: String = "Disponible") {
        viewModel.onAgregarClick()
        viewModel.onFormCodigoChange(codigo)
        viewModel.onFormNombreChange("Nueva")
        viewModel.onFormModuloChange("Lectura")
        viewModel.onFormCursoChange("1.º básico")
        viewModel.onFormContenidoChange("Comprensión lectora")
        viewModel.onFormObjetivoChange("Objetivo")
        viewModel.onFormDificultadChange("Fácil")
        viewModel.onFormInstruccionesChange("Lee")
        viewModel.onFormCantidadPreguntasChange("1")
        viewModel.onFormIntentosPermitidosChange("2")
        viewModel.onFormEstadoChange(estado)
        viewModel.onNuevaPreguntaChange("¿Pregunta?")
        viewModel.onNuevaAlternativaAChange("a")
        viewModel.onNuevaAlternativaBChange("b")
        viewModel.onNuevaAlternativaCChange("c")
        viewModel.onNuevaCorrectaChange(0)
        viewModel.onAgregarPregunta()
    }

    fun llenarAsignacion(viewModel: CatalogoViewModel) {
        viewModel.onSeleccionarActividad(actividadesIniciales.first { it.codigo == "LEC-002" })
        viewModel.onAsignarEstudianteChange("EST-001 · Estudiante ficticio 1")
        viewModel.onAsignarFechaChange("31/12/2099 23:59")
        viewModel.onAsignarMinutosChange("45")
    }

    "al abrir, debe cargar los datos de prueba en Room y mostrarlos" {
        val m = MocksCatalogo()
        val viewModel = catalogoViewModelDePrueba(m)
        coVerify { m.dao.insertar(actividadesIniciales.first()) }
        coVerify { m.dao.insertarPregunta(preguntasIniciales.first()) }
        coVerify { m.asignacionDao.insertarEstudiante(estudiantesIniciales.first()) }
        coVerify { m.asignacionDao.insertarAsignacion(asignacionesIniciales.first()) }
        coVerify { m.asignacionDao.insertarRespuesta(respuestasIniciales.first()) }
        viewModel.state.value.actividades.size shouldBe actividadesIniciales.size
        viewModel.state.value.preguntas.size shouldBe preguntasIniciales.size
        viewModel.state.value.asistente.estudiantes.size shouldBe estudiantesIniciales.size
        viewModel.state.value.asistente.asignaciones.size shouldBe asignacionesIniciales.size
        viewModel.state.value.asistente.respuestas.size shouldBe respuestasIniciales.size
    }

    "si Room ya tiene datos, no se vuelven a cargar" {
        val m = MocksCatalogo()
        coEvery { m.dao.contar() } returns 4
        coEvery { m.asignacionDao.contarEstudiantes() } returns 6
        catalogoViewModelDePrueba(m)
        coVerify(exactly = 0) { m.dao.insertar(any()) }
        coVerify(exactly = 0) { m.asignacionDao.insertarEstudiante(any()) }
    }

    "los filtros deben aplicarse al tocar Filtrar y reiniciarse" {
        val viewModel = catalogoViewModelDePrueba(MocksCatalogo())
        viewModel.onModuloChange("Matemática")
        viewModel.onCursoChange("Todos")
        viewModel.onContenidoChange("Todos")
        viewModel.onDificultadChange("Todos")
        viewModel.state.value.actividadesVisibles.size shouldBe actividadesIniciales.size
        viewModel.onFiltrarClick()
        viewModel.state.value.actividadesVisibles.all { it.modulo == "Matemática" } shouldBe true
        viewModel.reiniciarFiltros()
        viewModel.state.value.moduloAplicado shouldBe "Todos"
    }

    "Desactivar y Activar deben cambiar el estado en Room" {
        val m = MocksCatalogo()
        val desactivada = actividadesIniciales.first().copy(codigo = "LEC-004", estado = "desactivada")
        val borradorCompleto = actividadesIniciales.first().copy(codigo = "LEC-005", estado = "borrador")
        every { m.dao.observarTodas() } returns flowOf(actividadesIniciales + desactivada + borradorCompleto)
        val viewModel = catalogoViewModelDePrueba(m)
        viewModel.onCambiarEstado("LEC-001")
        coVerify { m.dao.actualizar(match { it.codigo == "LEC-001" && it.estado == "desactivada" }) }
        viewModel.onCambiarEstado("LEC-004")
        coVerify { m.dao.actualizar(match { it.codigo == "LEC-004" && it.estado == "disponible" }) }
        viewModel.onCambiarEstado("LEC-005")
        coVerify { m.dao.actualizar(match { it.codigo == "LEC-005" && it.estado == "disponible" }) }
        viewModel.onCambiarEstado("LEC-999")
        coVerify(exactly = 0) { m.dao.actualizar(match { it.codigo == "LEC-999" }) }
    }

    "un borrador incompleto no se puede activar y el aviso dice qué falta" {
        val viewModel = catalogoViewModelDePrueba(MocksCatalogo())
        viewModel.onCambiarEstado("LEC-003")
        viewModel.state.value.mensajeError shouldBe "Error: Falta decidir el curso, el contenido, el objetivo pedagógico, " +
                "la dificultad, las instrucciones, la cantidad de preguntas y los intentos permitidos"
        viewModel.onCerrarMensaje()
        viewModel.state.value.mensajeError shouldBe null
    }

    "si Room falla al cambiar el estado, la app no se cae" {
        val m = MocksCatalogo()
        coEvery { m.dao.actualizar(any()) } throws RuntimeException("base cerrada")
        val viewModel = catalogoViewModelDePrueba(m)
        viewModel.onCambiarEstado("LEC-001")
        viewModel.state.value.mensajeError shouldBe null
    }

    "el formulario debe pedir la pregunta completa y respetar la cantidad indicada" {
        val viewModel = catalogoViewModelDePrueba(MocksCatalogo())
        viewModel.onAgregarClick()
        viewModel.onAgregarPregunta()
        viewModel.state.value.formulario.errores.nuevaPregunta shouldBe "Completa la pregunta, las tres alternativas y marca la correcta."
        llenarNueva(viewModel)
        viewModel.state.value.formulario.preguntas.size shouldBe 1
        viewModel.onNuevaPreguntaChange("¿Otra?")
        viewModel.onNuevaAlternativaAChange("a")
        viewModel.onNuevaAlternativaBChange("b")
        viewModel.onNuevaAlternativaCChange("c")
        viewModel.onNuevaCorrectaChange(1)
        viewModel.onAgregarPregunta()
        viewModel.state.value.formulario.errores.cantidadPreguntas shouldBe "Ya agregaste las 1 preguntas indicadas. Aumenta la cantidad para agregar más."
        viewModel.onFormCantidadPreguntasChange("2")
        viewModel.onAgregarPregunta()
        viewModel.state.value.formulario.preguntas.size shouldBe 2
        viewModel.onQuitarPregunta(0)
        viewModel.state.value.formulario.preguntas.map { it.enunciado } shouldBe listOf("¿Otra?")
    }

    "sin sesión, guardar debe pedir volver a iniciar sesión" {
        val viewModel = catalogoViewModelDePrueba(MocksCatalogo())
        llenarNueva(viewModel)
        viewModel.onGuardarFormulario(" ") shouldBe false
        viewModel.state.value.mensajeError shouldBe "Vuelve a iniciar sesión."
    }

    "guardar debe validar el identificador y los números" {
        val viewModel = catalogoViewModelDePrueba(MocksCatalogo())
        llenarNueva(viewModel, codigo = "")
        viewModel.onGuardarFormulario("docente") shouldBe false
        viewModel.state.value.formulario.errores.codigo shouldBe "Completa este campo."
        viewModel.onFormCodigoChange("ABC")
        viewModel.onGuardarFormulario("docente")
        viewModel.state.value.formulario.errores.codigo shouldBe "Usa el formato LEC-### o MAT-### (por ejemplo, LEC-001)."
        viewModel.onFormCodigoChange("MAT-090")
        viewModel.onGuardarFormulario("docente")
        viewModel.state.value.formulario.errores.codigo shouldBe "Para Lectura usa LEC-### y para Matemática usa MAT-###."
        viewModel.onFormCodigoChange("LEC-001")
        viewModel.onFormIntentosPermitidosChange("0")
        viewModel.onFormCantidadPreguntasChange("3")
        viewModel.onGuardarFormulario("docente")
        viewModel.state.value.formulario.errores.codigo shouldBe "Este identificador ya existe."
        viewModel.state.value.formulario.errores.intentosPermitidos shouldBe "Ingresa un número entero mayor que 0."
        viewModel.state.value.formulario.errores.cantidadPreguntas shouldBe "Indicaste 3 preguntas, pero hay 1. Agrega o quita preguntas."
        viewModel.onFormCantidadPreguntasChange("tres")
        viewModel.onGuardarFormulario("docente")
        viewModel.state.value.formulario.errores.cantidadPreguntas shouldBe "Ingresa un número entero mayor que 0."
    }

    "una actividad nueva completa debe guardarse en Room con sus preguntas" {
        val m = MocksCatalogo()
        val viewModel = catalogoViewModelDePrueba(m)
        llenarNueva(viewModel)
        viewModel.onGuardarFormulario("docente") shouldBe true
        coVerify { m.dao.insertar(match { it.codigo == "LEC-090" && it.estado == "disponible" && it.docente == "docente" }) }
        coVerify { m.dao.borrarPreguntasDe("LEC-090") }
        coVerify { m.dao.insertarPregunta(match { it.actividadCodigo == "LEC-090" && it.enunciado == "¿Pregunta?" }) }
        viewModel.state.value.mensajeExito shouldBe "Actividad creada"
        viewModel.onCerrarMensajeExito()
        viewModel.state.value.mensajeExito shouldBe null
    }

    "con estado Desactivada, la actividad nueva se guarda desactivada" {
        val m = MocksCatalogo()
        val viewModel = catalogoViewModelDePrueba(m)
        llenarNueva(viewModel, estado = "Desactivada")
        viewModel.onGuardarFormulario("docente")
        coVerify { m.dao.insertar(match { it.codigo == "LEC-090" && it.estado == "desactivada" }) }
    }

    "sin estado elegido, la actividad se guarda como borrador" {
        val m = MocksCatalogo()
        val viewModel = catalogoViewModelDePrueba(m)
        llenarNueva(viewModel, estado = "")
        viewModel.onGuardarFormulario("docente")
        coVerify { m.dao.insertar(match { it.codigo == "LEC-090" && it.estado == "borrador" }) }
        viewModel.state.value.mensajeExito shouldBe "Actividad creada como borrador porque faltan datos"
    }

    "si Room falla al guardar, la app no se cae" {
        val m = MocksCatalogo()
        val viewModel = catalogoViewModelDePrueba(m)
        coEvery { m.dao.insertar(any()) } throws RuntimeException("base cerrada")
        llenarNueva(viewModel)
        viewModel.onGuardarFormulario("docente") shouldBe true
        coVerify(exactly = 0) { m.dao.insertarPregunta(match { it.actividadCodigo == "LEC-090" }) }
    }

    "editar debe cargar la actividad en el formulario y no dejar cambiar el módulo" {
        val m = MocksCatalogo()
        val viewModel = catalogoViewModelDePrueba(m)
        viewModel.onEditarClick(actividadesIniciales.first())
        viewModel.state.value.formulario.codigo shouldBe "LEC-001"
        viewModel.state.value.formulario.estado shouldBe "Disponible"
        viewModel.state.value.formulario.cantidadPreguntas shouldBe "4"
        viewModel.state.value.formulario.preguntas.size shouldBe 4
        viewModel.onFormModuloChange("Matemática")
        viewModel.onGuardarFormulario("docente") shouldBe false
        viewModel.state.value.formulario.errores.modulo shouldBe "El identificador LEC-001 corresponde a Lectura. No puedes cambiar el módulo a Matemática."
        viewModel.onFormModuloChange("Lectura")
        viewModel.onGuardarFormulario("docente") shouldBe true
        coVerify { m.dao.actualizar(match { it.codigo == "LEC-001" }) }
        viewModel.state.value.mensajeExito shouldBe "Actividad guardada"
    }

    "editar Matemática, un borrador o una desactivada muestra su estado en el formulario" {
        val viewModel = catalogoViewModelDePrueba(MocksCatalogo())
        viewModel.onEditarClick(actividadesIniciales.first { it.codigo == "MAT-001" })
        viewModel.onFormModuloChange("Lectura")
        viewModel.onGuardarFormulario("docente")
        viewModel.state.value.formulario.errores.modulo shouldBe "El identificador MAT-001 corresponde a Matemática. No puedes cambiar el módulo a Lectura."
        viewModel.onEditarClick(actividadesIniciales.first { it.codigo == "LEC-003" })
        viewModel.state.value.formulario.estado shouldBe ""
        viewModel.state.value.formulario.cantidadPreguntas shouldBe ""
        viewModel.onEditarClick(actividadesIniciales.first().copy(estado = "desactivada"))
        viewModel.state.value.formulario.estado shouldBe "Desactivada"
    }


    "al abrir el Catálogo, debe leer las actividades del servidor (GET)" {
        val viewModel = catalogoViewModelDePrueba(MocksCatalogo())
        viewModel.cargarServidor()
        viewModel.state.value.servidorConectado shouldBe true
        viewModel.state.value.actividadesServidor.size shouldBe 4
    }

    "sin conexión con el servidor, el Catálogo lo avisa y sigue mostrando lo que hay en Room" {
        val m = MocksCatalogo()
        coEvery { m.actividadRepository.obtenerActividades() } throws IOException("Sin conexión")
        val viewModel = catalogoViewModelDePrueba(m)
        viewModel.cargarServidor()
        viewModel.state.value.servidorConectado shouldBe false
        viewModel.state.value.actividadesServidor.size shouldBe 0
        viewModel.state.value.actividades.size shouldBe actividadesIniciales.size
    }

    "una actividad nueva se guarda en Room y después se envía al servidor con POST y su pregunta" {
        val m = MocksCatalogo()
        val nueva = actividadesIniciales.first().copy(codigo = "LEC-090", nombre = "Nueva")
        coEvery { m.dao.buscar("LEC-090") } returns nueva
        coEvery { m.dao.obtenerPreguntas("LEC-090") } returns listOf(Pregunta(actividadCodigo = "LEC-090", enunciado = "¿Pregunta?"))
        val viewModel = catalogoViewModelDePrueba(m)
        llenarNueva(viewModel)
        viewModel.onGuardarFormulario("docente") shouldBe true
        coVerify { m.dao.insertar(match { it.codigo == "LEC-090" }) }
        coVerify { m.actividadRepository.crearActividad(nueva) }
        coVerify { m.actividadRepository.crearPregunta(match { it.actividadCodigo == "LEC-090" && it.enunciado == "¿Pregunta?" }) }
        coVerify(exactly = 0) { m.actividadRepository.actualizarActividad(any(), any()) }
        viewModel.state.value.pendientes.size shouldBe 0
        viewModel.state.value.enviando shouldBe false
        coVerify { m.actividadRepository.obtenerActividades() }
    }

    "editar una actividad que el servidor ya tiene la envía con PUT y su id, y le cambia las preguntas" {
        val m = MocksCatalogo()
        coEvery { m.actividadRepository.obtenerPreguntas("LEC-001") } returns listOf(
            Pregunta(id = 11, actividadCodigo = "LEC-001"),
            Pregunta(id = 12, actividadCodigo = "LEC-001")
        )
        val viewModel = catalogoViewModelDePrueba(m)
        viewModel.onEditarClick(actividadesIniciales.first())
        viewModel.onGuardarFormulario("docente") shouldBe true
        coVerify { m.dao.actualizar(match { it.codigo == "LEC-001" }) }
        coVerify { m.actividadRepository.actualizarActividad(1, actividadesIniciales.first()) }
        coVerify { m.actividadRepository.eliminarPregunta(11) }
        coVerify { m.actividadRepository.eliminarPregunta(12) }
        coVerify(exactly = 4) { m.actividadRepository.crearPregunta(match { it.actividadCodigo == "LEC-001" }) }
        coVerify(exactly = 0) { m.actividadRepository.crearActividad(any()) }
        viewModel.state.value.pendientes.size shouldBe 0
    }

    "al desactivar, el cambio también se envía al servidor con PUT" {
        val m = MocksCatalogo()
        val desactivada = actividadesIniciales.first().copy(estado = "desactivada")
        coEvery { m.dao.buscar("LEC-001") } returns desactivada
        val viewModel = catalogoViewModelDePrueba(m)
        viewModel.onCambiarEstado("LEC-001")
        coVerify { m.actividadRepository.actualizarActividad(1, desactivada) }
        viewModel.state.value.pendientes.size shouldBe 0
        viewModel.state.value.enviando shouldBe false
    }

    "sin conexión al enviar, la actividad queda guardada en la tablet y pendiente" {
        val m = MocksCatalogo()
        coEvery { m.dao.buscar("LEC-090") } returns actividadesIniciales.first().copy(codigo = "LEC-090")
        coEvery { m.actividadRepository.crearActividad(any()) } throws IOException("Sin conexión")
        val viewModel = catalogoViewModelDePrueba(m)
        llenarNueva(viewModel)
        viewModel.onGuardarFormulario("docente") shouldBe true
        coVerify { m.dao.insertar(match { it.codigo == "LEC-090" }) }
        viewModel.state.value.mensajeExito shouldBe null
        viewModel.state.value.mensajeAlerta shouldBe "Actividad guardada en local. Reintenta guardarla en el servidor."
        viewModel.state.value.pendientes shouldBe listOf("LEC-090")
        viewModel.state.value.errorServidor shouldBe "No se pudo enviar LEC-090 porque no hay conexión con el servidor."
        viewModel.state.value.enviando shouldBe false
    }

    "si editar no se pudo enviar, Reintentar la vuelve a enviar y deja de estar pendiente" {
        val m = MocksCatalogo()
        coEvery { m.actividadRepository.actualizarActividad(any(), any()) } throws IOException("Sin conexión")
        val viewModel = catalogoViewModelDePrueba(m)
        viewModel.onEditarClick(actividadesIniciales.first())
        viewModel.onGuardarFormulario("docente")
        viewModel.state.value.pendientes shouldBe listOf("LEC-001")
        viewModel.onReintentar()
        viewModel.state.value.pendientes shouldBe listOf("LEC-001")
        viewModel.state.value.mensajeAlerta shouldBe "No tienes conexión, intenta más tarde."
        coEvery { m.actividadRepository.actualizarActividad(any(), any()) } returns ActividadResponse(1, "LEC-001")
        viewModel.onReintentar()
        coVerify(exactly = 3) { m.actividadRepository.actualizarActividad(1, any()) }
        viewModel.state.value.pendientes.size shouldBe 0
        viewModel.state.value.mensajeExito shouldBe "Actividad subida al servidor."
        viewModel.state.value.enviando shouldBe false
    }

    "si el servidor rechaza la actividad (error 400), queda pendiente con el número del error" {
        val m = MocksCatalogo()
        coEvery { m.dao.buscar("LEC-090") } returns actividadesIniciales.first().copy(codigo = "LEC-090")
        coEvery { m.actividadRepository.crearActividad(any()) } throws errorHttp(400)
        val viewModel = catalogoViewModelDePrueba(m)
        llenarNueva(viewModel)
        viewModel.onGuardarFormulario("docente")
        viewModel.state.value.pendientes shouldBe listOf("LEC-090")
        viewModel.state.value.errorServidor shouldBe "El servidor no aceptó LEC-090 (error 400)."
        viewModel.state.value.mensajeAlerta shouldBe "Actividad guardada en local. Reintenta guardarla en el servidor."
        viewModel.onCerrarAlerta()
        viewModel.state.value.mensajeAlerta shouldBe null
    }

    "si el servidor rechaza el PUT (error 400), sus preguntas en el servidor no se borran" {
        val m = MocksCatalogo()
        coEvery { m.actividadRepository.obtenerPreguntas("LEC-001") } returns listOf(
            Pregunta(id = 11, actividadCodigo = "LEC-001"),
            Pregunta(id = 12, actividadCodigo = "LEC-001")
        )
        coEvery { m.actividadRepository.actualizarActividad(any(), any()) } throws errorHttp(400)
        val viewModel = catalogoViewModelDePrueba(m)
        viewModel.onEditarClick(actividadesIniciales.first())
        viewModel.onFormNombreChange("a".repeat(151))
        viewModel.onGuardarFormulario("docente") shouldBe true
        viewModel.state.value.pendientes shouldBe listOf("LEC-001")
        viewModel.state.value.errorServidor shouldBe "El servidor no aceptó LEC-001 (error 400)."
        coVerify(exactly = 0) { m.actividadRepository.eliminarPregunta(any()) }
        coVerify(exactly = 0) { m.actividadRepository.crearPregunta(any()) }
    }

    "una actividad asignada no se puede eliminar y aparece el aviso" {
        val m = MocksCatalogo()
        val viewModel = catalogoViewModelDePrueba(m)
        viewModel.onEliminarClick(actividadesIniciales.first())
        viewModel.state.value.mensajeError shouldBe "No se puede eliminar «El perro Tito» porque está asignada a un estudiante."
        viewModel.state.value.codigoPorEliminar shouldBe null
        coVerify(exactly = 0) { m.dao.borrarActividad(any()) }
    }

    "Eliminar pide confirmar, y Cancelar no borra nada" {
        val m = MocksCatalogo()
        val viewModel = catalogoViewModelDePrueba(m)
        val lec2 = actividadesIniciales.first { it.codigo == "LEC-002" }
        viewModel.onEliminarClick(lec2)
        viewModel.state.value.actividadPorEliminar shouldBe lec2
        viewModel.onCancelarEliminar()
        viewModel.state.value.codigoPorEliminar shouldBe null
        viewModel.onConfirmarEliminar()
        coVerify(exactly = 0) { m.dao.borrarActividad(any()) }
        coVerify(exactly = 0) { m.actividadRepository.eliminarActividad(any()) }
    }

    "al confirmar, se borra en la tablet y en el servidor: sus preguntas y la actividad (DELETE)" {
        val m = MocksCatalogo()
        coEvery { m.dao.buscar("LEC-002") } returns null
        coEvery { m.actividadRepository.obtenerPreguntas("LEC-002") } returns listOf(
            Pregunta(id = 21, actividadCodigo = "LEC-002"),
            Pregunta(id = 22, actividadCodigo = "LEC-002")
        )
        val viewModel = catalogoViewModelDePrueba(m)
        viewModel.onEliminarClick(actividadesIniciales.first { it.codigo == "LEC-002" })
        viewModel.onConfirmarEliminar()
        coVerify { m.dao.borrarPreguntasDe("LEC-002") }
        coVerify { m.dao.borrarActividad("LEC-002") }
        coVerify { m.actividadRepository.eliminarPregunta(21) }
        coVerify { m.actividadRepository.eliminarPregunta(22) }
        coVerify { m.actividadRepository.eliminarActividad(2) }
        viewModel.state.value.mensajeExito shouldBe "Actividad eliminada."
        viewModel.state.value.codigoPorEliminar shouldBe null
        viewModel.state.value.pendientes.size shouldBe 0
        viewModel.state.value.enviando shouldBe false
    }

    "si el servidor no responde al eliminar, queda pendiente y Reintentar la borra" {
        val m = MocksCatalogo()
        coEvery { m.dao.buscar("LEC-002") } returns null
        coEvery { m.actividadRepository.eliminarActividad(any()) } throws IOException("Sin conexión")
        val viewModel = catalogoViewModelDePrueba(m)
        viewModel.onEliminarClick(actividadesIniciales.first { it.codigo == "LEC-002" })
        viewModel.onConfirmarEliminar()
        viewModel.state.value.mensajeExito shouldBe null
        viewModel.state.value.mensajeAlerta shouldBe "Actividad eliminada en local. Reintenta eliminarla en el servidor."
        viewModel.state.value.pendientes shouldBe listOf("LEC-002")
        coEvery { m.actividadRepository.eliminarActividad(any()) } returns Unit
        viewModel.onReintentar()
        coVerify(exactly = 2) { m.actividadRepository.eliminarActividad(2) }
        viewModel.state.value.pendientes.size shouldBe 0
    }

    "si Room falla al eliminar, la actividad no se borra del servidor" {
        val m = MocksCatalogo()
        coEvery { m.dao.borrarActividad(any()) } throws RuntimeException("base cerrada")
        val viewModel = catalogoViewModelDePrueba(m)
        viewModel.onEliminarClick(actividadesIniciales.first { it.codigo == "LEC-002" })
        viewModel.onConfirmarEliminar()
        viewModel.state.value.mensajeExito shouldBe null
        coVerify { m.actividadRepository.actualizarActividad(2, any()) }
        coVerify(exactly = 0) { m.actividadRepository.eliminarActividad(any()) }
    }

    "mientras se envía al servidor, se ve la ventana de carga y un segundo toque no hace nada" {
        val m = MocksCatalogo()
        coEvery { m.dao.buscar("LEC-090") } returns actividadesIniciales.first().copy(codigo = "LEC-090")
        val viewModel = catalogoViewModelDePrueba(m)
        var cargaVisible = false
        var segundoGuardar = true
        coEvery { m.actividadRepository.crearActividad(any()) } answers {
            cargaVisible = viewModel.state.value.enviando
            segundoGuardar = viewModel.onGuardarFormulario("docente")
            viewModel.onCambiarEstado("LEC-001")
            viewModel.onEliminarClick(actividadesIniciales.first { it.codigo == "LEC-002" })
            viewModel.onReintentar()
            ActividadResponse(5, "LEC-090")
        }
        llenarNueva(viewModel)
        viewModel.onGuardarFormulario("docente") shouldBe true
        cargaVisible shouldBe true
        segundoGuardar shouldBe false
        coVerify(exactly = 1) { m.dao.insertar(match { it.codigo == "LEC-090" }) }
        coVerify(exactly = 0) { m.dao.actualizar(any()) }
        viewModel.state.value.codigoPorEliminar shouldBe null
        coVerify(exactly = 1) { m.actividadRepository.obtenerActividades() }
        viewModel.state.value.enviando shouldBe false
    }

    "los filtros del asistente y la revisión de una actividad" {
        val viewModel = catalogoViewModelDePrueba(MocksCatalogo())
        viewModel.onAsistenteModuloChange("Lectura")
        viewModel.onAsistenteCursoChange("Todos")
        viewModel.onAsistenteContenidoChange("Todos")
        viewModel.onAsistenteDificultadChange("Todos")
        viewModel.onAsistenteFiltrarClick()
        viewModel.state.value.actividadesParaAsignar.all { it.modulo == "Lectura" } shouldBe true
        viewModel.reiniciarFiltrosAsistente()
        viewModel.state.value.asistente.moduloAplicado shouldBe "Todos"
        viewModel.onRevisarClick(actividadesIniciales.first())
        viewModel.state.value.actividadRevisada shouldBe actividadesIniciales.first()
    }

    "tocar una tarjeta del resumen filtra las asignaciones por estado, y tocarla otra vez quita el filtro" {
        val viewModel = catalogoViewModelDePrueba(MocksCatalogo())

        viewModel.onFiltroEstadoClick("finalizada")
        viewModel.state.value.asistente.asignacionesVisibles.size shouldBe 2
        viewModel.state.value.asistente.asignacionesVisibles.all { it.estado == "finalizada" } shouldBe true

        viewModel.onFiltroEstadoClick("finalizada")
        viewModel.state.value.asistente.filtroEstado shouldBe ""
        viewModel.state.value.asistente.asignacionesVisibles.size shouldBe asignacionesIniciales.size
    }

    "la observación del asistente se guarda en Room" {
        val m = MocksCatalogo()
        val finalizada = asignacionesIniciales.first { it.codigoTemporal == "100005" }
        coEvery { m.asignacionDao.buscarAsignacion("100005") } returns finalizada
        val viewModel = catalogoViewModelDePrueba(m)
        viewModel.onAsignacionClick(finalizada)
        viewModel.state.value.asignacionRevisada shouldBe finalizada
        viewModel.state.value.asistente.observacion shouldBe "ejemplo de observación 1"
        viewModel.onObservacionChange(" ")
        viewModel.onGuardarObservacion()
        viewModel.state.value.asistente.errorObservacion shouldBe "Escribe la observación."
        viewModel.onObservacionChange("Leyó con calma")
        viewModel.state.value.asistente.errorObservacion shouldBe null
        viewModel.onGuardarObservacion()
        coVerify { m.asignacionDao.actualizarAsignacion(finalizada.copy(observacion = "Leyó con calma")) }
        viewModel.state.value.asistente.mensajeExito shouldBe "Observación guardada."
    }

    "si la asignación no está o Room falla, la observación muestra el aviso" {
        val m = MocksCatalogo()
        val finalizada = asignacionesIniciales.first { it.codigoTemporal == "100005" }
        val viewModel = catalogoViewModelDePrueba(m)
        viewModel.onAsignacionClick(finalizada)
        viewModel.onObservacionChange("Leyó con calma")
        viewModel.onGuardarObservacion()
        viewModel.state.value.asistente.mensajeError shouldBe "No se encontró la asignación."
        coEvery { m.asignacionDao.buscarAsignacion("100005") } returns finalizada
        coEvery { m.asignacionDao.actualizarAsignacion(any()) } throws RuntimeException("base cerrada")
        viewModel.onGuardarObservacion()
        viewModel.state.value.asistente.mensajeError shouldBe "No se pudo guardar la observación. Inténtalo de nuevo."
        viewModel.onCerrarErrorAsistente()
        viewModel.state.value.asistente.mensajeError shouldBe null
    }

    "sin sesión, asignar debe pedir volver a iniciar sesión" {
        val viewModel = catalogoViewModelDePrueba(MocksCatalogo())
        llenarAsignacion(viewModel)
        viewModel.onAsignarClick(" ") shouldBe false
        viewModel.state.value.asistente.mensajeError shouldBe "Vuelve a iniciar sesión."
    }

    "asignar debe validar el estudiante, la hora límite y los minutos" {
        val viewModel = catalogoViewModelDePrueba(MocksCatalogo())
        viewModel.onSeleccionarActividad(actividadesIniciales.first { it.codigo == "LEC-002" })
        viewModel.onAsignarClick("asistente1") shouldBe false
        viewModel.state.value.asistente.formulario.errores.estudiante shouldBe "Selecciona un estudiante."
        viewModel.state.value.asistente.formulario.errores.fecha shouldBe "Escribe la hora límite de la actividad"
        viewModel.state.value.asistente.formulario.errores.minutos shouldBe "Ingresa los minutos que durará la actividad."
        viewModel.onAsignarFechaChange("31-12-2099")
        viewModel.onAsignarClick("asistente1")
        viewModel.state.value.asistente.formulario.errores.fecha shouldBe "La fecha está mal escrita o no existe. Escríbela como dd/mm/yyyy hh:mm"
        viewModel.onAsignarFechaChange("31/02/2099 10:00")
        viewModel.onAsignarClick("asistente1")
        viewModel.state.value.asistente.formulario.errores.fecha shouldBe "La fecha está mal escrita o no existe. Escríbela como dd/mm/yyyy hh:mm"
        viewModel.onAsignarFechaChange("01/01/2020 10:00")
        viewModel.onAsignarClick("asistente1")
        viewModel.state.value.asistente.formulario.errores.fecha shouldBe "La hora límite ya pasó"
    }

    "no se asigna dos veces la misma actividad a un estudiante si no ha terminado" {
        val viewModel = catalogoViewModelDePrueba(MocksCatalogo())
        viewModel.onSeleccionarActividad(actividadesIniciales.first())
        viewModel.onAsignarEstudianteChange("EST-001 · Estudiante ficticio 1")
        viewModel.onAsignarFechaChange("31/12/2099 23:59")
        viewModel.onAsignarMinutosChange("45")
        viewModel.onAsignarClick("asistente1") shouldBe false
        viewModel.state.value.asistente.formulario.errores.estudiante shouldBe "Este estudiante ya tiene asignada esta actividad."
    }

    "asignar con datos correctos debe crear el código temporal y guardarlo en Room" {
        val m = MocksCatalogo()
        val viewModel = catalogoViewModelDePrueba(m)
        val guardada = slot<Asignacion>()
        coEvery { m.asignacionDao.insertarAsignacion(capture(guardada)) } returns Unit
        llenarAsignacion(viewModel)
        viewModel.onAsignarClick("asistente1") shouldBe true
        guardada.captured.codigoTemporal.length shouldBe 6
        guardada.captured.estudianteCodigo shouldBe "EST-001"
        guardada.captured.actividadCodigo shouldBe "LEC-002"
        guardada.captured.asistente shouldBe "asistente1"
        guardada.captured.estado shouldBe "pendiente"
        guardada.captured.minutosDuracion shouldBe 45
        viewModel.state.value.asistente.mensajeExito shouldBe "Actividad asignada. Código temporal: ${guardada.captured.codigoTemporal}"
        viewModel.onCerrarMensajeAsistente()
        viewModel.state.value.asistente.mensajeExito shouldBe null
    }

    "si el código temporal ya existe, se prueba con el siguiente" {
        val m = MocksCatalogo()
        var consultas = 0
        coEvery { m.asignacionDao.buscarAsignacion(any()) } answers {
            consultas = consultas + 1
            if (consultas == 1) asignacionesIniciales.first() else null
        }
        val viewModel = catalogoViewModelDePrueba(m)
        llenarAsignacion(viewModel)
        viewModel.onAsignarClick("asistente1")
        coVerify(exactly = 2) { m.asignacionDao.buscarAsignacion(any()) }
    }

    "si Room falla al asignar, aparece el aviso" {
        val m = MocksCatalogo()
        val viewModel = catalogoViewModelDePrueba(m)
        coEvery { m.asignacionDao.insertarAsignacion(any()) } throws RuntimeException("base cerrada")
        llenarAsignacion(viewModel)
        viewModel.onAsignarClick("asistente1")
        viewModel.state.value.asistente.mensajeError shouldBe "No se pudo asignar la actividad. Inténtalo de nuevo."
    }
})
