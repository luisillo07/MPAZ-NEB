# MPAZ NEB

Aplicación Android de nivelación en Lectura y Matemática para estudiantes de 1.° a 4.° básico de la Escuela Escritora Marcela Paz. Se poseen tres perfiles: docente crea las actividades, asistente de aula las asigna y estudiante las responde con intentos y retroalimentación.

Proyecto del caso semestral de Desarrollo de Aplicaciones Móviles (DSY1105), Duoc UC. Todos los datos son ficticios: no hay nombres reales, RUT, diagnósticos ni notas.

## Integrantes:

- Javier Espinoza
- Luis Laffertte
- Benjamín Neira

## Funcionalidades implementadas:

### General

- Pantalla de carga con el logo de la escuela y el texto "Cargando...".
- Elección de perfil: Administración pedagógica (docente), Asistente de aula o Estudiante.
- Inicio de sesión con usuario y contraseña guardados en la base de datos local. Cada campo valida desde el ViewModel y muestra un ícono y un mensaje de error. Cada perfil entra a su propia pantalla.
- Diseño adaptable a teléfono y tableta (Window Size Class), y desplazamiento en horizontal.

### Estudiante

- Mis actividades: lista de actividades disponibles, con su estado y los intentos usados ("Intentos: N de M").
- Instrucciones y preguntas de Lectura (con texto de lectura) y de Matemática, con alternativas A, B y C, y una barra de avance.
- Respuesta obligatoria: no se puede avanzar sin elegir una alternativa (ícono + mensaje de aviso).
- Confirmación antes de enviar, control de intentos (Reintentar o Siguiente) y retroalimentación por pregunta.
- Pantallazo desde la Galería: el estudiante adjunta un pantallazo de la pregunta que más le costó.
- Guardar resumen en Archivos: crea un archivo de texto con el resultado del intento.

### Docente (Administración pedagógica)

- Catálogo de actividades con filtros por módulo, curso, contenido y dificultad.
- Crear y editar actividades con formulario validado por campo (código LEC-### o MAT-### según el módulo, cantidad de preguntas e intentos permitidos), y agregar o quitar preguntas.
- Activar o desactivar una actividad; el estudiante solo ve las disponibles.
- Revisar una actividad con todas sus preguntas.

### Asistente de aula

- Asignaciones: lista de actividades asignadas a estudiantes ficticios, con estado (pendiente, en desarrollo, finalizada).
- Asignar actividad: elegir una actividad con filtros, un estudiante y una fecha. Se genera un código temporal de 6 dígitos.
- Resumen de la asignación con las respuestas, aciertos y errores.

## Aspectos técnicos

- Lenguaje y UI: Kotlin + Jetpack Compose (Material 3).
- Arquitectura MVVM: cada pantalla recibe su ViewModel; el estado vive en StateFlow y la validación está en los ViewModels, no en las pantallas.
- Navegación: Navigation Compose con rutas centralizadas en navigation/Screen.kt.
- Persistencia local: Room, base mpazneb_database con 7 tablas (usuarios, actividades, preguntas, resultados, estudiantes, asignaciones y respuestas). Los datos de ejemplo se cargan la primera vez que se abre la app.
- Animaciones: cambio de color al elegir una alternativa (animateColorAsState) y mensajes que aparecen con AnimatedVisibility.
- Recursos nativos: Galería (GetContent + Coil para mostrar la imagen) y Archivos (CreateDocument). No se usan cámara, micrófono, GPS ni contactos, como pide el caso.

## Estructura de carpetas:

```
app/src/main/java/com/example/mpaz_real_real/
├── data/local/        Room: AppDatabase, dao/, entity/ y datos de ejemplo
├── model/             clases de datos y estados de pantalla (UiState)
├── navigation/        Screen.kt (rutas) y AppNavigation.kt (NavHost)
├── repository/        usuarios de prueba
├── ui/components/     componentes reutilizables
├── ui/screen/         pantallas
├── ui/theme/          colores, tipografía y tema
├── ui/utils/          utilidades de diseño (tamaño de ventana, colores por módulo)
└── viewmodel/         Carga, Login, Actividad y Catalogo
```

## Pasos para ejecutar:

1. Clonar el repositorio: git clone https://github.com/luisillo07/MPAZ-NEB.git
2. Abrir la carpeta del proyecto en Android Studio y esperar a que termine Gradle Sync.
3. Elegir un emulador o teléfono con Android 10 (API 29) o superior.
4. Seleccionar la configuración app y presionar Run.
5. Ingresar con alguna de las cuentas de prueba:

- Administración pedagógica: Usuario: docente, Contraseña: 1234
- Asistente de aula: Usuario: asistente, Contraseña: 1234
- Estudiante: Usuario: estudiante, Contraseña: 1234

Si la app ya estaba instalada con una versión anterior, conviene desinstalarla antes de ejecutar, para que se carguen los datos de ejemplo nuevos.

## Próximos pasos pendientes de desarrollo:

- Conectar el código temporal del estudiante con su asignación, para que el estado pase a "en desarrollo" y "finalizada" y el asistente vea las respuestas reales.
- Mostrar al estudiante solo las actividades asignadas a su código.

