# Alcance y desarrollo del MVP

## 1. Problema y propósito

Diseña Madera reúne información sobre soluciones constructivas en madera. El desafío
del proyecto es facilitar la búsqueda y lectura de una muestra de esas soluciones
desde un teléfono Android, con categorías comprensibles y acceso a recursos relacionados.

El objetivo general es desarrollar un MVP académico que permita explorar, buscar,
filtrar y consultar soluciones de prueba mediante una aplicación Kotlin con Jetpack
Compose y arquitectura MVVM.

Objetivos específicos:

- Organizar un catálogo local con información suficiente para comparar alternativas.
- Ofrecer un recorrido claro entre catálogo, ficha y recursos relacionados.
- Incorporar formularios con validación y retroalimentación por campo.
- Conservar sesión y favoritos entre ejecuciones.
- Integrar cámara y galería en una consulta técnica de demostración.
- Mantener responsabilidades separadas y evidencias de colaboración del equipo.

Este documento describe el resultado planificado. El estado implementado se informa
en el [README](../README.md); no se deben considerar disponibles las funciones siguientes.

## 2. Usuarios y escenario

Usuarios prioritarios: estudiantes y profesionales que necesitan explorar información
constructiva de referencia desde un dispositivo móvil. El MVP tendrá un único perfil
de usuario y una cuenta de demostración; no habrá roles de administrador.

Escenario: una persona ingresa con la cuenta demo, busca una solución por nombre,
filtra por categoría, revisa una ficha y guarda un favorito. Luego completa una consulta,
adjunta una fotografía y revisa el resumen de los datos ingresados.

El catálogo utilizará entre 6 y 8 fichas sintéticas identificadas como ejemplos.
No se presentarán sus contenidos como normativa o especificaciones oficiales.

## 3. Funcionalidades y criterios de aceptación

| ID | Funcionalidad | Criterio verificable |
| --- | --- | --- |
| RF01 | Inicio de sesión simulado | Correo vacío o inválido, clave vacía y credenciales incorrectas muestran errores y no permiten avanzar; la cuenta demo válida abre el catálogo |
| RF02 | Sesión persistente y cierre | Reabrir conserva la sesión; cerrar sesión elimina su indicador y vuelve a Login sin permitir regresar al catálogo con Atrás |
| RF03 | Catálogo local | Muestra al menos 6 soluciones con ID estable, nombre, categoría, material, descripción y recurso relacionado |
| RF04 | Búsqueda | Busca por nombre sin distinguir mayúsculas/minúsculas; quitar el texto restaura los resultados correspondientes al filtro activo |
| RF05 | Filtro por categoría | Permite elegir Todas o una categoría y combina esa selección con el texto de búsqueda |
| RF06 | Ficha de detalle | Abre la solución seleccionada por ID y muestra sus atributos y un recurso relacionado local o simulado |
| RF07 | Favoritos | Agrega y quita soluciones; el listado refleja cambios y conserva los IDs después de cerrar y reabrir la app |
| RF08 | Consulta validada | Exige nombre, correo válido, categoría y descripción; muestra errores por campo y solo permite avanzar con datos válidos |
| RF09 | Resumen | Muestra los datos de la consulta y la imagen, si existe, conservando los campos al volver a editar |
| RF10 | Galería y cámara | Permite seleccionar o capturar una fotografía y verla en el formulario; cancelar conserva la imagen anterior y no cierra la app |
| RF11 | Estados y animaciones | Informa errores, ausencia de resultados y favoritos vacíos; una animación comunica una acción como guardar un favorito o confirmar datos |
| RF12 | Adaptabilidad | Catálogo y formularios son utilizables en pantalla compacta y otra configuración, sin recortes ni campos inaccesibles con el teclado |

La fotografía será opcional. La consulta se mostrará como una demostración local:
ver el resumen no equivale a enviar una solicitud a una organización.

## 4. Límites

| Dentro del MVP | Fuera del MVP |
| --- | --- |
| Cuenta local de demostración | Registro de usuarios, recuperación de contraseña y autenticación remota |
| Catálogo sintético incluido en la aplicación | Migración de las aproximadamente 300 fichas y administración del catálogo |
| Recursos relacionados de prueba | Integraciones productivas, BIM, mapas y servicios de cálculo |
| Preferencias locales de sesión y favoritos | Backend, Firebase, sincronización o base de datos remota |
| Consulta con resumen y fotografía opcional | Envío real de solicitudes, historial persistente de consultas y almacenamiento remoto de imágenes |
| Ejecución en Android para evaluación | Publicación en tiendas o versión iOS |

Solo se persistirá un indicador de sesión y los IDs de favoritos; no se guardarán
contraseñas. El formulario y su imagen son estado temporal compartido durante el
flujo. Su recuperación tras cerrar el proceso no forma parte del alcance inicial.

## 5. Pantallas y navegación

| Pantalla | Contenido y acciones |
| --- | --- |
| LoginScreen | Correo, clave, errores e ingreso con cuenta demo |
| HomeScreen | Catálogo, búsqueda, categorías y accesos a Favoritos, Consulta y cierre de sesión |
| DetalleScreen | Información de una solución, recurso relacionado y acción de favorito |
| FavoritosScreen | Soluciones guardadas, estado vacío y acceso a detalle |
| ConsultaScreen | Campos, errores, cámara, galería, vista previa y acción Continuar |
| ResumenScreen | Datos válidos, fotografía opcional y regreso para editar |

Recorrido principal:

```text
Inicio → comprobar sesión → Login o Catálogo
Login válido → Catálogo → Detalle → recurso relacionado
                       → Favoritos → Detalle
                       → Consulta válida → Resumen → editar Consulta
Cerrar sesión → Login
```

Mientras se lee la preferencia inicial se mostrará un estado de carga, evitando
mostrar brevemente una pantalla que no corresponde. Los recursos relacionados
podrán mostrarse como una sección de la ficha, sin añadir una séptima pantalla.

## 6. Cómo se desarrollará

Se mantendrá un único módulo `app`. Cada incremento incluirá datos, lógica, interfaz
y verificación necesarios para entregar una función utilizable.

| Capa | Archivos previstos | Responsabilidad |
| --- | --- | --- |
| Modelo | Solucion, LoginUiState, ConsultaUiState | Describir datos, campos, errores y estados |
| Repositorio | SolucionRepository, PreferenciasRepository | Entregar fichas locales y gestionar preferencias con DataStore |
| ViewModel | LoginViewModel, SolucionViewModel, ConsultaViewModel | Procesar acciones, validar campos y exponer estado mediante StateFlow |
| Interfaz | Pantallas y componentes Compose | Mostrar estado y comunicar acciones mediante callbacks |
| Navegación | Routes, AppNavigation | Conectar destinos y coordinar el regreso entre pantallas |

Los composables no validarán credenciales ni leerán directamente DataStore. El detalle
recibirá un ID y resolverá la ficha correspondiente; si no existe, mostrará un mensaje
con opción de volver. Consulta y Resumen usarán la misma instancia de su ViewModel,
acotada a ese flujo. Se evitará pasar objetos completos o fotografías por las rutas.

La galería se integrará con un selector del sistema y la cámara con captura a una URI
mediante FileProvider. Antes de incorporar esa función se revisarán las dependencias
y permisos concretos que necesite la implementación.

## 7. Contratos para trabajar en paralelo

Antes de comenzar las pantallas, los tres integrantes acordarán en los Issues:

- Datos de `Solucion`: `id`, `nombre`, `descripcion`, `categoria`,
  `sistemaConstructivo`, `material`, `aplicacion` y `recursoRelacionado`.
- Categorías iniciales: Muros, Pisos, Cubiertas y Terminaciones; Todas es una opción
  del filtro, no una categoría de ficha.
- Rutas: login, catálogo, detalle con ID, favoritos, consulta y resumen.
- Estado y acciones públicos de cada pantalla, para integrar sin duplicar lógica.
- Catálogo y categorías mantenidos por Marcelo; login y sesión por Tomás;
  navegación e integración por Fernando, conforme al reparto propuesto.
- Cambios a Gradle, tema y MainActivity coordinados previamente con el equipo.

Las asignaciones son propuestas revisables. Cada persona debe implementar funciones,
probar su trabajo y revisar cambios de otra persona.

## 8. Desarrollo incremental y comprobación

1. Validar el entorno de cada integrante y acordar los contratos.
2. Construir login y catálogo en paralelo; integrar navegación y persistencia de sesión.
3. Completar detalle, recursos relacionados y favoritos.
4. Implementar consulta, validaciones y resumen.
5. Añadir cámara, galería, adaptación y animaciones.
6. Ejecutar pruebas completas, corregir problemas y preparar la entrega.

Cada tarea se registrará en un Issue con responsable y criterios de aceptación.
El desarrollo irá en una rama por tarea y se integrará por pull request con revisión
de otro integrante. El [plan de trabajo](PLAN_DE_TRABAJO.md) define dependencias,
responsables propuestos y entregables.

Se probarán las reglas de validación y búsqueda con pruebas unitarias cuando estén
implementadas. Se comprobarán manualmente navegación, reinicio, tamaños de ventana,
cámara, galería y cancelaciones, registrando dispositivo/API y resultado.

## 9. Definición de MVP terminado

- [ ] Los criterios RF01–RF12 tienen evidencia de verificación.
- [ ] Compilación, pruebas de las reglas implementadas y revisión estática completadas.
- [ ] Sesión y favoritos sobreviven al reinicio; cerrar sesión funciona.
- [ ] Cámara y galería funcionan, incluidos sus casos de cancelación.
- [ ] Se probó una pantalla compacta y otra configuración.
- [ ] Los tres integrantes tienen aportes y revisiones identificables en GitHub.
- [ ] README actualizado con funciones disponibles, cuenta demo y pasos de prueba.
- [ ] Código y evidencias preparados para GitHub y AVA conforme a las instrucciones del docente.

## 10. Decisiones pendientes

- Número de grupo, nombre visible definitivo y fecha exacta de entrega.
- Confirmación del reparto y publicación de los Issues.
- Confirmación de las excepciones acordadas con el docente respecto de la organización del equipo y la planificación.
- Credenciales de demostración y contenido final de las fichas, al implementar esas tareas.

Cualquier función adicional debe acordarse con el equipo y actualizar este alcance
antes de desarrollarse, para evitar retrasar el flujo principal.
