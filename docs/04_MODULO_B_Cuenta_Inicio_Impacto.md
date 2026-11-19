# Módulo B · Cuenta, Inicio e Impacto

**Responsable (sugerido):** Cesar · **Rama:** `modulo-b-cuenta`

> Pega primero `02_CONTEXTO_COMUN.md` en tu IA y luego este archivo.

Tu módulo responde a "¿quién soy y cómo voy?": crear cuenta, entrar, ver el resumen al abrir la app, ver el impacto y cerrar sesión. Son **5 pantallas**.

---

## 1. Qué recibes y qué entregas

**Recibes (Hito 0, lo deja Daniel):** el proyecto compilando con Hilt, la navegación lista, tus 5 pantallas como "pendientes" y los casos de uso ya escritos.

**Tu carpeta:** `presentation/screens/login/`, `registro/`, `inicio/`, `impacto/`, `perfil/`. Cada una con `XxxUiState.kt`, `XxxViewModel.kt`, `XxxScreen.kt`.

**No tocas:** nada fuera de esas 5 carpetas.

**Material de partida:** `referencia_v1/LoginScreen.kt`, `RegistroScreen.kt`, `InicioScreen.kt`, `ImpactoScreen.kt`, `PerfilScreen.kt` (la interfaz ya hecha, que hoy tiene datos escritos a mano) y las imágenes de `design/`.

**Casos de uso que usas:** `IniciarSesionUseCase`, `RegistrarUsuarioUseCase`, `CerrarSesionUseCase`, `GetObjetosUseCase`, `CalcularImpactoUseCase`. Además inyectas `SesionRepository` (para saber el usuario) y `UiEventBus` (Snackbar).

---

## 2. Tareas en orden

Haz un Pull Request por pantalla. Orden pensado para que lo primero que funcione sea entrar a la app.

### B1 · Login

```kotlin
data class LoginUiState(
    val correo: String = "",
    val clave: String = "",
    val error: String = "",
    val estaCargando: Boolean = false,
    val sesionIniciada: Boolean = false
)
```

ViewModel: `onCorreoChange`, `onClaveChange` (limpian `error`), `entrar()`.

- `entrar()` pone `estaCargando = true`, llama `iniciarSesionUseCase(correo, clave)`.
  - Éxito → `sesionIniciada = true`.
  - Falla → `error = mensaje` (se muestra en rojo bajo los campos) y `estaCargando = false`.
- Pantalla: `LaunchedEffect(uiState.sesionIniciada) { if (uiState.sesionIniciada) onSesionIniciada() }`.
- Botón "Entrar" con `habilitado = !uiState.estaCargando`; mientras carga, el texto cambia a "Entrando…".

**Listo cuando:** correo mal escrito → "Ingresa un correo válido"; clave equivocada → "Correo o contraseña incorrectos"; `ana@correo.com` / `123456` → entra a Inicio; el botón Atrás desde Inicio no vuelve al Login.

### B2 · Registro

```kotlin
data class RegistroUiState(
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val confirmar: String = "",
    val aceptaTerminos: Boolean = false,
    val error: String = "",
    val estaCargando: Boolean = false,
    val registrado: Boolean = false
)
```

- En el ViewModel, antes de llamar al caso de uso: `clave != confirmar` → "Las contraseñas no coinciden"; `!aceptaTerminos` → "Debes aceptar los términos". Lo demás (nombre, formato de correo, largo de clave, correo repetido) lo valida `RegistrarUsuarioUseCase` y el servidor.
- Éxito → `registrado = true` → la pantalla llama `onRegistrado()`. El caso de uso ya deja la sesión iniciada.
- La versión 1 solo tenía nombre, correo y clave: agrega "Confirmar" y la casilla de términos como en el diseño de Stitch.

**Listo cuando:** cada validación muestra su mensaje; registrar `ana@correo.com` → "Ya existe una cuenta con ese correo"; una cuenta nueva entra a Inicio con su nombre.

### B3 · Inicio

```kotlin
data class InicioUiState(
    val nombre: String = "",                       // primer nombre del usuario
    val resumen: ResumenImpacto = ResumenImpacto(),
    val entregasPorConfirmar: Int = 0,             // solicitanteId == yo && estado == ACEPTADO
    val proximoPaso: String = "",
    val estaCargando: Boolean = false
)
```

- `cargar()`: `getObjetosUseCase()` → `calcularImpactoUseCase(lista, yo)`.
- `proximoPaso` (una sola frase, en este orden de prioridad):
  1. hay solicitudes pendientes → "Tienes N solicitudes por responder."
  2. hay entregas por confirmar → "Coordina la entrega de «nombre del objeto»."
  3. no has publicado nada → "Publica tu primer objeto."
  4. si no → "Explora lo que publicó tu comunidad."
- Interfaz de la versión 1 con datos reales: "Hola, {nombre}", tarjeta EcoImpacto (`resumen.reutilizados`), botones "Tengo un objeto" / "Necesito uno", accesos rápidos (Mis objetos: `resumen.publicados` publicaciones · Solicitudes: `resumen.solicitudesPendientes` pendientes · Escanear entrega: `entregasPorConfirmar` por confirmar) y la tarjeta "Próximo paso".

**Listo cuando:** con Ana se ve "Hola, Ana", 1 objeto reutilizado, 3 publicaciones, 1 solicitud pendiente, 1 entrega por confirmar; con Carlos los números son otros; ya no aparece ningún "Ana" escrito a mano.

### B4 · EcoImpacto

```kotlin
data class ImpactoUiState(
    val resumen: ResumenImpacto = ResumenImpacto(),
    val estaCargando: Boolean = false
)
```

- Misma carga que Inicio. Cuatro tarjetas (Reutilizados, Donaciones, Préstamos, Intercambios), la tarjeta oscura de "Ciclos completados" y una línea con `resumen.kgEvitados` ("X kg que no terminaron en la basura", con un decimal).
- Si `reutilizados == 0`: mensaje de ánimo en vez de ceros sueltos ("Aún no completas un ciclo. Publica o solicita un objeto para empezar.").

**Listo cuando:** los números coinciden con los de Inicio y Perfil para la misma cuenta, y suben después de confirmar una entrega.

### B5 · Perfil

```kotlin
data class PerfilUiState(
    val usuario: Usuario? = null,
    val resumen: ResumenImpacto = ResumenImpacto(),
    val estaCargando: Boolean = false,
    val sesionCerrada: Boolean = false
)
```

- Muestra inicial en el avatar, nombre y correo reales (de `SesionRepository`), las dos tarjetas de impacto, los accesos a Mis objetos y Solicitudes, y "Cerrar sesión".
- `cerrarSesion()` llama `cerrarSesionUseCase()` y pone `sesionCerrada = true` → la pantalla llama `onSesionCerrada()`.
- Antes de cerrar, pide confirmación con un `AlertDialog` ("¿Cerrar sesión?").

**Listo cuando:** muestra los datos de la cuenta con la que entraste; cerrar sesión lleva a Login y al volver a abrir la app pide entrar otra vez.

---

## 3. Niveles (por si falta tiempo)

| Nivel | Qué incluye |
|---|---|
| **1 · obligatorio** | B1 a B5 como están descritas |
| **2 · deseable** | Mostrar/ocultar contraseña con un ícono de ojo; `ImeAction` para pasar de un campo al siguiente con el teclado; pruebas unitarias (abajo) |
| **3 · extra** | Barra de progreso hacia una meta personal de reutilización en Inicio |

### Pruebas unitarias (nivel 2)

En `app/src/test/java/com/reeloop/app/`: `ValidacionTest` (correos válidos e inválidos, `pesoDesdeTexto("0,8")`) y `CalcularImpactoUseCaseTest` (una lista de 4 o 5 objetos armada a mano y los números esperados). Son Kotlin puro: corren con JUnit sin emulador. Sirven como evidencia de calidad en el informe.

---

## 4. Lista de pruebas antes de cada Pull Request

- [ ] Login: los 3 casos de B1.
- [ ] Registro: clave corta, claves distintas, sin aceptar términos, correo repetido, registro correcto.
- [ ] Entrar con Ana y con Carlos: Inicio, Impacto y Perfil muestran datos distintos y coherentes entre sí.
- [ ] Se ve "Cargando..." un instante al entrar a Inicio, Impacto y Perfil.
- [ ] Girar el celular con el formulario de Registro lleno: no se borra.
- [ ] Cerrar sesión y volver a entrar con otra cuenta: no queda ningún dato de la anterior.

---

## 5. Preguntas de defensa

1. ¿Dónde se valida el correo: en la pantalla, en el ViewModel o en el caso de uso? ¿Por qué ahí?
2. ¿Por qué el `LoginViewModel` no navega y usa el booleano `sesionIniciada`?
3. ¿Cómo sabe la app quién tiene la sesión abierta después de cerrarla y volver a abrirla?
4. ¿Por qué Inicio, Impacto y Perfil muestran los mismos números sin copiar el cálculo tres veces?
5. ¿Qué es `StateFlow` y qué hace `collectAsState()`?
6. ¿Qué pasa en pantalla, paso a paso, si el servidor responde "Correo o contraseña incorrectos"?
7. ¿Se guarda la contraseña en el celular? (No: solo id, nombre y correo. La contraseña viaja al servidor, que guarda un hash.)

---

## 6. Encargo para tu IA

```
Con el contexto común que te pegué, soy responsable del módulo B de ReLoop.
Empecemos por la tarea B1 (Login). Te pego mi archivo de módulo y el código de la versión 1
de la pantalla (referencia_v1/LoginScreen.kt).

Dame los 3 archivos completos de presentation/screens/login/ (UiState, ViewModel, Screen):
- conserva el diseño de la versión 1;
- quita los datos escritos a mano y lee todo del UiState;
- la firma de LoginScreen debe ser exactamente la del contrato;
- usa solo componentes de presentation/common y colores de ui/theme.
Después explícame, en simple, el recorrido completo desde que toco "Entrar" hasta que
aparece Inicio, y hazme 3 preguntas para comprobar que lo entendí.
Cuando te confirme que compila y funciona, seguimos con B2.
```
