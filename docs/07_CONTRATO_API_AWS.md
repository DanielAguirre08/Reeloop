# ReLoop · Contrato de la API en AWS Academy

> Lo construye el **módulo A (Daniel)**. Los demás no necesitan leerlo entero: les basta el resumen del contexto común, porque trabajan contra el modo Fake, que aplica estas mismas reglas.
>
> Si le pides a tu IA que escriba las Lambda, pégale este archivo completo. Las reglas y los **mensajes de error deben quedar idénticos** a los de aquí: son los mismos que ya usa `FakeObjetoRepository`, y así la app se comporta igual con Fake o con nube.

---

## 1. Arquitectura

```
App Android (Retrofit)
      │  HTTPS + JSON
      ▼
API Gateway · HTTP API  "reloop-api"   (us-east-1, stage $default con auto-deploy)
      ├── /auth/*      ──►  Lambda  reloop_auth      ──►  DynamoDB  reloop_usuarios
      └── /objetos/*   ──►  Lambda  reloop_objetos   ──►  DynamoDB  reloop_objetos
```

Es la misma forma de la API de libros del profesor (`https://xxxx.execute-api.us-east-1.amazonaws.com/`).

Restricciones de Learner Lab que afectan:

- Región **us-east-1**.
- **No se pueden crear roles IAM.** En cada Lambda elige *Use an existing role → `LabRole`*.
- La sesión del lab dura unas horas y hay un presupuesto. Lo que creas se conserva entre sesiones mientras el lab del curso siga vigente.
- Con el lab apagado, **comprueba si la URL sigue respondiendo** (ábrela en el navegador). Si no responde, hay que encender el lab antes de cada prueba y antes de la exposición. Graben el video final con el lab encendido.

---

## 2. Tablas DynamoDB (modo de capacidad: On-demand)

### `reloop_usuarios` — clave de partición: `correo` (String)

| Atributo | Tipo | Nota |
|---|---|---|
| `correo` | S | en minúsculas; es la clave, así el login es un `get_item` directo |
| `id` | S | `uuid4` |
| `nombre` | S | |
| `salt` | S | hex, aleatorio por usuario |
| `claveHash` | S | hex de `pbkdf2_hmac("sha256", clave, salt, 100000)` |
| `creado` | N | milisegundos |

La contraseña **nunca** se guarda en texto y `salt` / `claveHash` **nunca** salen en una respuesta.

### `reloop_objetos` — clave de partición: `id` (String)

| Atributo | Tipo | Nota |
|---|---|---|
| `id` | S | `uuid4` |
| `nombre`, `descripcion`, `categoria`, `conservacion`, `modalidad`, `estado` | S | |
| `peso` | N | |
| `propietarioId`, `propietarioNombre` | S | |
| `solicitanteId`, `solicitanteNombre` | S | `""` cuando no hay |
| `codigoEntrega` | S | 6 dígitos; `""` hasta que se acepta |
| `fechaPublicado`, `fechaSolicitado`, `fechaEntregado` | N | milisegundos; `0` = no ocurrió |

La lista se obtiene con `scan` (suficiente para el tamaño de un proyecto de curso).

---

## 3. Formato de respuesta

Siempre **HTTP 200** con esta envoltura, incluso en errores de negocio. Así el repositorio de la app usa el mismo `if (response.success) ... else throw` del ejemplo del profesor.

```json
{ "success": true,  "data": { }, "message": null, "timestamp": "2026-11-20T15:04:05Z" }
{ "success": false, "data": null, "message": "Código incorrecto", "timestamp": "2026-11-20T15:04:05Z" }
```

Cabecera: `Content-Type: application/json`. Cualquier excepción no prevista se captura, se registra con `print` (queda en CloudWatch) y responde `success:false` con `"Error interno del servidor"`.

### Objeto en JSON (lo que la app espera en `ObjetoDto`)

```json
{
  "id": "5f0c6c1e-...", "nombre": "Libro de cálculo", "descripcion": "Texto universitario...",
  "categoria": "Académico", "conservacion": "Buen estado", "peso": 0.7, "modalidad": "Donación",
  "estado": "Aceptado",
  "propietarioId": "a1...", "propietarioNombre": "Ana Torres",
  "solicitanteId": "b2...", "solicitanteNombre": "Carlos Mendoza",
  "codigoEntrega": "482913",
  "fechaPublicado": 1795000000000, "fechaSolicitado": 1795003600000, "fechaEntregado": 0
}
```

**Regla del código:** `codigoEntrega` solo viaja con valor cuando quien pregunta (`usuarioId`) es el `propietarioId`. Para cualquier otro usuario se devuelve `""`. Aplica a todas las rutas que devuelven objetos.

### Usuario en JSON (`UsuarioDto`)

```json
{ "id": "a1...", "nombre": "Ana Torres", "correo": "ana@correo.com" }
```

---

## 4. Rutas

`routeKey` es el valor que llega en `event["routeKey"]` y sirve para decidir qué hacer dentro de la Lambda.

### Lambda `reloop_auth`

| routeKey | Body | `data` | Errores (`message`) |
|---|---|---|---|
| `POST /auth/registro` | `{"nombre","correo","clave"}` | Usuario | `Completa todos los campos` · `La contraseña debe tener al menos 6 caracteres` · `Ya existe una cuenta con ese correo` |
| `POST /auth/login` | `{"correo","clave"}` | Usuario | `Correo o contraseña incorrectos` (el mismo mensaje si el correo no existe o la clave está mal) |

El correo se guarda y se busca con `strip().lower()`.

### Lambda `reloop_objetos`

| routeKey | Entrada | `data` |
|---|---|---|
| `GET /objetos` | query `usuarioId` | `{"items":[Objeto...], "nextKey": null}` ordenado por `fechaPublicado` descendente |
| `GET /objetos/{id}` | query `usuarioId` | Objeto |
| `POST /objetos` | body `{"nombre","descripcion","categoria","conservacion","peso","modalidad","propietarioId","propietarioNombre"}` | Objeto creado (`estado: "Disponible"`, `fechaPublicado: ahora`) |
| `DELETE /objetos/{id}` | query `usuarioId` | `{"id": "..."}` |
| `POST /objetos/{id}/solicitar` | body `{"usuarioId","usuarioNombre"}` | Objeto |
| `POST /objetos/{id}/aceptar` | body `{"usuarioId"}` | Objeto (con `codigoEntrega`) |
| `POST /objetos/{id}/rechazar` | body `{"usuarioId"}` | Objeto |
| `POST /objetos/{id}/entregar` | body `{"usuarioId","codigo"}` | Objeto |

### Reglas y mensajes (en este orden de comprobación)

| Acción | Comprobación | `message` si falla |
|---|---|---|
| cualquiera con `{id}` | el objeto existe | `El objeto no existe` |
| publicar | nombre y descripción no vacíos, `peso > 0`, `propietarioId` presente | `Completa todos los campos` |
| eliminar | `usuarioId == propietarioId` | `Solo el propietario puede eliminar el objeto` |
| eliminar | `estado == "Disponible"` | `No puedes eliminar un objeto con una solicitud en curso` |
| solicitar | `usuarioId != propietarioId` | `No puedes solicitar tu propio objeto` |
| solicitar | `estado == "Disponible"` | `El objeto ya no está disponible` |
| aceptar / rechazar | `usuarioId == propietarioId` | `Solo el propietario puede responder la solicitud` |
| aceptar / rechazar | `estado == "Solicitado"` | `La solicitud ya fue respondida` |
| entregar | `usuarioId == solicitanteId` | `Solo quien solicitó el objeto puede confirmar la entrega` |
| entregar | `estado == "Aceptado"` | `Este objeto no tiene una entrega pendiente` |
| entregar | `codigo == codigoEntrega` | `Código incorrecto` |
| ruta desconocida | — | `Ruta no encontrada` |

Efecto de cada acción:

- **solicitar** → `estado="Solicitado"`, guarda `solicitanteId`, `solicitanteNombre`, `fechaSolicitado=ahora`.
- **aceptar** → `estado="Aceptado"`, `codigoEntrega` = 6 dígitos aleatorios (`100000`–`999999`).
- **rechazar** → `estado="Disponible"`, `solicitanteId=""`, `solicitanteNombre=""`, `fechaSolicitado=0`.
- **entregar** → `estado="Entregado"`, `fechaEntregado=ahora`.

Implementación sencilla recomendada: `get_item` → validar → modificar el diccionario → `put_item`.

---

## 5. Detalles técnicos que suelen romper la Lambda

1. **Formato del evento (HTTP API, payload 2.0):**
   - ruta: `event["routeKey"]`, por ejemplo `"POST /objetos/{id}/solicitar"`.
   - id: `event["pathParameters"]["id"]`.
   - query: `event.get("queryStringParameters") or {}`.
   - body: `json.loads(event.get("body") or "{}")`. Si `event.get("isBase64Encoded")` es verdadero, primero `base64.b64decode`.
2. **Números de DynamoDB:** `boto3` devuelve `Decimal`, y `json.dumps` falla con `Decimal`. Hay que convertir: entero si no tiene decimales, `float` si los tiene. Al guardar, `peso` debe ir como `Decimal(str(peso))` (boto3 no acepta `float`).
3. **Respuesta:** `{"statusCode": 200, "headers": {"Content-Type": "application/json"}, "body": json.dumps(cuerpo, ensure_ascii=False)}`. `body` es un **texto**, no un diccionario.
4. **Hora:** `int(time.time() * 1000)` para las fechas del objeto; `timestamp` en ISO UTC.
5. **Nombres de tabla** en variables de entorno de la Lambda (`TABLA_USUARIOS`, `TABLA_OBJETOS`), no escritos dentro del código.
6. **Runtime:** Python 3.12 o superior. Solo biblioteca estándar + `boto3` (ya viene en Lambda): no hace falta subir paquetes.
7. **Tiempo de espera** de la Lambda: 10 segundos (el valor por defecto de 3 puede quedar corto en el primer arranque).

---

## 6. Pasos en la consola (orden recomendado)

1. AWS Academy → Learner Lab → **Start Lab** → cuando el círculo esté verde, abrir la consola. Verificar región `us-east-1`.
2. **DynamoDB** → Create table → `reloop_usuarios`, partition key `correo` (String), On-demand. Repetir con `reloop_objetos`, partition key `id` (String).
3. **Lambda** → Create function → `reloop_auth`, Python, *Use an existing role → LabRole*. Pegar el código → Deploy. Variables de entorno. Timeout 10 s. Repetir con `reloop_objetos`.
4. Probar cada Lambda con un **evento de prueba** de la consola antes de tocar API Gateway (sección 7).
5. **API Gateway** → Create API → **HTTP API** → nombre `reloop-api`.
   - Integraciones: las dos Lambda.
   - Rutas: las 10 de la sección 4, cada una apuntando a su Lambda.
   - Stage `$default` con auto-deploy.
6. Copiar la **Invoke URL**. En la app va en `NetworkModule` como `BASE_URL` y **debe terminar en `/`**.
7. Probar las 10 rutas con Postman o `curl` (sección 8) y guardar la colección en el repositorio (`docs/postman/`).

---

## 7. Evento de prueba para la consola de Lambda

```json
{
  "routeKey": "POST /auth/registro",
  "rawPath": "/auth/registro",
  "pathParameters": null,
  "queryStringParameters": null,
  "isBase64Encoded": false,
  "body": "{\"nombre\":\"Ana Torres\",\"correo\":\"ana@correo.com\",\"clave\":\"123456\"}"
}
```

Para otra ruta se cambia `routeKey`, `pathParameters` (por ejemplo `{"id": "..."}`) y `body`.

---

## 8. Batería de pruebas (hazla completa antes de conectar la app)

Sustituye `$URL` por la Invoke URL. Anota los `id` que devuelve cada paso.

```bash
# 1. Registrar dos usuarios
curl -s -X POST $URL/auth/registro -H "Content-Type: application/json" \
  -d '{"nombre":"Ana Torres","correo":"ana@correo.com","clave":"123456"}'
curl -s -X POST $URL/auth/registro -H "Content-Type: application/json" \
  -d '{"nombre":"Carlos Mendoza","correo":"carlos@correo.com","clave":"123456"}'

# 2. Login correcto y login con clave mala
curl -s -X POST $URL/auth/login -H "Content-Type: application/json" -d '{"correo":"ana@correo.com","clave":"123456"}'
curl -s -X POST $URL/auth/login -H "Content-Type: application/json" -d '{"correo":"ana@correo.com","clave":"mala"}'

# 3. Ana publica
curl -s -X POST $URL/objetos -H "Content-Type: application/json" \
  -d '{"nombre":"Libro de cálculo","descripcion":"Con ejercicios","categoria":"Académico","conservacion":"Buen estado","peso":0.7,"modalidad":"Donación","propietarioId":"ID_ANA","propietarioNombre":"Ana Torres"}'

# 4. Lista y detalle
curl -s "$URL/objetos?usuarioId=ID_CARLOS"
curl -s "$URL/objetos/ID_OBJETO?usuarioId=ID_CARLOS"

# 5. Carlos solicita, Ana acepta
curl -s -X POST $URL/objetos/ID_OBJETO/solicitar -H "Content-Type: application/json" -d '{"usuarioId":"ID_CARLOS","usuarioNombre":"Carlos Mendoza"}'
curl -s -X POST $URL/objetos/ID_OBJETO/aceptar   -H "Content-Type: application/json" -d '{"usuarioId":"ID_ANA"}'

# 6. Carlos confirma con el código que vio Ana
curl -s -X POST $URL/objetos/ID_OBJETO/entregar -H "Content-Type: application/json" -d '{"usuarioId":"ID_CARLOS","codigo":"CODIGO"}'
```

| # | Caso | Resultado esperado |
|---|---|---|
| 1 | registrar correo nuevo | `success:true`, sin `salt` ni `claveHash` en `data` |
| 2 | registrar el mismo correo otra vez | `Ya existe una cuenta con ese correo` |
| 3 | login correcto / clave mala | usuario / `Correo o contraseña incorrectos` |
| 4 | publicar | `estado:"Disponible"`, `fechaPublicado > 0`, `peso` sale como número |
| 5 | lista pedida por Carlos | `codigoEntrega:""` en los objetos de Ana |
| 6 | Ana solicita su propio objeto | `No puedes solicitar tu propio objeto` |
| 7 | Carlos solicita | `estado:"Solicitado"` con sus datos |
| 8 | Carlos intenta aceptar | `Solo el propietario puede responder la solicitud` |
| 9 | Ana acepta | `estado:"Aceptado"`, `codigoEntrega` de 6 dígitos |
| 10 | la misma lista pedida por Ana y por Carlos | Ana ve el código, Carlos ve `""` |
| 11 | Carlos entrega con código malo | `Código incorrecto` |
| 12 | Carlos entrega con el código bueno | `estado:"Entregado"`, `fechaEntregado > 0` |
| 13 | Ana elimina un objeto entregado | `No puedes eliminar un objeto con una solicitud en curso` |
| 14 | rechazar una solicitud pendiente | vuelve a `Disponible`, solicitante vacío |
| 15 | `GET /objetos/no-existe` | `El objeto no existe` |

---

## 9. Límites conocidos (decirlos en la exposición si preguntan)

- **No hay tokens de sesión.** La app envía `usuarioId` y el servidor confía en él. Es suficiente para el curso; en producción se usaría Amazon Cognito con JWT (Cognito está disponible en Learner Lab, queda como mejora).
- La lista usa `scan` sin paginar; con miles de objetos habría que paginar con `nextKey` (el campo ya existe en la respuesta).
- La API es pública: cualquiera con la URL puede llamarla. No publiquen la URL fuera del equipo.
