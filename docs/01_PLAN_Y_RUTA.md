# ReLoop · Plan y ruta para la versión final

Grupo 05 · 4 integrantes · Android (Kotlin + Compose) con nube en AWS Academy

---

## 1. Dónde estamos y a dónde vamos

Revisé el proyecto actual (`Reeloop`, 38 archivos Kotlin, 2 532 líneas) contra el ejemplo final del profesor (`proyecto_final_ejemplo`).

**Lo que ya está bien y se conserva:** las 13 pantallas con su diseño, los componentes reutilizables, el tema de colores, la navegación con barra inferior y la lógica del ciclo de vida del objeto.

**La brecha con el ejemplo final del profesor:**

| Tema | Hoy en ReLoop | Ejemplo final del profesor | Qué se hace |
|---|---|---|---|
| Datos | Room local, cada celular ve solo lo suyo | API en AWS (API Gateway) con Retrofit | API propia en AWS Academy |
| Inyección | `AndroidViewModel` que crea la base a mano | Hilt (`@HiltViewModel`, `NetworkModule`) | Migrar a Hilt |
| ViewModel | Uno solo para las 13 pantallas | Uno por pantalla, con su `UiState` | Uno por pantalla |
| Casos de uso | No hay | `GetLibrosUseCase`, `GetLibroUseCase` | 12 casos de uso |
| Capa de datos | DAO dentro de `domain`, modelo con `@Entity` | DTO + `toDomain()`, dominio limpio | DTO y dominio puro |
| Errores y carga | No hay estados de carga ni de error | `estaCargando`, `CargandoScreen`, Snackbar con `UiEventBus` | Igual que el profesor |
| Login y registro | Cualquier texto entra, no valida nada | — | Cuentas reales en la nube |
| Usuario | "Ana Torres" escrito a mano en Inicio y Perfil | — | Sesión real |
| Solicitudes | Solicitantes simulados con una lista fija | — | Entre usuarios reales |
| QR | Un ícono; Escanear no hace nada | — | QR real y confirmación con código |
| Ciclo completo | Desde la app no se puede llegar a "Entregado" | — | Se completa con el QR |
| Menú de opciones | No existe | — | Menú ⋮ en la barra superior |
| Git | Un commit, sin repositorio remoto | — | GitHub con ramas por módulo |

**Resultado final:** dos personas con dos celulares (o dos cuentas) completan el ciclo real: una publica, la otra solicita, la primera acepta y muestra un QR, la segunda lo escanea, y ambas ven el Pasaporte y su EcoImpacto actualizados desde la nube.

---

## 2. Estrategia: cómo trabajan 4 personas con 4 IA sin romperse el código

1. **Un contrato único.** Modelos, repositorios, casos de uso, DTO y firmas de pantallas están fijados en `02_CONTEXTO_COMUN.md` y ya escritos en `contrato_kotlin/`. Las 4 IA parten de lo mismo.
2. **Un armazón primero (Hito 0).** Daniel deja el proyecto compilando con la arquitectura final y las 13 pantallas "pendientes". Desde ahí cada uno solo rellena sus carpetas.
3. **Modo Fake.** Las pantallas se desarrollan contra repositorios en memoria que aplican las mismas reglas que tendrá el servidor. Nadie espera a que la nube esté lista.
4. **Un interruptor.** Cuando la API pasa sus pruebas, se cambia `USAR_NUBE = true` y toda la app queda conectada, sin tocar las pantallas.
5. **Propiedad clara.** Cada carpeta tiene un dueño. Los archivos compartidos solo los toca Daniel.

### Reparto

| Módulo | Responsable (sugerido) | Entrega |
|---|---|---|
| **A · Base, nube e integración** | Daniel | Hilt + Retrofit, navegación, barra superior con menú ⋮, sesión, API en AWS, integración final |
| **B · Cuenta, Inicio e Impacto** | Cesar | Login, Registro, Inicio, EcoImpacto, Perfil |
| **C · Catálogo y publicación** | Erik | Explorar, Detalle, Publicar, Mis objetos |
| **D · Solicitudes, QR y Pasaporte** | Joel | Solicitudes, Código QR, Escanear QR, Pasaporte |

Daniel lleva la nube porque es quien tiene la cuenta de AWS Academy. Si alguien más tiene Learner Lab, puede construir la API en paralelo al Hito 0 y se gana un día.

---

## 3. Hitos

| Hito | Qué queda funcionando | Quién | Días |
|---|---|---|---|
| **0 · Armazón** | App con Hilt, navegación final, 13 pantallas pendientes, modo Fake | A | 1 |
| **1 · Pantallas** | Las 13 pantallas reales sobre Fake · API probada en Postman | B, C, D · A | 3 |
| **2 · Nube** | `USAR_NUBE = true`, flujo completo con dos cuentas reales | A + todos prueban | 1 |
| **3 · Pulido** | Escáner de cámara, detalles de nivel 2, pruebas unitarias, corrección de errores | todos | 1 |
| **4 · Cierre** | Código congelado, video, informe, ensayo de la exposición | todos | 2 |

### Calendario de 8 días

Escriban la fecha real al lado de cada día. Si tienen menos de 8, vean la sección 4.

| Día | A · Daniel | B · Cesar | C · Erik | D · Joel |
|---|---|---|---|---|
| **1** | Hito 0 completo. Al cerrar: etiqueta `hito-0` y aviso al grupo | Acceso a GitHub. Leer contexto común y su módulo. Revisar el ejemplo del profesor (`InicioViewModel`, `InicioScreen`) | Igual | Igual. Probar en un proyecto aparte que ZXing dibuja un QR |
| **2** | AWS: tablas + Lambda `reloop_auth` probada | B1 Login · B2 Registro | C1 Explorar · empezar C2 | D1 Solicitudes |
| **3** | AWS: Lambda `reloop_objetos` + API Gateway | B3 Inicio · B4 Impacto | C2 Detalle · C3 Publicar | D2 Pasaporte · D3 Código QR |
| **4** | Batería de 15 pruebas en Postman. Mezclar los primeros PR | B5 Perfil. PR de todo | C4 Mis objetos. PR de todo | D4 Escanear (código escrito). PR de todo |
| **5** | `USAR_NUBE = true`. Flujo completo con dos cuentas | Probar su módulo contra la nube y corregir | Igual | Igual |
| **6** | Corrección de errores de integración | Nivel 2 + pruebas unitarias | Nivel 2 | Nivel 2: escáner con cámara |
| **7** | **Congelar código.** APK final | Informe: su parte | Informe: su parte | Grabar el video (lab encendido) |
| **8** | Ensayo general y entrega | Ensayo | Ensayo | Ensayo |

Reunión diaria de 10 minutos: qué terminé, qué sigue, qué me bloquea.

---

## 4. Si hay menos tiempo

Nunca se recorta el Hito 0 ni la batería de pruebas de la API. Se recorta en este orden:

1. Nivel 3 de todos los módulos.
2. Escáner con cámara (D4 queda con el código escrito de 6 dígitos, que igual demuestra el ciclo).
3. Eliminar publicación (C2) y pruebas unitarias (B).
4. Pestaña "Enviadas" con diseño simple.

**Con 5 días:** día 1 Hito 0 · días 2 y 3 pantallas de nivel 1 y API · día 4 nube e integración · día 5 video y entrega.

**Plan de emergencia:** si la nube no queda estable a tiempo, se entrega con `USAR_NUBE = false` para la demo en vivo y se muestra la API funcionando en Postman. La arquitectura (Hilt, Retrofit, casos de uso, repositorios) queda completa y demostrable igual. Decidan esto como máximo el día 5, no la víspera.

---

## 5. Riesgos

| Riesgo | Cómo se evita |
|---|---|
| El Hito 0 se atrasa y bloquea a los tres | Es lo único que hace Daniel el día 1. Los archivos del contrato ya están escritos. Si a mediodía no compila, pedir ayuda ese mismo día |
| Cada IA inventa su propia arquitectura | Todos pegan el contexto común primero. En cada PR Daniel revisa: 3 archivos por pantalla, firma intacta, sin librerías nuevas |
| Conflictos en Git | Cada uno solo edita sus carpetas. `git pull origin main` cada mañana. PR pequeños |
| El lab de AWS está apagado en la exposición | Encender el lab 15 minutos antes. Tener el video grabado como respaldo. Tener listo el modo Fake |
| Se acaba el presupuesto del lab | DynamoDB en On-demand, sin EC2 ni bases de datos encendidas. Revisar el presupuesto en la página del lab |
| La Lambda devuelve un JSON distinto al DTO | Los nombres de los campos están fijados en el contrato de la API. Probar con Postman antes de conectar la app |
| El escáner no funciona en el emulador | El código escrito es el camino obligatorio; el escáner es nivel 2 y se demuestra con celulares reales |
| El profesor pregunta y alguien no sabe explicar su parte | Cada módulo trae sus preguntas de defensa. Se ensayan el día 8 |
| El profesor exige algo que no está (Room, Cognito…) | Confirmar la rúbrica hoy (sección 8) |

---

## 6. Guion de la demo y del video (5 a 7 minutos)

Dos celulares o dos emuladores, lab de AWS encendido, las tablas de DynamoDB abiertas en la computadora.

1. **Registro** de una cuenta nueva (A1). Mostrar que aparece en la tabla `reloop_usuarios`, sin contraseña visible.
2. **Publicar** un objeto con A1. Mostrarlo en `reloop_objetos`.
3. En el otro celular, entrar con A2 → **Explorar** → buscar el objeto → **Detalle** → **Solicitar**.
4. Con A1 → **Solicitudes · Recibidas** → **Aceptar** → **Ver código QR**.
5. Con A2 → **Solicitudes · Enviadas** → **Confirmar entrega** → escanear el QR del otro celular.
6. Se abre el **Pasaporte** con las 3 etapas. Mostrar el **EcoImpacto** actualizado en ambos.
7. **Menú ⋮**: Acerca de y Cerrar sesión.
8. Cierre de 1 minuto con el diagrama de arquitectura: pantalla → ViewModel → caso de uso → repositorio → Retrofit → API Gateway → Lambda → DynamoDB.

Cada integrante narra la parte de su módulo.

---

## 7. Lista de entrega final

- [ ] Repositorio con `main` compilando y etiqueta `v1.0`.
- [ ] APK de depuración generado desde `main`.
- [ ] `USAR_NUBE = true` y URL correcta.
- [ ] Flujo del guion probado dos veces seguidas sin errores.
- [ ] Video grabado con el lab encendido.
- [ ] Código de las Lambda y colección de Postman en el repositorio.
- [ ] Capturas de AWS (tablas, Lambda, rutas) en el informe.
- [ ] Diagrama de arquitectura y diagrama del ciclo de vida del objeto en el informe.
- [ ] Cada integrante responde sus preguntas de defensa.
- [ ] Sin claves, tokens ni la contraseña de nadie dentro del repositorio.

---

## 8. Por confirmar antes de empezar

1. **Fecha y hora exactas de la entrega final.** De eso depende si usan el calendario de 8 días o el comprimido.
2. **Rúbrica del proyecto final.** En especial: ¿exige persistencia local con Room además de la nube? ¿exige algún servicio concreto de AWS? Si pide Room, se agrega como caché de la lista de objetos sin cambiar el resto del plan.
3. **Quién toma cada módulo.** Los nombres del reparto son una sugerencia.
4. **Quién tiene Learner Lab.** Si solo Daniel, él construye la API; si hay otro, se adelanta trabajo.
