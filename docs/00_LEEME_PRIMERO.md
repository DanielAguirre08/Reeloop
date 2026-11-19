# ReLoop · Kit del equipo para la versión final

Este kit sirve para que 4 personas, cada una con una IA distinta, terminen ReLoop sin que el código de uno rompa el de otro.

## Qué hay aquí

| Archivo | Para quién | Qué es |
|---|---|---|
| `01_PLAN_Y_RUTA.md` | Todos | Dónde estamos, a dónde vamos, hitos, calendario, riesgos y guion de la demo |
| `02_CONTEXTO_COMUN.md` | **Todos, siempre** | El contrato: arquitectura, modelos, casos de uso, plantilla de pantalla, reglas |
| `03_MODULO_A_Base_Nube_Integracion.md` | Daniel | Hito 0, AWS e integración |
| `04_MODULO_B_Cuenta_Inicio_Impacto.md` | Cesar (sugerido) | Login, Registro, Inicio, EcoImpacto, Perfil |
| `05_MODULO_C_Catalogo_Publicacion.md` | Erik (sugerido) | Explorar, Detalle, Publicar, Mis objetos |
| `06_MODULO_D_Solicitudes_QR_Pasaporte.md` | Joel (sugerido) | Solicitudes, Código QR, Escanear QR, Pasaporte |
| `07_CONTRATO_API_AWS.md` | Daniel | Tablas, rutas, reglas y pruebas de la API en AWS Academy |
| `contrato_kotlin/` | Daniel lo copia al proyecto | 36 archivos Kotlin ya escritos: modelos, repositorios, casos de uso, DTO, Retrofit, repositorios Fake y utilidades |

## Cómo se usa (cada integrante)

1. Lee `01_PLAN_Y_RUTA.md` una vez.
2. Abre una conversación nueva en tu IA y pega, en este orden:
   - el texto de arranque (sección 13 de `02_CONTEXTO_COMUN.md`),
   - `02_CONTEXTO_COMUN.md` completo,
   - el archivo de tu módulo,
   - el código de la versión 1 de la pantalla que vas a hacer (`referencia_v1/` en el repositorio).
3. Trabaja **una pantalla por conversación**, con el encargo que está al final de tu módulo.
4. Cuando compile y pase la lista de pruebas de tu módulo, abre el Pull Request.

## Orden de arranque del equipo

1. **Daniel** hace el Hito 0 (módulo A, sección 2) y avisa.
2. **Cesar, Erik y Joel** crean su rama desde `main` y empiezan sus pantallas en modo Fake.
3. **Daniel**, en paralelo, construye la API en AWS.
4. Cuando la API pasa sus pruebas, Daniel activa la nube y todos prueban su módulo otra vez.

## Sobre `contrato_kotlin/`

- Está compilado con Kotlin fuera de Android y pasó 45 comprobaciones del flujo completo (login, registro, publicar, solicitar, aceptar, rechazar, entregar con código, permisos, impacto, errores de red).
- **No** se probó dentro del proyecto Android con Hilt y Retrofit reales: esa primera compilación es parte del Hito 0.
- Las Lambda de AWS no vienen escritas: su especificación completa está en `07_CONTRATO_API_AWS.md`.
- Nadie edita estos archivos salvo Daniel. Si falta algo, se le avisa.

## Cuentas de prueba (modo Fake)

`ana@correo.com` y `carlos@correo.com`, clave `123456`.
