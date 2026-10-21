# Base MVVM y conexión de pantallas

## Archivos y responsabilidades

```text
com.duoc.d_log_mobile/
  MainActivity.kt                  Tema, Scaffold y entrada a AppNavigation
  model/
    Solucion.kt                    Contrato de una ficha
    SolucionUiState.kt             Fichas, búsqueda, categoría y favoritos temporales
    LoginUiState.kt                Estado inicial del login
    ConsultaUiState.kt             Datos del formulario
  repository/
    SolucionRepository.kt          Seis fichas sintéticas para el catálogo
  viewmodel/
    LoginViewModel.kt              Actualiza el correo de prueba
    SolucionViewModel.kt           Entrega fichas y cambia favoritos temporales
    ConsultaViewModel.kt           Actualiza los campos compartidos
  navigation/
    Routes.kt                     Nombres de rutas y argumento del detalle
    AppNavigation.kt              NavHost, ViewModels y callbacks
  ui/
    components/PantallaBase.kt     Contenedor con título, espaciado y scroll
    screens/                      Login, Home, Detalle, Favoritos, Consulta y Resumen
    theme/                        Tema existente
```

Las vistas reciben datos y funciones; no conocen el repositorio ni el NavController.
Los ViewModels exponen StateFlow de solo lectura y procesan cambios. AppNavigation
observa el estado con `collectAsState` y conecta las acciones con los destinos.

La base usa los conceptos de las guías 7 a 11: paquetes MVVM, composables,
NavHost/NavController, data class, StateFlow y ViewModel compartido. Hay un solo
NavHost con seis destinos y una instancia de cada ViewModel, creada con `viewModel()`.
El repositorio es una clase con una lista local. Se mantienen funciones cortas y
comentarios para explicar las conexiones, sin fábricas personalizadas ni grafos anidados.

## Estado compartido

- Los tres ViewModels se crean en AppNavigation, fuera de los destinos, y quedan
  asociados a la actividad. No se crean instancias nuevas al cambiar de pantalla.
- Catálogo, detalle y favoritos reciben datos del mismo SolucionViewModel.
  Marcar una ficha se refleja en Favoritos; salir llama a `limpiarFavoritos()`.
- Consulta y Resumen reciben el estado del mismo ConsultaViewModel.
  Volver a editar conserva campos. Finalizar o abrir una nueva consulta llama a
  `limpiarConsulta()` para comenzar con campos vacíos.
- El detalle navega con un ID, no con un objeto completo. Un ID inexistente muestra
  un mensaje y permite volver.

No crear un ViewModel nuevo dentro de cada pantalla: perdería la conexión entre
destinos que deben compartir estado. Mantener la creación y su alcance en AppNavigation.

## Límites de esta base

El botón «Abrir catálogo de prueba» permite recorrer pantallas sin autenticación.
El catálogo tiene seis fichas, búsqueda por texto y filtros combinables por categoría.
No existe aún cuenta demo, clave, validación, sesión persistente, fotografía ni DataStore. Los favoritos son temporales.
El formulario permite pasar al resumen sin validar y no envía datos.

La finalidad de este incremento es preparar archivos y conexiones. Cada responsable
debe extender los contratos existentes y su ViewModel, sin duplicar las pantallas ni
presentar el recorrido provisional como el login final.

No se creó PreferenciasRepository ni un componente de fotografía vacío: se añadirán
con su implementación en las tareas de persistencia y recursos nativos.

## Reparto para continuar

- Tomás: completar LoginUiState, LoginViewModel y LoginScreen; integrar después
  preferencias y sustituir el acceso provisional por el resultado del login.
- Marcelo: ampliar SolucionRepository y SolucionUiState con el catálogo, búsqueda,
  filtros y presentación de la ficha; completar la pantalla de resumen.
- Fernando: mantener Routes y AppNavigation; completar favoritos, consulta y sus
  validaciones en ConsultaViewModel, coordinando los contratos con el equipo.

Los estados actuales son mínimos y pueden incorporar errores, carga e imagen al
desarrollar cada tarea. No modificar las rutas compartidas sin avisar al resto.

## Comprobación en Android Studio

1. Sincronizar Gradle para descargar Navigation Compose y las integraciones de Lifecycle.
2. Ejecutar `app` en el emulador.
3. Abrir catálogo de prueba → seleccionar una de las seis fichas → agregar favorito → volver → Favoritos.
   Buscar «muro», combinar con «Muros» y luego con «Pisos» para comprobar el estado vacío.
   Limpiar la búsqueda y seleccionar «Todas» para recuperar las seis fichas.
4. Volver al catálogo → Nueva consulta → escribir campos → ver resumen → editar.
5. Finalizar → Nueva consulta: los campos deben estar vacíos.
6. Salir del recorrido → volver a entrar → Favoritos: el listado debe estar vacío.
7. Comprobar también el regreso con el botón Atrás del sistema.

Para esta primera base, comprobar manualmente los pasos anteriores en el emulador.
Se conservan las pruebas de ejemplo y se agregan seis pruebas de catálogo,
filtros y favoritos temporales en SolucionViewModelTest.

El 21 de octubre de 2026 se completaron desde terminal `assembleDebug`,
`testDebugUnitTest` y `lintDebug`: APK generado, siete pruebas sin fallos y lint
sin errores (once advertencias). Las pruebas instrumentadas y el recorrido visual
no se han ejecutado. Ver [registro de integración](INTEGRACION_CATALOGO.md).

## Referencias técnicas

- [Navigation Compose y su versión](https://developer.android.com/jetpack/androidx/releases/navigation).
- [Navegación con Compose](https://developer.android.com/develop/ui/compose/navigation).
