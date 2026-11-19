# Módulo C · Catálogo y publicación

**Responsable (sugerido):** Erik · **Rama:** `modulo-c-catalogo`

> Pega primero `02_CONTEXTO_COMUN.md` en tu IA y luego este archivo.

Tu módulo es el corazón visible de la app: publicar un objeto, encontrarlo, ver su detalle y solicitarlo. Son **4 pantallas** y aquí empieza el ciclo de vida del objeto (`Disponible → Solicitado`).

---

## 1. Qué recibes y qué entregas

**Recibes (Hito 0):** el proyecto compilando con Hilt, la navegación lista, tus 4 pantallas como "pendientes" y los casos de uso ya escritos.

**Tu carpeta:** `presentation/screens/explorar/`, `detalle/`, `publicar/`, `misobjetos/`. Cada una con `XxxUiState.kt`, `XxxViewModel.kt`, `XxxScreen.kt`.

**No tocas:** nada fuera de esas 4 carpetas.

**Material de partida:** `referencia_v1/ExplorarScreen.kt`, `DetalleScreen.kt`, `PublicarScreen.kt`, `MisObjetosScreen.kt` y las imágenes de `design/`.

**Casos de uso que usas:** `GetObjetosUseCase`, `GetObjetoUseCase`, `PublicarObjetoUseCase`, `SolicitarObjetoUseCase`, `EliminarObjetoUseCase`. Además inyectas `SesionRepository` y `UiEventBus`.

**Cambio clave respecto a la versión 1:** el `id` del objeto es `String`. Y ya no existe el estado compartido `ObjetoState`: cada pantalla carga lo suyo.

---

## 2. Tareas en orden

Un Pull Request por pantalla.

### C1 · Explorar

Es exactamente la plantilla de la sección 7 del contexto común: empieza por ahí.

```kotlin
data class ExplorarUiState(
    val objetos: List<Objeto> = emptyList(),      // ya filtrados: disponibles y que no son míos
    val busqueda: String = "",
    val filtroModalidad: String = "Todos",
    val estaCargando: Boolean = false
)
```

- `cargar()` filtra `estado == DISPONIBLE && propietarioId != yo`.
- La búsqueda y el chip de modalidad se aplican **en la pantalla** sobre `uiState.objetos` (son instantáneos, no vuelven a llamar al servidor):

```kotlin
val resultados = uiState.objetos.filter {
    (uiState.filtroModalidad == "Todos" || it.modalidad == uiState.filtroModalidad) &&
        it.nombre.contains(uiState.busqueda, ignoreCase = true)
}
```

- Tocar una tarjeta → `onVerDetalle(objeto.id)`.

**Listo cuando:** con Ana se ve "Novela juvenil" (de Carlos) y **no** sus propios objetos; buscar "nov" la encuentra; el chip "Donación" la oculta; el contador "N objetos" coincide; sin resultados aparece "No hay objetos para mostrar."

### C2 · Detalle (el más importante de tu módulo)

```kotlin
data class DetalleUiState(
    val objeto: Objeto? = null,
    val esMio: Boolean = false,          // propietarioId == yo
    val estaCargando: Boolean = false,
    val enviando: Boolean = false,       // hay una acción en curso (solicitar / eliminar)
    val eliminado: Boolean = false
)
```

- El id llega por la ruta: `private val objetoId: String? = savedStateHandle["id"]` (igual que `DetalleViewModel` del profesor).
- `cargar()` usa `getObjetoUseCase(id)`.
- Qué botones se ven:

| Situación | Botones |
|---|---|
| No es mío y está `Disponible` | "Ver pasaporte" + **"Solicitar objeto"** |
| No es mío y ya no está disponible | "Ver pasaporte" + "Ya solicitado" (deshabilitado) |
| Es mío y está `Disponible` | "Ver pasaporte" + **"Eliminar publicación"** |
| Es mío y tiene solicitud o ya se entregó | solo "Ver pasaporte" |

- `solicitar()`: `solicitarObjetoUseCase(id)`. Éxito → actualiza `objeto` con el que devuelve el servidor + Snackbar SUCCESS "Solicitud enviada a {propietarioNombre}". Falla → Snackbar ERROR con el mensaje.
- `eliminar()`: primero un `AlertDialog` de confirmación en la pantalla. Éxito → `eliminado = true` → la pantalla llama `onEliminado()`.
- "Ver pasaporte" → `onPasaporte(objeto.id)`.
- Muestra además "Publicado por {propietarioNombre}".

**Listo cuando:** Ana abre "Novela juvenil" y la solicita → el botón pasa a "Ya solicitado" y la etiqueta a "Solicitado"; al volver, ya no está en Explorar; Ana abre su "Libro de cálculo" desde Mis objetos y ve "Eliminar publicación", no "Solicitar"; abrir un objeto que no existe muestra un mensaje y no se cierra la app.

### C3 · Publicar

```kotlin
data class PublicarUiState(
    val nombre: String = "",
    val descripcion: String = "",
    val categoria: String = Opciones.categorias.first(),
    val conservacion: String = Opciones.conservaciones[1],
    val peso: String = "",                 // texto tal como lo escribe el usuario
    val modalidad: String = Opciones.DONACION,
    val error: String = "",
    val estaCargando: Boolean = false,
    val publicado: Boolean = false
)
```

- Un `onXxxChange` por campo (los de texto limpian `error`).
- `publicar()`:

```kotlin
publicarObjetoUseCase(
    nombre = s.nombre, descripcion = s.descripcion, categoria = s.categoria,
    conservacion = s.conservacion, peso = Validacion.pesoDesdeTexto(s.peso), modalidad = s.modalidad
)
```

  - Éxito → Snackbar SUCCESS "¡Publicación creada!" y `publicado = true` → la pantalla llama `onPublicado()` (Daniel ya lo dirige a Mis objetos).
  - Falla → `error = mensaje` bajo el formulario.
- El propietario **no** se pide en el formulario: el caso de uso lo toma de la sesión.
- El recuadro "Añadir fotografía" se queda como elemento de diseño (las fotos están fuera del alcance).

**Listo cuando:** publicar vacío → "Escribe el nombre del objeto"; peso `abc` o `0` → "Ingresa un peso válido"; `0,8` y `0.8` funcionan; al publicar llegas a Mis objetos y el objeto aparece primero; el botón se deshabilita mientras envía (no se crean duplicados con doble toque).

### C4 · Mis objetos

```kotlin
data class MisObjetosUiState(
    val objetos: List<Objeto> = emptyList(),   // propietarioId == yo
    val estaCargando: Boolean = false
)
```

- Lista con `ObjetoItem(objeto, mostrarFecha = true)`; tocar → `onVerDetalle(objeto.id)`.
- Vacío → texto "Aún no has publicado objetos." + `BotonPrincipal("Publicar objeto", onPublicar)`.
- El aviso verde "¡Publicación creada!" de la versión 1 se reemplaza por el Snackbar de C3: borra `BannerExito`.

**Listo cuando:** Ana ve sus 3 objetos con su estado real; una cuenta recién registrada ve el estado vacío con el botón; después de eliminar un objeto en Detalle y volver, ya no aparece.

---

## 3. Niveles (por si falta tiempo)

| Nivel | Qué incluye |
|---|---|
| **1 · obligatorio** | C1 a C4; en C2, "Solicitar" y "Ver pasaporte" |
| **2 · deseable** | "Eliminar publicación" en C2; chips de estado en Mis objetos (Todos / Disponibles / En proceso / Entregados); botón "Reintentar" cuando la carga falla |
| **3 · extra** | Filtro por categoría en Explorar; deslizar hacia abajo para recargar |

---

## 4. Lista de pruebas antes de cada Pull Request

- [ ] Explorar con Ana y con Carlos: cada uno ve lo del otro, nunca lo suyo.
- [ ] Búsqueda + chip combinados.
- [ ] Solicitar y comprobar que desaparece de Explorar.
- [ ] Entrar con la otra cuenta y comprobar en Solicitudes (módulo D, cuando esté) que llegó la solicitud.
- [ ] Publicar con cada modalidad y categoría; revisar que Detalle muestra todo igual.
- [ ] Todas las validaciones de Publicar.
- [ ] Se ve "Cargando..." al entrar a cada pantalla.
- [ ] Girar el celular con el formulario lleno: no se borra.

---

## 5. Preguntas de defensa

1. ¿Por qué Explorar filtra en el ViewModel "disponibles que no son míos" pero la búsqueda se hace en la pantalla?
2. ¿Cómo llega el `id` del objeto desde la tarjeta de Explorar hasta `DetalleViewModel`? (Ruta → `SavedStateHandle`.)
3. ¿Quién decide que no puedes solicitar tu propio objeto: la app o el servidor? (Los dos: la app esconde el botón; el servidor lo rechaza igual.)
4. ¿Por qué el peso es `String` en el UiState y `Double` en el modelo?
5. ¿Qué hace `PublicarObjetoUseCase` que no hace el ViewModel?
6. ¿Qué pasa si dos personas solicitan el mismo objeto casi a la vez?
7. ¿Para qué sirve `LaunchedEffect(Unit) { viewModel.cargar() }` y cuándo se ejecuta?

---

## 6. Encargo para tu IA

```
Con el contexto común que te pegué, soy responsable del módulo C de ReLoop.
Empecemos por la tarea C1 (Explorar). Te pego mi archivo de módulo y el código de la versión 1
de la pantalla (referencia_v1/ExplorarScreen.kt).

Dame los 3 archivos completos de presentation/screens/explorar/ (UiState, ViewModel, Screen):
- conserva el diseño de la versión 1, pero sin Encabezado (ahora hay barra superior global);
- el id del objeto es String;
- la firma de ExplorarScreen debe ser exactamente la del contrato;
- usa solo componentes de presentation/common y colores de ui/theme.
Después explícame, en simple, qué pasa desde que entro a la pantalla hasta que veo la lista,
y hazme 3 preguntas para comprobar que lo entendí.
Cuando te confirme que compila y funciona, seguimos con C2.
```
