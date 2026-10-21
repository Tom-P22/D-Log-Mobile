# Guía de archivos del proyecto

Equipo: Tomás Peña, Marcelo Salas y Fernando Villalobos.

Este documento explica la base actual de D-Log-Mobile. Describe lo que hace el
código existente y distingue las funciones que todavía faltan. Las rutas se indican
desde la raíz del repositorio. No incluye cada archivo generado de caché o compilación.

## 1. Cómo leer el proyecto

El patrón MVVM separa tres responsabilidades:

| Parte | Ubicación | Pregunta que responde |
| --- | --- | --- |
| Model | `model/` | ¿Qué datos y estados utiliza la aplicación? |
| View | `ui/screens/` y `ui/components/` | ¿Qué ve el usuario y qué acciones puede realizar? |
| ViewModel | `viewmodel/` | ¿Cómo se actualizan los datos ante una acción? |

El repositorio entrega los datos de prueba. La navegación conecta las vistas y sus
ViewModels. Todos los archivos Kotlin de la aplicación descritos a continuación
están bajo `app/src/main/java/com/duoc/d_log_mobile/`.

Recorrido de inicio:

```text
AndroidManifest.xml
  → MainActivity.kt
    → DLogMobileTheme
      → AppNavigation.kt
        → LoginScreen.kt
```

Recorrido de una actualización:

```text
Usuario escribe en una pantalla
  → la pantalla llama una función recibida
  → el ViewModel modifica su estado
  → AppNavigation observa el cambio
  → la pantalla recibe y muestra el nuevo estado
```

## 2. Entrada de la aplicación

### `MainActivity.kt`

Es la actividad que Android abre al iniciar la aplicación.

- `onCreate()` configura el contenido inicial.
- `enableEdgeToEdge()` permite dibujar hasta los bordes de la ventana.
- `setContent` inicia la interfaz de Jetpack Compose.
- `DLogMobileTheme` aplica el tema visual.
- `Scaffold` aporta la estructura general; su `innerPadding` se pasa al contenido
  para respetar los espacios de las barras del sistema.
- `AppNavigation` se encarga de mostrar la pantalla correspondiente.

Aquí no se implementan validaciones ni se escriben todas las pantallas. Para cambiar
un formulario se modifica su pantalla y su ViewModel.

## 3. Modelos: carpeta `model/`

### `model/Solucion.kt`

Define una ficha mediante una `data class`. Sus campos son `id`, `nombre`,
`descripcion`, `categoria`, `sistemaConstructivo`, `material`, `aplicacion` y
`recursoRelacionado`.

El `id` identifica una ficha aunque su nombre cambie. Se utiliza para abrir el detalle
y registrar favoritos. Esta clase solo describe datos: no dibuja la ficha ni la guarda.

### `model/SolucionUiState.kt`

Describe el estado que comparten Catálogo, Detalle y Favoritos:

- `soluciones`: lista de fichas disponibles.
- `favoritos`: conjunto de IDs marcados, sin repetirlos.
- `solucionesFavoritas`: propiedad que filtra la lista y devuelve las fichas cuyos
  IDs están en `favoritos`.

La lista de favoritos se obtiene a partir de los datos actuales; no existe una segunda
lista independiente que haya que actualizar a mano. `busquedaTexto` y
`categoriaSeleccionada` guardan los criterios; `solucionesFiltradas` combina ambos.
La búsqueda ignora mayúsculas y revisa nombre y descripción.

### `model/LoginUiState.kt`

Contiene el campo `correo` que muestra la pantalla de entrada, inicialmente vacío.
Es el contrato inicial del formulario. Aún no tiene clave, errores ni sesión activa;
esos datos deberán incorporarse al implementar el login.

### `model/ConsultaUiState.kt`

Contiene `nombre`, `correo`, `categoria` y `descripcion`, todos inicialmente vacíos.
Consulta muestra campos editables y Resumen muestra esos mismos datos.
Incluye `errores: ConsultaErrores` con un mensaje opcional por campo.
No incluye todavía fotografía.

### `model/ConsultaErrores.kt`

Contiene `nombre`, `correo`, `categoria` y `descripcion` como `String?`.
`null` significa que el campo no tiene un error pendiente de mostrar.

## 4. Datos: carpeta `repository/`

### `repository/SolucionRepository.kt`

Separa el origen de los datos de las pantallas.
`obtenerSoluciones()` devuelve seis fichas sintéticas con IDs estables.
`obtenerSolucionPorId(id)` devuelve la ficha solicitada o `null`.

`SolucionViewModel` consulta este repositorio; las pantallas no lo consultan directamente.
Para ampliar el catálogo se agregarán fichas aquí. No hay conexión a internet,
base de datos ni almacenamiento persistente en este archivo.

## 5. Lógica y estado: carpeta `viewmodel/`

Los tres ViewModels usan el mismo esquema:

```kotlin
private val _uiState = MutableStateFlow(ConsultaUiState())
val uiState = _uiState.asStateFlow()
```

`_uiState` es privado y permite cambiar el estado desde el ViewModel. `uiState` permite
observarlo desde fuera sin exponer directamente la posibilidad de modificarlo.
`copy()` crea un estado con un campo actualizado y mantiene los demás valores.

### `viewmodel/LoginViewModel.kt`

Administra `LoginUiState`.

- `actualizarCorreo(correo)`: guarda el texto ingresado.
- `limpiarCorreo()`: reemplaza el estado por uno inicial vacío.

LoginScreen informa lo que escribe el usuario y AppNavigation observa el estado.
No comprueba formato de correo, clave ni credenciales. El acceso actual es únicamente
un recorrido de prueba.

### `viewmodel/SolucionViewModel.kt`

Obtiene las fichas desde `SolucionRepository` al crear su estado y administra los
favoritos temporales. También actualiza búsqueda y categoría mediante
`actualizarBusqueda(texto)` y `seleccionarCategoria(categoria)`.

- `obtenerSolucion(id)`: busca una ficha y devuelve `null` si no existe.
- `alternarFavorito(id)`: comprueba que la ficha exista; si ya está marcada la quita,
  y si no, agrega su ID.
- `limpiarFavoritos()`: vacía los IDs marcados manteniendo el catálogo.

Catálogo, Detalle y Favoritos reciben información de la misma instancia de este
ViewModel. Eso permite que una acción en Detalle se refleje en Favoritos.
Los cambios viven en memoria: aún no se usa DataStore.

### `viewmodel/ConsultaViewModel.kt`

Administra una instancia de `ConsultaUiState` compartida por Consulta y Resumen.

- `actualizarNombre(valor)`: cambia el nombre.
- `actualizarCorreo(valor)`: cambia el correo.
- `actualizarCategoria(valor)`: cambia la categoría.
- `actualizarDescripcion(valor)`: cambia la descripción.
- `limpiarConsulta()`: vuelve a dejar todos los campos vacíos.

Modificar un campo conserva los demás mediante `copy()` y limpia solo su error.
`validarFormulario()` comprueba los campos obligatorios, el formato básico del correo
y la descripción de al menos diez caracteres. Guarda los errores y devuelve un Boolean.
AppNavigation abre Resumen solo si devuelve true. No se envía información.

## 6. Navegación: carpeta `navigation/`

### `navigation/Routes.kt`

Centraliza los nombres de los destinos: `login`, `catalogo`, `favoritos`, `consulta`,
`resumen` y la ruta parametrizada `detalle/{solucionId}`.

- `SOLUCION_ID` define el nombre del argumento del detalle.
- `detalle(id)` construye la dirección de una ficha concreta. Usa `Uri.encode` para
  que caracteres especiales del ID no se interpreten como partes de la ruta.

Este archivo evita repetir nombres de rutas escritos a mano en diferentes lugares.
No contiene pantallas ni procesa formularios.

### `navigation/AppNavigation.kt`

Es el punto de conexión de la base:

1. `rememberNavController()` crea y conserva el controlador de navegación.
2. `viewModel()` obtiene una instancia de cada ViewModel fuera de los destinos,
   asociada a la actividad, para compartirla al cambiar de pantalla.
3. `collectAsState()` convierte el estado observado en datos que Compose puede mostrar.
4. `NavHost` define las seis pantallas; `login` es el destino inicial.
5. Cada `composable` entrega estado y funciones a una pantalla.

Operaciones importantes:

| Operación | Qué hace en esta base |
| --- | --- |
| `navigate(...)` | Abre un destino |
| `popBackStack()` | Regresa al destino anterior |
| `popUpTo(..., inclusive = true)` | Elimina el destino indicado y los posteriores de la pila de regreso |
| `launchSingleTop = true` | Evita duplicar el destino si ya está en la parte superior |
| `navArgument(...)` | Declara el ID de la ficha como argumento de texto |

Al abrir una consulta nueva se limpian sus campos. Pasar de Consulta a Resumen no
los limpia; por eso se puede volver a editar. Finalizar limpia la consulta y regresa
al catálogo. Salir del recorrido limpia correo, favoritos y consulta, y vuelve a Login.

Los ViewModels siguen existiendo mientras exista su actividad; limpiar el estado
es una acción explícita. Esta navegación no implementa autenticación ni persistencia de sesión.

## 7. Vistas: carpeta `ui/screens/`

Las pantallas son funciones `@Composable`. Reciben `state` y funciones como
`onVolver` o `onCorreoChange`. Esas funciones también se llaman callbacks: permiten
avisar qué hizo el usuario sin que la pantalla conozca el NavController o el repositorio.

### `ui/screens/LoginScreen.kt`

Muestra «Diseña Madera», un correo de prueba y el botón para abrir el catálogo.

- Lee el correo desde `LoginUiState`.
- El campo llama a `onCorreoChange` cuando cambia el texto.
- El botón llama a `onAbrirRecorrido`.

Todavía permite entrar sin validar datos. No debe presentarse como login terminado.

### `ui/screens/HomeScreen.kt`

Muestra un buscador, categorías y tarjetas de `state.solucionesFiltradas`.
Cada tarjeta comunica su ID mediante `onDetalle`.

Incluye accesos a Favoritos, Nueva consulta y Salir. Si la lista está vacía, muestra
el mensaje «No se encontraron soluciones». El buscador y las categorías envían
acciones al ViewModel mediante los callbacks conectados en AppNavigation.

### `ui/screens/DetalleScreen.kt`

Muestra los atributos de una `Solucion`, su recurso relacionado y la acción de favorito.

- Recibe `Solucion?`: el signo `?` indica que puede no existir una ficha.
- Si recibe `null`, muestra «No se encontró la solución».
- `esFavorita` determina el texto del botón.
- `onFavorito` comunica la acción; el cambio real se hace en el ViewModel.
- `onVolver` permite regresar.

El recurso relacionado actual es un texto simulado, no una descarga ni un enlace externo.

### `ui/screens/FavoritosScreen.kt`

Recibe la lista de fichas favoritas y muestra un botón para abrir cada una.
Si la lista está vacía, informa que todavía no hay favoritos.
No calcula ni guarda favoritos: recibe el resultado preparado a partir del estado.
También permite volver a la pantalla anterior.

### `ui/screens/ConsultaScreen.kt`

Muestra cuatro `OutlinedTextField`: nombre, correo, categoría y descripción.
Cada campo utiliza el valor de `ConsultaUiState` y llama a su función de actualización.

«Ver resumen» llama a `onResumen`; «Volver» llama a `onVolver`.
Los errores se muestran con `isError`, `supportingText` e icono local `ic_error.xml`.
La categoría sigue siendo texto libre y obligatorio. Faltan cámara y galería.
Hay previews del formulario inicial y con errores en un ancho de 320 dp.

### `ui/screens/ResumenScreen.kt`

Muestra los cuatro campos recibidos desde el mismo `ConsultaUiState` usado en Consulta.
No recibe esos datos mediante argumentos de ruta ni crea una copia independiente.

- `onEditar` regresa al formulario conservando el contenido.
- `onFinalizar` permite limpiar los campos y regresar al catálogo desde AppNavigation.

Todavía no muestra fotografía ni confirma un envío real.

## 8. Componente compartido: carpeta `ui/components/`

### `ui/components/PantallaBase.kt`

Evita repetir el contenedor visual en las seis pantallas.
Recibe `titulo`, un `Modifier` opcional y `content`, que es el contenido de cada pantalla.

- `Column` organiza los elementos verticalmente.
- `fillMaxSize()` ocupa el espacio disponible.
- `verticalScroll(rememberScrollState())` permite desplazarse por el contenido.
- `padding(24.dp)` agrega margen interior.
- `Arrangement.spacedBy(16.dp)` separa los elementos.
- El título usa `MaterialTheme.typography.headlineMedium`.
- `content()` coloca los campos, textos y botones enviados por la pantalla.

`ColumnScope.() -> Unit` permite que ese contenido se escriba dentro del contexto de
una Column. Es un contenedor reutilizable; no contiene lógica de negocio.

## 9. Apariencia: carpeta `ui/theme/`

| Archivo | Función específica |
| --- | --- |
| `ui/theme/Color.kt` | Declara los colores de la plantilla en variables como Purple40 y Purple80; no configura por sí solo qué color usa cada componente |
| `ui/theme/Theme.kt` | Define DLogMobileTheme, selecciona tema claro/oscuro y aplica MaterialTheme; en Android 12 o superior puede usar los colores dinámicos del dispositivo |
| `ui/theme/Type.kt` | Define Typography y el estilo bodyLarge: familia, peso, tamaño, interlineado y espaciado |

Para personalizar el aspecto global se trabaja en estos archivos, evitando cambiar
manualmente el mismo estilo en cada pantalla. Los colores dinámicos pueden hacer
que los componentes no usen la paleta fija de Color.kt.

## 10. Configuración Android y recursos

| Ruta | Función y explicación |
| --- | --- |
| `app/src/main/AndroidManifest.xml` | Declara la aplicación, su icono, nombre, tema y MainActivity como punto de inicio; también configura respaldo y ajuste al teclado. Todavía no declara FileProvider ni permisos de cámara |
| `app/src/main/res/values/strings.xml` | Contiene app_name, usado por el manifest para el nombre visible. La mayoría de los textos de las pantallas están actualmente escritos en Kotlin |
| `app/src/main/res/values/colors.xml` | Contiene colores XML de la plantilla; es independiente de los colores Compose de Color.kt |
| `app/src/main/res/values/themes.xml` | Define el tema Android Theme.DLogMobile sin ActionBar; no sustituye el MaterialTheme de Compose |
| `app/src/main/res/xml/backup_rules.xml` | Archivo de configuración de copias de seguridad referenciado desde el manifest; conserva el contenido inicial de plantilla |
| `app/src/main/res/xml/data_extraction_rules.xml` | Configura reglas de extracción/respaldo y transferencia de datos para Android reciente; no equivale a persistir favoritos con DataStore |
| `app/src/main/res/drawable/ic_launcher_background.xml` | Define el fondo gráfico del icono de la aplicación |
| `app/src/main/res/drawable/ic_launcher_foreground.xml` | Define la figura del primer plano del icono |
| `app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml` | Combina fondo y primer plano como icono adaptativo |
| `app/src/main/res/mipmap-anydpi-v26/ic_launcher_round.xml` | Define la variante redonda adaptativa con los mismos recursos gráficos |
| `app/src/main/keepRules/rules.keep` | Lugar para reglas de conservación de código de R8. Actualmente tiene comentarios de plantilla, sin reglas específicas activas |

Iconos de imagen para distintas densidades:

| Carpeta bajo `app/src/main/res/` | Archivos | Función |
| --- | --- | --- |
| `mipmap-mdpi/` | `ic_launcher.webp`, `ic_launcher_round.webp` | Icono normal y redondo para densidad mdpi |
| `mipmap-hdpi/` | `ic_launcher.webp`, `ic_launcher_round.webp` | Variantes para densidad hdpi |
| `mipmap-xhdpi/` | `ic_launcher.webp`, `ic_launcher_round.webp` | Variantes para densidad xhdpi |
| `mipmap-xxhdpi/` | `ic_launcher.webp`, `ic_launcher_round.webp` | Variantes para densidad xxhdpi |
| `mipmap-xxxhdpi/` | `ic_launcher.webp`, `ic_launcher_round.webp` | Variantes para densidad xxxhdpi |

Android selecciona los recursos según la configuración del dispositivo. Estas imágenes
son iconos de inicio, no fotografías ni imágenes de las fichas del catálogo.

## 11. Gradle y dependencias

| Ruta | Función y cuándo revisarla |
| --- | --- |
| `settings.gradle.kts` | Nombra el proyecto, incluye el módulo app y define repositorios para plugins/dependencias; configura también el resolutor de herramientas JVM |
| `build.gradle.kts` | Declara los plugins comunes en la raíz, sin aplicarlos a la raíz mediante apply false |
| `app/build.gradle.kts` | Aplica los plugins del módulo, configura SDK, identificador, versión, Compose, compilación y dependencias de app/pruebas |
| `gradle/libs.versions.toml` | Centraliza versiones y alias. Por ejemplo, el alias de Navigation Compose se usa desde app/build.gradle.kts |
| `gradle.properties` | Guarda opciones generales de Gradle: memoria, codificación, caché de configuración y estilo Kotlin |
| `gradle/gradle-daemon-jvm.properties` | Define Java 25 como criterio para la JVM del daemon y URLs para resolverla según plataforma |
| `gradle/wrapper/gradle-wrapper.properties` | Fija la distribución Gradle 9.6.0, su checksum, ubicación de caché y opciones de descarga |
| `gradle/wrapper/gradle-wrapper.jar` | Ejecutable del Wrapper que permite obtener y arrancar la distribución indicada; debe acompañar a los scripts |
| `gradlew.bat` | Script del Wrapper para Windows; permite ejecutar tareas como assembleDebug desde PowerShell |
| `gradlew` | Script equivalente para sistemas como Linux y macOS |

Las versiones exactas se consultan en los archivos del repositorio. Cambiar una
dependencia requiere sincronizar Gradle en Android Studio antes de compilar.
El nivel Java de compilación del módulo y la JVM que ejecuta Gradle son configuraciones diferentes.

## 12. Archivos de pruebas

| Ruta | Qué prueba actualmente |
| --- | --- |
| `app/src/test/java/com/duoc/d_log_mobile/ConsultaViewModelTest.kt` | Ocho pruebas: campos vacíos, espacios, correo inválido, mínimo de descripción, datos válidos, limpieza de errores, corrección y reinicio |
| `app/src/test/java/com/duoc/d_log_mobile/SolucionViewModelTest.kt` | Comprueba seis fichas e IDs únicos, búsqueda y categorías combinadas, recuperación del catálogo, favoritos ocultos por filtros, limpieza e IDs inexistentes |
| `app/src/test/java/com/duoc/d_log_mobile/ExampleUnitTest.kt` | Ejecuta addition_isCorrect, que comprueba 2 + 2 = 4; es un ejemplo local sin emulador |
| `app/src/androidTest/java/com/duoc/d_log_mobile/ExampleInstrumentedTest.kt` | Ejecuta useAppContext en dispositivo/emulador y comprueba el identificador com.duoc.d_log_mobile |

Estas pruebas de plantilla no validan login, favoritos ni formularios. El recorrido de
la base está descrito para comprobación manual en [ESTRUCTURA_MVVM.md](ESTRUCTURA_MVVM.md).

## 13. Git y colaboración

| Ruta | Función |
| --- | --- |
| `.gitignore` | Excluye cachés, configuración local, compilaciones, claves y otros archivos privados o generados; no deja de seguir archivos que Git ya registraba |
| `app/.gitignore` | Excluye la carpeta build del módulo app |
| `.gitattributes` | Define tratamiento de texto/binarios y finales de línea para compartir archivos entre sistemas |
| `.github/ISSUE_TEMPLATE/tarea.md` | Plantilla para describir objetivo, responsable, dependencias, criterios y evidencia de una tarea |
| `.github/pull_request_template.md` | Plantilla para explicar un cambio, enlazar su Issue y registrar verificaciones y revisión |

Las plantillas ayudan a organizar el trabajo, pero no crean tareas ni otorgan permisos
automáticamente. Esas acciones se realizan en GitHub.

## 14. Documentación del equipo

| Ruta | Qué explica |
| --- | --- |
| `README.md` | Presentación, integrantes, estado actual, alcance resumido y enlaces para empezar |
| `CONTRIBUTING.md` | Flujo de ramas, commits, pull requests y revisión entre integrantes |
| `docs/ALCANCE_DEL_PROYECTO.md` | Problema, usuarios, funcionalidades, límites y criterios del MVP terminado |
| `docs/PLAN_DE_TRABAJO.md` | Reparto propuesto, tareas, dependencias y cronograma de referencia |
| `docs/ENTORNO.md` | Instalación y comprobación de herramientas, ejecución y problemas frecuentes |
| `docs/VALIDACION_ENTORNO.md` | Registro histórico de las comprobaciones iniciales y del bloqueo encontrado en la terminal; no certifica por sí solo el estado actual de compilación |
| `docs/ESTRUCTURA_MVVM.md` | Resumen de la estructura, conexiones de pantallas, límites de la base y recorrido de prueba |
| `docs/GUIA_DE_ARCHIVOS.md` | Este documento: función de cada archivo y explicación de las conexiones |

## 15. Archivos locales y de Android Studio

`local.properties` registra dónde está instalado el SDK en cada equipo. No debe
copiarse la ruta de otro integrante ni compartirse en Git.

Android Studio también mantiene archivos dentro de `.idea/`. Se describen porque
algunos ya están versionados en el repositorio, aunque la carpeta figure en .gitignore.
No contienen las reglas del MVP y no suelen editarse a mano para implementar funciones.

| Ruta | Función |
| --- | --- |
| `.idea/.gitignore` | Excluye configuraciones personales del IDE, como workspace y shelf |
| `.idea/.name` | Guarda el nombre con que el IDE identifica el proyecto |
| `.idea/AndroidProjectSystem.xml` | Identifica Gradle como sistema de proyecto Android |
| `.idea/compiler.xml` | Guarda preferencias del compilador del IDE; no sustituye la configuración Gradle del módulo |
| `.idea/deploymentTargetSelector.xml` | Recuerda el dispositivo elegido para ejecutar; puede incluir rutas locales |
| `.idea/gradle.xml` | Registra la vinculación del IDE con el proyecto Gradle y sus módulos |
| `.idea/inspectionProfiles/Project_Default.xml` | Define inspecciones del editor, principalmente comprobaciones de previews Compose |
| `.idea/misc.xml` | Registra opciones generales del proyecto, como el JDK seleccionado en el IDE |
| `.idea/runConfigurations.xml` | Configura el comportamiento de generadores de configuraciones de ejecución/pruebas |
| `.idea/vcs.xml` | Vincula la carpeta del proyecto con Git |

Las carpetas `.gradle/`, `.kotlin/` y `build/` contienen cachés y resultados generados.
La carpeta `.git/` contiene el historial y configuración del repositorio. No son
lugares para escribir las pantallas o la lógica de la aplicación.

## 16. Ejemplo completo: escribir una consulta

1. HomeScreen avisa que se pulsó Nueva consulta.
2. AppNavigation llama a limpiarConsulta() y navega a ConsultaScreen.
3. El usuario escribe su nombre en el campo.
4. ConsultaScreen llama a onNombreChange con el texto nuevo.
5. AppNavigation conecta esa función con ConsultaViewModel.actualizarNombre().
6. El ViewModel actualiza ConsultaUiState mediante copy(nombre = valor).
7. collectAsState() observa el cambio y la pantalla recibe el nombre actualizado.
8. Ver resumen navega a ResumenScreen, que recibe el mismo estado compartido.
9. Editar vuelve sin borrar campos; Finalizar limpia el estado y vuelve al catálogo.

En este ejemplo, la pantalla dibuja, el ViewModel actualiza los datos y la navegación
cambia de destino. Mantener esa separación es la regla para extender la base.

## 17. Dónde hacer los siguientes cambios

| Necesidad | Archivos principales |
| --- | --- |
| Agregar una ficha de prueba | SolucionRepository.kt, usando los campos de Solucion.kt |
| Agregar validación de login | LoginUiState.kt y LoginViewModel.kt; mostrar errores en LoginScreen.kt |
| Agregar errores de consulta | ConsultaUiState.kt y ConsultaViewModel.kt; mostrarlos en ConsultaScreen.kt |
| Cambiar una conexión entre pantallas | Routes.kt y AppNavigation.kt |
| Cambiar estilos globales | Color.kt, Theme.kt y Type.kt |
| Agregar una biblioteca | gradle/libs.versions.toml y app/build.gradle.kts |
| Guardar sesión y favoritos | Crear el repositorio de preferencias y conectarlo con los ViewModels correspondientes |
| Agregar cámara y galería | Ampliar estado y pantalla de consulta, preparar FileProvider y revisar manifest/recursos |

Persistencia, validaciones de login y fotografías siguen pendientes. La consulta
ya tiene validaciones; ver CONSULTA_VALIDADA.md. El catálogo de seis
fichas, búsqueda, categorías y detalle ya se integró; ver INTEGRACION_CATALOGO.md.
Las responsabilidades futuras de esta tabla no describen funciones ya implementadas.
