# Módulo D · Solicitudes, QR y Pasaporte

**Responsable (sugerido):** Joel · **Rama:** `modulo-d-entregas`

> Pega primero `02_CONTEXTO_COMUN.md` en tu IA y luego este archivo.

Tu módulo cierra el ciclo: responder solicitudes, confirmar la entrega con un código QR y mostrar la historia del objeto. Son **4 pantallas**. Es la parte que hace distinta a ReLoop, y la que más se luce en el video.

---

## 1. Qué recibes y qué entregas

**Recibes (Hito 0):** el proyecto compilando con Hilt, la navegación lista, tus 4 pantallas como "pendientes", los casos de uso escritos y las dos librerías de QR ya agregadas en Gradle.

**Tu carpeta:** `presentation/screens/solicitudes/`, `codigoqr/`, `escanearqr/`, `pasaporte/`. Cada una con `XxxUiState.kt`, `XxxViewModel.kt`, `XxxScreen.kt`.

**No tocas:** nada fuera de esas 4 carpetas.

**Material de partida:** `referencia_v1/SolicitudesScreen.kt`, `CodigoQrScreen.kt`, `EscanearQrScreen.kt`, `PasaporteScreen.kt` y las imágenes de `design/`.

**Casos de uso que usas:** `GetObjetosUseCase`, `GetObjetoUseCase`, `AceptarSolicitudUseCase`, `RechazarSolicitudUseCase`, `ConfirmarEntregaUseCase`. Utilidades: `CodigoQr.crear / leer`, `Fechas.formatear / haceCuanto`. Además inyectas `SesionRepository` y `UiEventBus`.

### Cómo funciona la entrega (apréndelo bien, te lo van a preguntar)

```
PROPIETARIO (Ana)                                  SOLICITANTE (Carlos)
Solicitudes · Recibidas → "Aceptar"
   el servidor genera el código (6 dígitos)
"Ver código QR" → muestra el QR y los 6 dígitos ─►  Solicitudes · Enviadas → "Confirmar entrega"
                                                     escanea el QR  (o escribe los 6 dígitos)
                                                     el servidor compara el código
                                              ◄─────  objeto = Entregado → se abre el Pasaporte
```

- El QR contiene el texto `reloop:<idObjeto>:<codigo>` (lo arma `CodigoQr.crear`).
- El código solo le llega al propietario: en la cuenta del solicitante `objeto.codigoEntrega` viene vacío. Por eso el solicitante tiene que escanear o que le dicten el código.
- En la versión 1 no había forma de llegar a "Entregado" desde la app. Tu módulo es el que lo hace posible.

---

## 2. Tareas en orden

Un Pull Request por pantalla. El orden deja lo más riesgoso (cámara) para el final y con plan B.

### D1 · Solicitudes

```kotlin
data class SolicitudesUiState(
    val recibidas: List<Objeto> = emptyList(),   // propietarioId == yo && estado in (SOLICITADO, ACEPTADO)
    val enviadas: List<Objeto> = emptyList(),    // solicitanteId == yo && estado in (SOLICITADO, ACEPTADO)
    val pestana: String = "Recibidas",           // "Recibidas" | "Enviadas"
    val estaCargando: Boolean = false,
    val idEnProceso: String = ""                 // objeto cuya acción está en curso (deshabilita sus botones)
)
```

- Dos chips arriba (`ChipOpcion`): **Recibidas** y **Enviadas**, con el contador de cada una.
- Tarjeta según el caso (la tarjeta de la versión 1 sirve de base):

| Pestaña | Estado | Texto | Botones |
|---|---|---|---|
| Recibidas | Solicitado | "Pide: **{solicitanteNombre}** · hace X" | Rechazar / Aceptar |
| Recibidas | Aceptado | "Acuerdo confirmado · QR generado" | Ver código QR → `onVerQr(id)` |
| Enviadas | Solicitado | "De: **{propietarioNombre}** · esperando respuesta" | ninguno |
| Enviadas | Aceptado | "¡Aceptada! Coordina la entrega" | Confirmar entrega → `onConfirmarEntrega(id)` |

- `aceptar(id)` / `rechazar(id)`: llaman al caso de uso y, si sale bien, **vuelven a cargar la lista** y lanzan un Snackbar ("Solicitud aceptada" / "Solicitud rechazada"). Si falla, Snackbar ERROR con el mensaje.
- Ya no existen los solicitantes simulados de la versión 1: los nombres son de usuarios reales.

**Listo cuando:** Ana ve en Recibidas la Mochila (pendiente) y la Regla T (aceptada), y en Enviadas los Audífonos (aceptada); Aceptar la Mochila la pasa a "Aceptada" con su botón de QR; Rechazar la saca de la lista y vuelve a aparecer en Explorar para otros usuarios.

### D2 · Pasaporte Circular

```kotlin
data class PasaporteUiState(
    val objeto: Objeto? = null,
    val estaCargando: Boolean = false
)
```

- Id por `savedStateHandle["id"]`, carga con `getObjetoUseCase(id)`.
- Línea de tiempo con 3 etapas: **PUBLICADO** (`fechaPublicado`), **SOLICITADO** (`fechaSolicitado`), **ENTREGADO** (`fechaEntregado`). Una etapa está completa si su fecha es mayor que 0: punto verde y `Fechas.formatear(fecha)`. Si no: punto gris y "Pendiente".
- Textos reales: "Publicado por {propietarioNombre}", "Solicitado por {solicitanteNombre}", "Entrega confirmada con código QR". Quita el texto "Objeto registrado en Room" de la versión 1.
- Identificador visible: `#RL-` + los primeros 6 caracteres del id en mayúsculas.

**Listo cuando:** la Calculadora científica muestra las 3 etapas en verde con fechas; el Libro de cálculo solo la primera; llegar desde Detalle y desde una entrega recién confirmada funciona igual.

### D3 · Código QR (lo muestra el propietario)

```kotlin
data class CodigoQrUiState(
    val objeto: Objeto? = null,
    val textoQr: String = "",          // CodigoQr.crear(id, codigoEntrega); vacío si no hay código
    val estaCargando: Boolean = false
)
```

- Id por `savedStateHandle["id"]`, carga con `getObjetoUseCase(id)`.
- Dibujar el QR con **ZXing** a partir de `textoQr` (función propia en tu carpeta, por ejemplo `codigoqr/QrBitmap.kt`):

```kotlin
fun generarQr(texto: String, tamano: Int = 512): ImageBitmap {
    val matriz = QRCodeWriter().encode(texto, BarcodeFormat.QR_CODE, tamano, tamano)
    val pixeles = IntArray(tamano * tamano) { i ->
        if (matriz[i % tamano, i / tamano]) android.graphics.Color.rgb(36, 90, 59) else android.graphics.Color.WHITE
    }
    return Bitmap.createBitmap(pixeles, tamano, tamano, Bitmap.Config.ARGB_8888).asImageBitmap()
}
```

  En la pantalla: `val qr = remember(uiState.textoQr) { generarQr(uiState.textoQr) }` y `Image(bitmap = qr, ...)`.
  **Cuidado:** ZXing lanza una excepción si el texto está vacío. Llama a `generarQr` solo cuando `uiState.textoQr.isNotBlank()`.
- Debajo del QR, **los 6 dígitos en grande** ("Código: 482 913"): es el plan B si la cámara no lee.
- Botón "Actualizar estado" que vuelve a cargar: cuando el otro usuario confirma, el objeto pasa a `Entregado` y la pantalla cambia a "¡Entrega confirmada!" con un check.
- Si `codigoEntrega` está vacío (entraste con una cuenta que no es la dueña, o el objeto no está aceptado): mensaje "Este objeto no tiene un código de entrega activo", sin QR.

**Listo cuando:** Ana abre el QR de la Regla T y ve un QR real y el código `482913`; al leer ese QR con la cámara de cualquier celular aparece `reloop:o-regla:482913`.

### D4 · Escanear QR (lo usa el solicitante)

```kotlin
data class EscanearQrUiState(
    val pendientes: List<Objeto> = emptyList(),   // solicitanteId == yo && estado == ACEPTADO
    val idSeleccionado: String = "",              // llega por la ruta o se elige de la lista
    val codigo: String = "",                      // lo que escribe el usuario
    val error: String = "",
    val estaCargando: Boolean = false,
    val idEntregado: String = ""                  // cuando tiene valor, la pantalla navega al pasaporte
)
```

- El id es **opcional**: `savedStateHandle["id"]` puede ser null (desde Inicio se entra sin id).
- La pantalla tiene dos caminos que terminan en la misma función `confirmar(id, codigo)`:

  **Camino 1 · Código escrito (obligatorio, funciona siempre, también en el emulador).**
  Lista de "Entregas por confirmar" para elegir el objeto (si llegó un id, ya viene elegido), un `CampoTexto` numérico para los 6 dígitos y el botón "Confirmar entrega".

  **Camino 2 · Escanear con la cámara (nivel 2).**
  Botón "Escanear QR" que abre el escáner de Google:

```kotlin
val opciones = GmsBarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build()
GmsBarcodeScanning.getClient(context, opciones).startScan()
    .addOnSuccessListener { codigoLeido -> viewModel.onQrLeido(codigoLeido.rawValue.orEmpty()) }
    .addOnFailureListener { viewModel.onEscaneoFallido() }
```

  `onQrLeido(texto)` usa `CodigoQr.leer(texto)`: si devuelve null → Snackbar "Ese QR no es de ReLoop"; si devuelve `(id, codigo)` → `confirmar(id, codigo)`.
  No necesita permiso de cámara ni CameraX. Requiere un celular (o emulador) con Google Play Services; por eso el camino 1 es el que garantiza la demo.

- `confirmar(id, codigo)`: `confirmarEntregaUseCase(id, codigo)`. Éxito → Snackbar SUCCESS "¡Entrega confirmada!" y `idEntregado = id` → la pantalla llama `onEntregado(id)` (Daniel ya lo dirige al Pasaporte). Falla → `error = mensaje` ("Código incorrecto", "El código tiene 6 dígitos"…).
- Conserva el marco oscuro con las esquinas verdes de la versión 1 como ilustración, encima del botón de escanear.

**Listo cuando:** Ana entra a Escanear, ve "Audífonos de diadema" como entrega pendiente, escribe `000000` → "Código incorrecto"; escribe `731550` → "¡Entrega confirmada!" y se abre el Pasaporte con las 3 etapas en verde; el objeto ya no aparece en Solicitudes y el EcoImpacto de Ana sube en 1.

---

## 3. Niveles (por si falta tiempo)

| Nivel | Qué incluye |
|---|---|
| **1 · obligatorio** | D1, D2, D3 con QR real, D4 con código escrito |
| **2 · deseable** | D4 con el escáner de Google; D3 que se actualice solo cada 5 segundos mientras está abierta |
| **3 · extra** | Botón "Compartir pasaporte" (texto con la historia del objeto por `Intent.ACTION_SEND`) |

Si el escáner da problemas en tu equipo, no te quedes atascado: el nivel 1 completo ya demuestra el ciclo entero.

---

## 4. Lista de pruebas antes de cada Pull Request

Flujo completo con las dos cuentas de prueba (clave `123456`):

- [ ] Carlos entra → Solicitudes · Enviadas muestra la Mochila "esperando respuesta".
- [ ] Ana entra → Recibidas → Aceptar la Mochila → Ver código QR → anota el código.
- [ ] Carlos entra → Enviadas → la Mochila dice "¡Aceptada!" → Confirmar entrega → código malo → error; código bueno → Pasaporte completo.
- [ ] Ana entra → la Mochila ya no está en Solicitudes; en Mis objetos figura "Entregado".
- [ ] Rechazar una solicitud y comprobar que el objeto vuelve a Explorar.
- [ ] Abrir el QR de un objeto con una cuenta que no es la dueña → mensaje, sin QR.
- [ ] Pasaporte de un objeto en cada estado (1, 2 y 3 etapas).
- [ ] (Nivel 2) Con dos celulares: uno muestra el QR, el otro lo escanea.

---

## 5. Preguntas de defensa

1. ¿Qué texto lleva el QR y por qué incluye el id del objeto además del código?
2. ¿Por qué el código lo genera el servidor y no la app?
3. ¿Por qué el solicitante no puede ver el código en su propia pantalla?
4. ¿Quién valida que el código sea correcto y qué pasa si alguien intenta confirmar una entrega que no es suya?
5. ¿Qué librería dibuja el QR y cuál lo lee? ¿Por qué no hizo falta pedir permiso de cámara?
6. ¿Cómo sabe el Pasaporte qué etapas pintar en verde?
7. ¿Por qué después de "Aceptar" vuelves a pedir la lista al servidor en vez de modificarla en memoria?

---

## 6. Encargo para tu IA

```
Con el contexto común que te pegué, soy responsable del módulo D de ReLoop.
Empecemos por la tarea D1 (Solicitudes). Te pego mi archivo de módulo y el código de la versión 1
de la pantalla (referencia_v1/SolicitudesScreen.kt).

Dame los archivos completos de presentation/screens/solicitudes/ (UiState, ViewModel, Screen y,
si la pantalla pasa de 200 líneas, un SolicitudesComponentes.kt con la tarjeta):
- conserva el diseño de la versión 1, pero sin EncabezadoAtras (ahora hay barra superior global);
- el id del objeto es String; los solicitantes ya no son simulados;
- la firma de SolicitudesScreen debe ser exactamente la del contrato;
- usa solo componentes de presentation/common y colores de ui/theme.
Después explícame, en simple, qué pasa desde que toco "Aceptar" hasta que la tarjeta cambia,
y hazme 3 preguntas para comprobar que lo entendí.
Cuando te confirme que compila y funciona, seguimos con D2.
```
