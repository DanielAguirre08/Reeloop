# Módulo A · Base, nube e integración

**Responsable:** Daniel · **Rama:** `modulo-a-nube` (el Hito 0 va directo a `main`)

> Pega primero `02_CONTEXTO_COMUN.md` en tu IA y luego este archivo. Para la parte de AWS, pega además `07_CONTRATO_API_AWS.md`.

Tu módulo es la columna del proyecto. Tiene tres trabajos, en este orden:

1. **Hito 0 (día 1, bloquea a todos):** dejar la app con la arquitectura del profesor y las 13 pantallas cableadas como "pendientes", para que B, C y D trabajen en paralelo sin pisarse.
2. **Nube (días 2 a 4):** construir la API en AWS Academy mientras los demás hacen pantallas sobre el modo Fake.
3. **Integración (día 5 en adelante):** pasar de Fake a nube, mezclar ramas y cerrar.

---

## 1. Carpetas que son tuyas

`ReeloopApplication.kt`, `MainActivity.kt`, `di/`, `data/local/`, `presentation/common/`, `presentation/event/`, `presentation/navigation/`, `ui/theme/`, Gradle (`libs.versions.toml`, `build.gradle.kts`), `AndroidManifest.xml`, y todo AWS.

Además eres el único que copia y, si hace falta, corrige los archivos del contrato (`domain/`, `data/remote/`, `data/repository/`, `data/fake/`, `util/`).

---

## 2. Hito 0 · paso a paso

Trabaja sobre el proyecto actual (`Reeloop`, paquete `com.reeloop.app`). Haz un commit por paso.

### Paso 0.1 · Repositorio

- Crear el repositorio privado en GitHub, subir el proyecto e invitar a los 3 compañeros.
- Proteger `main` (solo se entra por Pull Request aprobado por ti).
- Copiar el kit en `docs/` del repositorio, y las 13 imágenes de Stitch en `design/`.

### Paso 0.2 · Guardar la versión 1 como referencia

Mover las 13 pantallas actuales (`presentation/screens/*.kt`), `ObjetoViewModel.kt`, `ObjetoState.kt` y `ObjetoEvent.kt` a una carpeta **`referencia_v1/` en la raíz del repositorio** (fuera de `app/src`, para que no se compile). Cada compañero copiará de ahí la interfaz de sus pantallas.

### Paso 0.3 · Gradle

En `gradle/libs.versions.toml`, **agregar** (versiones del profesor):

```toml
[versions]
hilt = "2.60.1"
hiltNavigationCompose = "1.4.0"
retrofit = "3.0.0"
converterGson = "3.0.0"
loggingInterceptor = "4.12.0"
zxing = "3.5.3"
codeScanner = "16.1.0"

[libraries]
hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
hilt-android-compiler = { group = "com.google.dagger", name = "hilt-android-compiler", version.ref = "hilt" }
androidx-hilt-navigation-compose = { group = "androidx.hilt", name = "hilt-navigation-compose", version.ref = "hiltNavigationCompose" }
retrofit = { group = "com.squareup.retrofit2", name = "retrofit", version.ref = "retrofit" }
converter-gson = { group = "com.squareup.retrofit2", name = "converter-gson", version.ref = "converterGson" }
logging-interceptor = { group = "com.squareup.okhttp3", name = "logging-interceptor", version.ref = "loggingInterceptor" }
zxing-core = { group = "com.google.zxing", name = "core", version.ref = "zxing" }
play-services-code-scanner = { group = "com.google.android.gms", name = "play-services-code-scanner", version.ref = "codeScanner" }

[plugins]
hilt = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }
```

**Quitar** las tres líneas de Room (`androidx-room-runtime`, `androidx-room-ktx`, `androidx-room-compiler`) y la versión `room`. KSP se queda: ahora lo usa Hilt.

- `build.gradle.kts` (raíz): `alias(libs.plugins.hilt) apply false`.
- `app/build.gradle.kts`: `alias(libs.plugins.hilt)` en `plugins`; en `dependencies` quitar las 3 de Room y agregar:

```kotlin
implementation(libs.hilt.android)
ksp(libs.hilt.android.compiler)
implementation(libs.androidx.hilt.navigation.compose)
implementation(libs.retrofit)
implementation(libs.converter.gson)
implementation(libs.logging.interceptor)
implementation(libs.zxing.core)
implementation(libs.play.services.code.scanner)
```

Si la sincronización falla por versiones, iguala `agp`, `composeBom` y `navigationCompose` a los del `libs.versions.toml` del profesor (`proyecto_final_ejemplo`): esa combinación ya está probada por él.

### Paso 0.4 · Manifest, Application y MainActivity

- `AndroidManifest.xml`: `<uses-permission android:name="android.permission.INTERNET"/>` y `android:name=".ReeloopApplication"` en `<application>`.
- `ReeloopApplication.kt`:

```kotlin
@HiltAndroidApp
class ReeloopApplication : Application()
```

- `MainActivity`: `@AndroidEntryPoint`, con `@Inject lateinit var eventBus: UiEventBus` y `@Inject lateinit var sesion: SesionRepository`, igual que el profesor inyecta su `eventBus`.

### Paso 0.5 · Copiar el contrato

Copiar la carpeta `contrato_kotlin/com/reeloop/app/` del kit dentro de `app/src/main/java/com/reeloop/app/` (reemplaza `Objeto.kt`, `Opciones.kt`, `Fechas.kt` y `ObjetoRepository.kt` actuales). Borrar lo que queda de la versión con Room: `data/AppData.kt`, `data/ObjetoRepositoryImpl.kt` (el nuevo vive en `data/repository/`) y `domain/repository/ObjetoDao.kt`.

Son 36 archivos: modelos, repositorios, 12 casos de uso, DTO, servicios Retrofit, repositorios reales y Fake, y utilidades. Ya están compilados y probados fuera de Android (45 comprobaciones del flujo completo). Lo que **no** está probado es su compilación dentro del proyecto con Hilt y Retrofit reales: si aparece un error de import, corrígelo y avisa.

### Paso 0.6 · Sesión, eventos e inyección

`data/local/SesionRepositoryImpl.kt`:

```kotlin
@Singleton
class SesionRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : SesionRepository {

    private val prefs = context.getSharedPreferences("reloop_sesion", Context.MODE_PRIVATE)
    private val _usuario = MutableStateFlow(leer())
    override val usuario: StateFlow<Usuario?> = _usuario.asStateFlow()

    override fun guardar(usuario: Usuario) {
        prefs.edit()
            .putString("id", usuario.id)
            .putString("nombre", usuario.nombre)
            .putString("correo", usuario.correo)
            .apply()
        _usuario.value = usuario
    }

    override fun cerrar() {
        prefs.edit().clear().apply()
        _usuario.value = null
    }

    private fun leer(): Usuario? {
        val id = prefs.getString("id", null) ?: return null
        return Usuario(id, prefs.getString("nombre", "").orEmpty(), prefs.getString("correo", "").orEmpty())
    }
}
```

`presentation/event/UiEvent.kt` y `UiEventBus.kt`, `presentation/common/SnackbarTipo.kt`, `SnackbarCustom.kt` y `CargandoScreen.kt`: **copiarlos del ejemplo del profesor** cambiando solo el `package`. En `SnackbarCustom` usa los colores del tema para éxito (`VerdeOscuro`) y error (`Rojo`).

`di/NetworkModule.kt` (como el del profesor, con una sola URL, así que no hacen falta los `@Qualifier`):

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    // Invoke URL de API Gateway. Debe terminar en "/".
    private const val BASE_URL = "https://PENDIENTE.execute-api.us-east-1.amazonaws.com/"

    @Provides @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()

    @Provides @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService = retrofit.create(AuthApiService::class.java)

    @Provides @Singleton
    fun provideObjetoApiService(retrofit: Retrofit): ObjetoApiService = retrofit.create(ObjetoApiService::class.java)
}
```

`di/RepositoryModule.kt` (aquí vive el interruptor Fake / nube):

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    // false = datos de prueba en memoria.  true = nube AWS.
    private const val USAR_NUBE = false

    @Provides @Singleton
    fun provideAuthRepository(remoto: AuthRepositoryImpl, fake: FakeAuthRepository): AuthRepository =
        if (USAR_NUBE) remoto else fake

    @Provides @Singleton
    fun provideObjetoRepository(remoto: ObjetoRepositoryImpl, fake: FakeObjetoRepository): ObjetoRepository =
        if (USAR_NUBE) remoto else fake

    @Provides @Singleton
    fun provideSesionRepository(impl: SesionRepositoryImpl): SesionRepository = impl
}
```

### Paso 0.7 · Navegación y armazón (estilo del profesor)

- `presentation/navigation/Rutas.kt`: las rutas de la sección 8 del contexto común, con `id` de tipo `String`:
  - `fun detalle(id: String) = "detalle/$id"`, `codigoQr(id)`, `pasaporte(id)`, y `fun escanearQr(id: String? = null) = if (id != null) "escanear_qr?id=$id" else "escanear_qr"`.
  - `fun getTitulo(ruta: String?): String` (como `RutasNav.getTitulo` del profesor): "Explorar", "Detalle del objeto", "Publicar objeto", "Mis objetos", "Solicitudes", "Código QR", "Escanear QR", "Pasaporte Circular", "EcoImpacto", "Perfil", "ReLoop" en Inicio.
  - `sinBarras = listOf(LOGIN, REGISTRO)`; `sinBarraInferior = listOf(CODIGO_QR, ESCANEAR_QR)`; `conFlechaAtras` = detalle, mis objetos, solicitudes, código QR, escanear, pasaporte; `pestanaDe(ruta)` se conserva.
- `presentation/common/AppTopBar.kt`: logo o flecha atrás, título, avatar con la inicial del usuario y **menú de opciones ⋮** (`DropdownMenu`):
  1. *Mi impacto* → navega a `impacto`.
  2. *Compartir ReLoop* → `Intent.ACTION_SEND` con un texto.
  3. *Acerca de* → `AlertDialog` con el nombre de la app, el grupo y los integrantes.
  4. *Cerrar sesión* → `sesion.cerrar()` y navegar a `login` limpiando la pila.
- `presentation/common/AppBottomBar.kt`: la `BarraInferior` actual, sin cambios de diseño.
- `presentation/common/AppScaffold.kt`: como el del profesor (`SnackbarHost` + `topBar` + `bottomBar`), ocultando las barras según `Rutas`.
- `presentation/navigation/AppNavigation.kt`: `NavHost` con las 13 rutas llamando a las **firmas congeladas** del contexto común. `startDestination` = `inicio` si `sesion.usuario.value != null`, si no `login`. Argumentos con `NavType.StringType`; el de `escanear_qr` es `nullable = true, defaultValue = null`.
- Navegaciones que decides tú: después de login o registro → `inicio` limpiando la pila; `onPublicado` → `mis_objetos`; `onEliminado` → `popBackStack()`; `onEntregado(id)` → `pasaporte/{id}` quitando el escáner de la pila; `onSesionCerrada` → `login` limpiando la pila.

### Paso 0.8 · Las 13 pantallas "pendientes"

Para que el proyecto compile desde el primer día, crea cada pantalla en su carpeta final con su **firma final** y un cuerpo provisional. Como el ViewModel todavía no existe, la firma provisional omite el parámetro `viewModel` (cada dueño lo agrega al final de la lista de parámetros, con valor por defecto, así `AppNavigation` no cambia):

```kotlin
// presentation/screens/explorar/ExplorarScreen.kt   (provisional, la reemplaza el módulo C)
@Composable
fun ExplorarScreen(onVerDetalle: (String) -> Unit) {
    PantallaPendiente("Explorar", "Módulo C")
}
```

`PantallaPendiente(titulo, modulo)` va en `common/` y muestra el título centrado y un texto "En construcción". En Login y Registro pendientes, deja además un botón que llame a `onSesionIniciada` / `onRegistrado` para poder entrar a probar la navegación.

### Criterio de salida del Hito 0

- [ ] Compila y corre. Arranca en "Login pendiente", el botón entra a Inicio.
- [ ] La barra inferior navega entre las 5 pestañas; la barra superior muestra el título correcto y la flecha atrás donde toca.
- [ ] El menú ⋮ funciona: Mi impacto, Compartir, Acerca de, Cerrar sesión.
- [ ] Un Snackbar de prueba aparece al lanzar un `UiEvent`.
- [ ] No queda ninguna referencia a Room ni a `ObjetoViewModel`.
- [ ] Etiqueta `hito-0` en Git y aviso al grupo: "ya pueden crear sus ramas".

---

## 3. Nube · AWS Academy (días 2 a 4)

Sigue `07_CONTRATO_API_AWS.md` de principio a fin. Orden: tablas → Lambda `reloop_auth` → Lambda `reloop_objetos` → probar cada Lambda con evento de prueba → API Gateway → batería de 15 casos con Postman o `curl`.

Entregables:

- Código de las dos Lambda en el repositorio (`backend/reloop_auth.py`, `backend/reloop_objetos.py`).
- Colección de Postman en `docs/postman/`.
- Capturas de las tablas con datos, las Lambda y las rutas de API Gateway (sirven para el informe).
- La Invoke URL anotada en el grupo.

No conectes la app hasta que la batería pase completa: depurar la Lambda desde el celular es mucho más lento que desde Postman.

---

## 4. Integración (día 5 en adelante)

1. Poner la URL real en `NetworkModule.BASE_URL` y `USAR_NUBE = true`.
2. Registrar desde la app dos cuentas reales y recorrer el flujo completo (guion de la demo en `01_PLAN_Y_RUTA.md`).
3. Mirar el Logcat con el filtro `okhttp`: el `HttpLoggingInterceptor` muestra cada petición y respuesta. Si un campo llega vacío, casi siempre es un nombre distinto entre el JSON de la Lambda y el DTO.
4. Mezclar los Pull Request de B, C y D conforme lleguen, uno por pantalla. Después de cada merge: compilar y abrir esa pantalla.
5. Congelar el código un día antes de la entrega: desde ahí solo correcciones.

---

## 5. Lista de pruebas de tu módulo

- [ ] Abrir la app sin sesión → Login. Con sesión guardada → Inicio directo.
- [ ] Cerrar sesión desde el menú ⋮ y desde Perfil → vuelve a Login y el botón Atrás no regresa a la app.
- [ ] Modo avión con `USAR_NUBE = true` → Snackbar "Sin conexión. Revisa tu internet…", la app no se cierra.
- [ ] Girar el celular en un formulario → no se pierde lo escrito.
- [ ] Con dos emuladores o dos celulares: lo que publica uno lo ve el otro al volver a entrar a Explorar.

---

## 6. Preguntas de defensa

1. ¿Qué hace Hilt y qué pasaría sin él? (Quién crea `Retrofit`, los repositorios y los ViewModel.)
2. ¿Por qué el ViewModel depende de `ObjetoRepository` (interfaz) y no de `ObjetoRepositoryImpl`? Muéstralo con el interruptor `USAR_NUBE`.
3. ¿Para qué sirve un DTO y por qué tiene `toDomain()`?
4. ¿Qué recorrido hace "Solicitar objeto" desde el toque hasta DynamoDB y de vuelta?
5. ¿Cómo viaja un mensaje de error desde la Lambda hasta el Snackbar?
6. ¿Por qué `Result<T>` en vez de lanzar excepciones?
7. ¿Qué limitaciones de seguridad tiene la API y cómo se resolverían? (Sección 9 del contrato de la API.)

---

## 7. Encargo para tu IA (Hito 0)

```
Con el contexto común que te pegué, ayúdame a migrar el proyecto ReLoop al Hito 0 del módulo A.
Te voy a pegar: mi libs.versions.toml, mi app/build.gradle.kts, mi AndroidManifest, mi
AppNavigation.kt, Rutas.kt y BarraInferior.kt actuales, y del ejemplo del profesor:
AppScaffold.kt, AppTopBar.kt, AppBottomBar.kt, UiEvent.kt, UiEventBus.kt, SnackbarCustom.kt.

Vamos paso por paso (0.3 a 0.8 de mi archivo de módulo). En cada paso:
- dame los archivos completos con su ruta;
- no avances al siguiente hasta que yo te confirme que compila;
- explícame qué hace cada anotación de Hilt que aparezca.
No escribas ViewModel ni lógica de las pantallas: solo las pantallas provisionales con
PantallaPendiente y las firmas congeladas del contrato.
```
