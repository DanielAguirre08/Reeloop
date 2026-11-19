# ReLoop · Contexto común del equipo (versión final)

> **Para qué sirve este archivo.** Los 4 integrantes usamos una IA distinta. Para que el código de los cuatro encaje, **todos pegamos este archivo primero** en nuestra IA y después el archivo de nuestro módulo. Lo que dice aquí es contrato: no se cambia sin avisar a Daniel (módulo A).

---

## 1. Qué es ReLoop

App Android de economía circular (ODS 12) para la comunidad universitaria. Un usuario publica un objeto que ya no usa; otro lo solicita; el dueño acepta; se encuentran y la entrega se confirma con un código QR. Cada objeto guarda su historia (Pasaporte Circular) y cada usuario ve su EcoImpacto.

Curso: Arquitectura de aplicaciones móviles (UPN), docente Mg. Narváez Villacorta. Grupo 05.

### Ciclo de vida de un objeto (regla central del negocio)

```
            solicitar              aceptar                entregar (código QR)
Disponible ───────────► Solicitado ───────────► Aceptado ───────────────────────► Entregado
     ▲                       │
     └─────── rechazar ──────┘
```

| Acción | Quién puede | Estado requerido | Resultado |
|---|---|---|---|
| publicar | cualquier usuario con sesión | — | nace `Disponible` |
| solicitar | cualquiera **menos** el propietario | `Disponible` | `Solicitado`, guarda solicitante y fecha |
| aceptar | solo el propietario | `Solicitado` | `Aceptado`, el servidor genera `codigoEntrega` (6 dígitos) |
| rechazar | solo el propietario | `Solicitado` | vuelve a `Disponible`, borra solicitante |
| entregar | solo el solicitante, con el código correcto | `Aceptado` | `Entregado`, guarda fecha |
| eliminar | solo el propietario | `Disponible` | se borra |

Quién muestra y quién escanea: **el propietario muestra el QR**, **el solicitante lo escanea** (o escribe los 6 dígitos).

---

## 2. Reglas del proyecto (léelas antes de pedirle código a tu IA)

1. **La base es el ejemplo final del profesor** (`proyecto_final_ejemplo`). Misma arquitectura, mismos nombres de capas, mismo estilo. Si algo se puede hacer con lo que enseñó el profesor, se hace así.
2. **Nadie agrega librerías.** Las únicas permitidas están en la sección 3. Si tu IA propone otra (Coil, Ktor, Koin, DataStore, Moshi, Accompanist, Firebase…), dile que no.
3. **Cada uno trabaja solo dentro de sus carpetas** (sección 9). Los archivos compartidos los edita solo Daniel.
4. **Archivos cortos:** máximo ~200 líneas por archivo. Si una pantalla crece, se parte en `XxxScreen.kt` + `XxxComponentes.kt`.
5. **Todo en español** (nombres de variables, funciones y textos), como ya está el proyecto.
6. **Tienes que poder explicar tu código.** El profesor no acepta proyectos hechos enteramente por IA. Pídele a tu IA que te explique cada archivo y revisa las "preguntas de defensa" de tu módulo.
7. **No se cambia el diseño visual.** Las 13 pantallas ya existen (versión 1, en `referencia_v1/`). Se conserva su aspecto y se cambia de dónde salen los datos.
8. El paquete es **`com.reeloop.app`** (con doble "e"). No se renombra.

---

## 3. Tecnología y versiones

Copiadas del `libs.versions.toml` del profesor. No se sube ni se baja ninguna por cuenta propia.

| Qué | Librería | Versión |
|---|---|---|
| Lenguaje | Kotlin | 2.2.10 |
| UI | Jetpack Compose + Material 3 (BOM) | la del proyecto |
| Íconos | `material-icons-extended` | la del BOM |
| Navegación | `navigation-compose` | la del proyecto |
| Inyección | Hilt (`hilt-android`, `hilt-android-compiler` con KSP) | 2.60.1 |
| Hilt + Compose | `hilt-navigation-compose` | 1.4.0 |
| KSP | `com.google.devtools.ksp` | 2.3.12 |
| Red | Retrofit + `converter-gson` | 3.0.0 |
| Red (logs) | OkHttp `logging-interceptor` | 4.12.0 |
| ViewModel | `lifecycle-viewmodel-compose` | la del proyecto |
| QR (dibujar) | ZXing `com.google.zxing:core` | 3.5.3 |
| QR (leer) | `com.google.android.gms:play-services-code-scanner` | 16.1.0 |

Las dos últimas son las **únicas** librerías que no vienen del profesor y solo las usa el módulo D. **Room sale del proyecto**: en la versión final los datos viven en la nube (igual que el ejemplo final del profesor, que tampoco usa Room).

Nube: **AWS Academy Learner Lab** → API Gateway (HTTP API) → Lambda (Python) → DynamoDB, región `us-east-1`. Es la misma forma de la API de libros del profesor.

---

## 4. Arquitectura

```
┌──────────────────────────── presentation ────────────────────────────┐
│  Screen (Compose)  ──lee──►  UiState  ◄──actualiza──  ViewModel       │
│        │ llama funciones del ViewModel            (@HiltViewModel)    │
│        └──────────────────────────────────────────────┘│              │
│  UiEventBus ──► AppScaffold muestra el Snackbar        │              │
└────────────────────────────────────────────────────────┼──────────────┘
                                                         ▼
┌──────────────────────────────── domain ──────────────────────────────┐
│  UseCase (una acción)  ──►  Repository (interface)   ·  model (puro)  │
└────────────────────────────────────────────────────────┬──────────────┘
                                                         ▼
┌───────────────────────────────── data ───────────────────────────────┐
│  RepositoryImpl ──► ApiService (Retrofit) ──► DTO ──toDomain()──► model│
│  FakeRepository (memoria, para trabajar sin nube)                     │
│  SesionRepositoryImpl (SharedPreferences)                             │
└───────────────────────────────────────────────────────────────────────┘
        di/  →  Hilt arma todo: NetworkModule + RepositoryModule
```

Regla de dependencias: `presentation → domain ← data`. Una pantalla **nunca** llama a Retrofit ni a un Repository directamente: siempre pasa por un UseCase.

### Árbol de paquetes final

```
com.reeloop.app
├── ReeloopApplication.kt            (@HiltAndroidApp)                      A
├── MainActivity.kt                  (@AndroidEntryPoint)                   A
├── di/            NetworkModule, RepositoryModule                           A
├── domain/
│   ├── model/     Usuario, Objeto, Opciones, ResumenImpacto                 contrato
│   ├── repository/AuthRepository, ObjetoRepository, SesionRepository        contrato
│   └── usecase/   12 casos de uso (sección 6)                               contrato
├── data/
│   ├── remote/api/  AuthApiService, ObjetoApiService                        contrato
│   ├── remote/dto/  ApiResponseDto, UsuarioDto, ObjetoDto, ...              contrato
│   ├── repository/  AuthRepositoryImpl, ObjetoRepositoryImpl, Respuestas    contrato
│   ├── fake/        FakeAuthRepository, FakeObjetoRepository, DatosFake     contrato
│   └── local/       SesionRepositoryImpl                                    A
├── presentation/
│   ├── common/      componentes compartidos (sección 8)                     A
│   ├── event/       UiEvent, UiEventBus                                     A
│   ├── navigation/  Rutas, AppNavigation, BottomNavItem                     A
│   └── screens/
│       ├── login/ registro/ inicio/ impacto/ perfil/                        B
│       ├── explorar/ detalle/ publicar/ misobjetos/                         C
│       └── solicitudes/ codigoqr/ escanearqr/ pasaporte/                    D
├── ui/theme/      Color, Theme, Type                                        A
└── util/          Validacion, CodigoQr, Fechas                              contrato
```

"contrato" = archivos ya escritos y probados que vienen en la carpeta `contrato_kotlin/` del kit. Daniel los copia tal cual en el Hito 0.

---

## 5. Modelos y repositorios (contrato, no se modifican)

```kotlin
// domain/model
data class Usuario(val id: String = "", val nombre: String, val correo: String)

data class Objeto(
    val id: String = "",
    val nombre: String,
    val descripcion: String,
    val categoria: String,
    val conservacion: String,
    val peso: Double,
    val modalidad: String,                    // Opciones.DONACION / PRESTAMO / INTERCAMBIO
    val estado: String = Opciones.DISPONIBLE, // DISPONIBLE / SOLICITADO / ACEPTADO / ENTREGADO
    val propietarioId: String = "",
    val propietarioNombre: String = "",
    val solicitanteId: String = "",
    val solicitanteNombre: String = "",
    val codigoEntrega: String = "",           // solo llega con valor al propietario
    val fechaPublicado: Long = 0L,            // milisegundos; 0 = todavía no ocurrió
    val fechaSolicitado: Long = 0L,
    val fechaEntregado: Long = 0L
)

data class ResumenImpacto(
    val reutilizados: Int = 0, val donaciones: Int = 0, val prestamos: Int = 0,
    val intercambios: Int = 0, val kgEvitados: Double = 0.0,
    val publicados: Int = 0, val solicitudesPendientes: Int = 0
)

object Opciones {   // igual que en la versión 1
    const val DISPONIBLE = "Disponible"; const val SOLICITADO = "Solicitado"
    const val ACEPTADO = "Aceptado";     const val ENTREGADO = "Entregado"
    const val DONACION = "Donación"; const val PRESTAMO = "Préstamo"; const val INTERCAMBIO = "Intercambio"
    val modalidades = listOf(DONACION, PRESTAMO, INTERCAMBIO)
    val categorias = listOf("Académico", "Libros", "Accesorios", "Hogar", "Tecnología", "Ropa", "Otros")
    val conservaciones = listOf("Como nuevo", "Buen estado", "Usado")
}
```

**Cambio importante respecto a la versión 1:** el `id` ahora es `String` (lo genera el servidor), antes era `Int` (Room). Las rutas de navegación usan `NavType.StringType`, igual que el ejemplo del profesor.

```kotlin
// domain/repository
interface AuthRepository {
    suspend fun registrar(nombre: String, correo: String, clave: String): Result<Usuario>
    suspend fun iniciarSesion(correo: String, clave: String): Result<Usuario>
}

interface SesionRepository {                 // quién está usando la app
    val usuario: StateFlow<Usuario?>         // null = sin sesión
    fun guardar(usuario: Usuario)
    fun cerrar()
}

interface ObjetoRepository {
    suspend fun getObjetos(usuarioId: String): Result<List<Objeto>>
    suspend fun getObjetoById(id: String, usuarioId: String): Result<Objeto>
    suspend fun publicar(objeto: Objeto): Result<Objeto>
    suspend fun eliminar(id: String, usuarioId: String): Result<Unit>
    suspend fun solicitar(id: String, solicitante: Usuario): Result<Objeto>
    suspend fun aceptar(id: String, usuarioId: String): Result<Objeto>
    suspend fun rechazar(id: String, usuarioId: String): Result<Objeto>
    suspend fun entregar(id: String, codigo: String, usuarioId: String): Result<Objeto>
}
```

Todo devuelve `Result<T>`, como `LibroRepository` del profesor. Los mensajes de error ya vienen en español dentro del `Result` (`error.message`): **la pantalla solo los muestra, no los inventa**.

---

## 6. Casos de uso disponibles (los ViewModel solo inyectan estos)

Los UseCase ya toman el usuario de la sesión por dentro: **el ViewModel no pasa `usuarioId`**.

| UseCase | Cómo se llama | Devuelve | Valida |
|---|---|---|---|
| `IniciarSesionUseCase` | `(correo, clave)` | `Result<Usuario>` | formato de correo, clave no vacía. Guarda la sesión |
| `RegistrarUsuarioUseCase` | `(nombre, correo, clave)` | `Result<Usuario>` | nombre ≥ 3, correo, clave ≥ 6. Guarda la sesión |
| `CerrarSesionUseCase` | `()` | `Unit` (no es `suspend`) | — |
| `GetObjetosUseCase` | `()` | `Result<List<Objeto>>` | **todos** los objetos de todos los usuarios, más nuevo primero |
| `GetObjetoUseCase` | `(id)` | `Result<Objeto>` | — |
| `PublicarObjetoUseCase` | `(nombre, descripcion, categoria, conservacion, peso: Double?, modalidad)` | `Result<Objeto>` | campos vacíos, peso > 0, opciones válidas |
| `EliminarObjetoUseCase` | `(id)` | `Result<Unit>` | — |
| `SolicitarObjetoUseCase` | `(id)` | `Result<Objeto>` | — |
| `AceptarSolicitudUseCase` | `(id)` | `Result<Objeto>` | — |
| `RechazarSolicitudUseCase` | `(id)` | `Result<Objeto>` | — |
| `ConfirmarEntregaUseCase` | `(id, codigo)` | `Result<Objeto>` | código de 6 dígitos |
| `CalcularImpactoUseCase` | `(objetos, usuarioId)` | `ResumenImpacto` (no es `suspend`, no usa red) | — |

`GetObjetosUseCase` trae todo y **cada pantalla filtra** lo que necesita:

| Pantalla | Filtro sobre la lista (`yo` = id del usuario con sesión) |
|---|---|
| Explorar | `estado == DISPONIBLE && propietarioId != yo` + búsqueda + modalidad |
| Mis objetos | `propietarioId == yo` |
| Solicitudes · Recibidas | `propietarioId == yo && estado in (SOLICITADO, ACEPTADO)` |
| Solicitudes · Enviadas | `solicitanteId == yo && estado in (SOLICITADO, ACEPTADO)` |
| Escanear · entregas pendientes | `solicitanteId == yo && estado == ACEPTADO` |
| Inicio / Impacto / Perfil | `CalcularImpactoUseCase(lista, yo)` |

Para saber quién es "yo", el ViewModel inyecta `SesionRepository` y lee `sesion.usuario.value`.

Utilidades puras (paquete `util`): `Validacion.correoValido(...)`, `Validacion.claveValida(...)`, `Validacion.pesoDesdeTexto("0,8") → 0.8`, `CodigoQr.crear(id, codigo)`, `CodigoQr.leer(texto)`, `Fechas.haceCuanto(ms)`, `Fechas.formatear(ms)`.

---

## 7. Plantilla de una pantalla (cópiala, es el estilo del profesor)

Cada pantalla son **3 archivos** en su carpeta: `XxxUiState.kt`, `XxxViewModel.kt`, `XxxScreen.kt`.

```kotlin
// presentation/screens/explorar/ExplorarUiState.kt
data class ExplorarUiState(
    val objetos: List<Objeto> = emptyList(),
    val busqueda: String = "",
    val filtroModalidad: String = "Todos",
    val estaCargando: Boolean = false
)
```

```kotlin
// presentation/screens/explorar/ExplorarViewModel.kt
@HiltViewModel
class ExplorarViewModel @Inject constructor(
    private val getObjetosUseCase: GetObjetosUseCase,
    private val sesion: SesionRepository,
    private val eventBus: UiEventBus
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExplorarUiState())
    val uiState = _uiState.asStateFlow()

    fun cargar() {
        viewModelScope.launch {
            _uiState.update { it.copy(estaCargando = true) }
            getObjetosUseCase()
                .onSuccess { lista ->
                    val yo = sesion.usuario.value?.id.orEmpty()
                    val visibles = lista.filter { it.estado == Opciones.DISPONIBLE && it.propietarioId != yo }
                    _uiState.update { it.copy(objetos = visibles, estaCargando = false) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(estaCargando = false) }
                    eventBus.lanzarEvento(
                        UiEvent.MostrarSnackbar(error.message ?: "No se pudo cargar", SnackbarTipo.ERROR)
                    )
                }
        }
    }

    fun onBusquedaChange(valor: String) = _uiState.update { it.copy(busqueda = valor) }
    fun onFiltroModalidad(valor: String) = _uiState.update { it.copy(filtroModalidad = valor) }
}
```

```kotlin
// presentation/screens/explorar/ExplorarScreen.kt
@Composable
fun ExplorarScreen(
    onVerDetalle: (String) -> Unit,
    viewModel: ExplorarViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) { viewModel.cargar() }      // recarga cada vez que se entra

    if (uiState.estaCargando) {
        CargandoScreen()
        return
    }
    // ... la misma interfaz de la versión 1, leyendo de uiState y llamando a viewModel.onXxx(...)
}
```

Convenciones obligatorias:

- El estado se llama `_uiState` / `uiState`. El booleano de carga se llama `estaCargando` (como el profesor).
- La pantalla llama **funciones** del ViewModel (`viewModel.publicar()`, `viewModel.onNombreChange(it)`). Ya no hay `sealed class ObjetoEvent`.
- El id de una ruta se lee en el ViewModel con `savedStateHandle["id"]` (como `DetalleViewModel` del profesor), no se pasa por parámetro a la pantalla.
- Import de `hiltViewModel`: `androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel` (el mismo que usa el profesor).
- Mensajes al usuario: **Snackbar** con `eventBus.lanzarEvento(UiEvent.MostrarSnackbar(mensaje, SnackbarTipo.SUCCESS | ERROR | INFO | WARNING))`. Errores de un campo de formulario pueden ir además en un `error: String` del UiState, debajo del campo.
- Navegar después de una acción (por ejemplo, al publicar): el UiState tiene un booleano (`publicado`), la pantalla lo observa con `LaunchedEffect(uiState.publicado)` y llama al callback. El ViewModel nunca navega.
- Mientras una acción está en curso (`estaCargando` o `enviando`), el botón va con `habilitado = false` para evitar doble toque.
- Listas vacías: siempre un texto ("Aún no hay objetos…"), nunca una pantalla en blanco.

---

## 8. Lo que ya existe y debes reutilizar

### Componentes (`presentation/common`) — firmas reales

```kotlin
BotonPrincipal(texto: String, onClick: () -> Unit, modifier: Modifier = Modifier, habilitado: Boolean = true)
BotonSecundario(texto: String, onClick: () -> Unit, modifier: Modifier = Modifier)
CampoTexto(valor: String, onCambio: (String) -> Unit, etiqueta: String, modifier: Modifier = Modifier,
           esPassword: Boolean = false, teclado: KeyboardType = KeyboardType.Text,
           readOnly: Boolean = false, lineas: Int = 1, icono: ImageVector? = null)
CampoSelector(valor: String, opciones: List<String>, etiqueta: String, onSeleccion: (String) -> Unit, modifier: Modifier = Modifier)
ChipOpcion(texto: String, seleccionado: Boolean, onClick: () -> Unit)
Etiqueta(texto: String, destacada: Boolean = false)
ObjetoItem(objeto: Objeto, modifier: Modifier = Modifier, mostrarFecha: Boolean = false, onClick: () -> Unit = {})
colorCategoria(categoria: String): Color
LogoReeloop(tamaño: Dp = 56.dp)
CargandoScreen()                       // nuevo en el Hito 0, igual al del profesor
```

Colores (`ui/theme/Color.kt`): `VerdePrincipal`, `VerdeOscuro`, `VerdeClaro`, `Fondo`, `TextoOscuro`, `TextoGris`, `Borde`, `Rojo`, `Crema`, `AzulClaro`, `Durazno`, `GrisClaro`. No se crean colores sueltos con `Color(0xFF...)` dentro de las pantallas.

**Ya no se usa** `Encabezado(...)` ni `EncabezadoAtras(...)` dentro de las pantallas: el título, la flecha atrás y el menú ⋮ los pone la barra superior global (`AppTopBar`, módulo A). Tu pantalla empieza directamente con su contenido.

### Firmas de las pantallas (congeladas: `AppNavigation` ya las llama así)

```kotlin
// Módulo B
LoginScreen(onSesionIniciada: () -> Unit, onCrearCuenta: () -> Unit, viewModel: LoginViewModel = hiltViewModel())
RegistroScreen(onRegistrado: () -> Unit, onYaTengoCuenta: () -> Unit, viewModel: RegistroViewModel = hiltViewModel())
InicioScreen(onPublicar: () -> Unit, onExplorar: () -> Unit, onMisObjetos: () -> Unit,
             onSolicitudes: () -> Unit, onEscanear: () -> Unit, viewModel: InicioViewModel = hiltViewModel())
ImpactoScreen(viewModel: ImpactoViewModel = hiltViewModel())
PerfilScreen(onMisObjetos: () -> Unit, onSolicitudes: () -> Unit, onSesionCerrada: () -> Unit,
             viewModel: PerfilViewModel = hiltViewModel())

// Módulo C
ExplorarScreen(onVerDetalle: (String) -> Unit, viewModel: ExplorarViewModel = hiltViewModel())
DetalleScreen(onPasaporte: (String) -> Unit, onEliminado: () -> Unit, viewModel: DetalleViewModel = hiltViewModel())
PublicarScreen(onPublicado: () -> Unit, viewModel: PublicarViewModel = hiltViewModel())
MisObjetosScreen(onPublicar: () -> Unit, onVerDetalle: (String) -> Unit, viewModel: MisObjetosViewModel = hiltViewModel())

// Módulo D
SolicitudesScreen(onVerQr: (String) -> Unit, onConfirmarEntrega: (String) -> Unit,
                  viewModel: SolicitudesViewModel = hiltViewModel())
CodigoQrScreen(viewModel: CodigoQrViewModel = hiltViewModel())
EscanearQrScreen(onEntregado: (String) -> Unit, viewModel: EscanearQrViewModel = hiltViewModel())
PasaporteScreen(viewModel: PasaporteViewModel = hiltViewModel())
```

Si necesitas otro callback de navegación, **no cambies la firma por tu cuenta**: pídeselo a Daniel.

### Rutas

`login`, `registro`, `inicio`, `explorar`, `publicar`, `impacto`, `perfil`, `mis_objetos`, `solicitudes`, `detalle/{id}`, `codigo_qr/{id}`, `pasaporte/{id}`, `escanear_qr?id={id}` (el id es opcional, como `form?id={id}` del profesor).

---

## 9. Quién toca qué

| Módulo | Responsable (sugerido) | Carpetas propias |
|---|---|---|
| **A · Base, nube e integración** | Daniel | `di/`, `data/local/`, `presentation/common/`, `presentation/event/`, `presentation/navigation/`, `ui/theme/`, `MainActivity`, `ReeloopApplication`, Gradle, Manifest, AWS |
| **B · Cuenta, Inicio e Impacto** | Cesar | `presentation/screens/{login,registro,inicio,impacto,perfil}/` |
| **C · Catálogo y publicación** | Erik | `presentation/screens/{explorar,detalle,publicar,misobjetos}/` |
| **D · Solicitudes, QR y Pasaporte** | Joel | `presentation/screens/{solicitudes,codigoqr,escanearqr,pasaporte}/` |

Los nombres son una sugerencia; pueden intercambiar módulos, pero **un módulo = una persona**.

Archivos del contrato (`domain/`, `data/remote/`, `data/repository/`, `data/fake/`, `util/`): **nadie los edita**. Si encuentras un error o te falta algo, escríbele a Daniel con el caso concreto.

¿Necesitas un componente nuevo? Créalo como `private` dentro de tu carpeta. Si luego sirve a otro módulo, Daniel lo sube a `common/`.

### Git

- Repositorio privado en GitHub. `main` siempre compila; solo Daniel hace merge a `main`.
- Ramas: `modulo-a-nube`, `modulo-b-cuenta`, `modulo-c-catalogo`, `modulo-d-entregas`.
- Cada mañana: `git pull origin main` y mezclar en tu rama antes de seguir.
- Un Pull Request por pantalla terminada (no uno gigante al final). Antes de pedirlo: la app compila, corre y pasaste la lista de pruebas de tu módulo.
- Mensajes de commit en español y concretos: `Explorar: búsqueda y filtro por modalidad`.

---

## 10. Trabajar sin la nube: modo Fake

Mientras Daniel construye la API en AWS, la app usa repositorios falsos en memoria con las **mismas reglas y mensajes** que tendrá el servidor. Para ti es transparente: tu ViewModel llama a los mismos UseCase.

Cuentas de prueba (clave de las dos: `123456`):

| Usuario | Correo | Tiene |
|---|---|---|
| Ana Torres | `ana@correo.com` | 3 objetos publicados (uno disponible, uno con solicitud pendiente, uno aceptado con código `482913`), 1 entrega por confirmar (Audífonos, código `731550`), 1 ciclo completado |
| Carlos Mendoza | `carlos@correo.com` | es el dueño de "Novela juvenil", "Audífonos de diadema" y "Calculadora científica" |

Para probar un flujo de dos personas: entra con una cuenta, haz la acción, cierra sesión y entra con la otra. En modo Fake los datos se reinician al cerrar la app.

---

## 11. Resumen de la API (detalle completo en `07_CONTRATO_API_AWS.md`)

Todas las respuestas tienen la envoltura del profesor más `message`:

```json
{ "success": true,  "data": { ... }, "message": null, "timestamp": "2026-11-20T15:04:05Z" }
{ "success": false, "data": null,    "message": "El objeto ya no está disponible", "timestamp": "..." }
```

| Método | Ruta | Para qué |
|---|---|---|
| POST | `/auth/registro` | crear cuenta |
| POST | `/auth/login` | iniciar sesión |
| GET | `/objetos?usuarioId=` | lista completa (`data.items`) |
| GET | `/objetos/{id}?usuarioId=` | un objeto |
| POST | `/objetos` | publicar |
| DELETE | `/objetos/{id}?usuarioId=` | eliminar |
| POST | `/objetos/{id}/solicitar` · `/aceptar` · `/rechazar` · `/entregar` | cambiar de estado |

---

## 12. "Terminado" significa

- [ ] Compila sin errores ni imports sin usar, y corre en el emulador.
- [ ] La pantalla tiene sus 3 archivos y sigue la plantilla de la sección 7.
- [ ] Muestra `CargandoScreen()` mientras carga, un texto cuando la lista está vacía y un Snackbar cuando algo falla.
- [ ] No hay datos escritos a mano ("Ana Torres", "12 objetos"): todo sale del UiState.
- [ ] Ningún archivo pasa de ~200 líneas.
- [ ] Pasaste la lista de pruebas de tu módulo con las dos cuentas.
- [ ] Puedes responder las preguntas de defensa de tu módulo sin leer.

---

## 13. Texto para pegar al inicio en tu IA

```
Voy a trabajar en una app Android llamada ReLoop (Kotlin, Jetpack Compose, MVVM con Hilt,
Retrofit y casos de uso). Somos 4 personas y cada una usa una IA distinta, así que debes
respetar al pie de la letra el contrato que te pego a continuación.

Reglas para ti:
1. No agregues librerías ni cambies versiones.
2. No modifiques modelos, repositorios, casos de uso ni firmas de pantallas del contrato.
3. Crea o edita archivos solo dentro de las carpetas de mi módulo.
4. Sigue la plantilla UiState + ViewModel + Screen del contrato y el estilo del profesor.
5. Nombres y textos en español. Archivos de máximo 200 líneas.
6. Entrégame los archivos completos, uno por uno, con su ruta, y después explícame
   en simple qué hace cada parte, porque tengo que sustentarlo.
7. Si algo del contrato no alcanza para lo que te pido, no lo inventes: dímelo.

[pega aquí 02_CONTEXTO_COMUN.md]
[pega aquí el archivo de tu módulo]
[pega aquí el código de la versión 1 de la pantalla que vas a migrar, desde referencia_v1/]
```
