# Archivos del proyecto

Los archivos Kotlin están en app/src/main/java/com/duoc/d_log_mobile.

| Archivo | Función |
| --- | --- |
| MainActivity.kt | Tema, Scaffold, teclado y entrada a AppNavigation |
| model/Solucion.kt | Datos de la ficha |
| model/SolucionUiState.kt | Búsqueda, categoría, favoritos y guardado |
| model/LoginUiState.kt | Correo, clave, errores, sesión y carga |
| model/ConsultaUiState.kt | Datos, imagen, URI pendiente y errores |
| model/ConsultaErrores.kt | Mensajes individuales del formulario |
| repository/SolucionRepository.kt | Seis soluciones sintéticas |
| repository/PreferenciasRepository.kt | Lectura y escritura de sesión e IDs favoritos |
| viewmodel/LoginViewModel.kt | Validación de credenciales e inicio/cierre de sesión |
| viewmodel/SolucionViewModel.kt | Acciones de catálogo y favoritos |
| viewmodel/ConsultaViewModel.kt | Campos, validación y resultados de imagen |
| navigation/Routes.kt | Rutas e identificador de detalle |
| navigation/AppNavigation.kt | Conexión de seis destinos y estado compartido |
| ui/screens/LoginScreen.kt | Formulario de ingreso |
| ui/screens/HomeScreen.kt | Buscador, categorías, tarjetas y acciones |
| ui/screens/DetalleScreen.kt | Ficha y cambio de favorito |
| ui/screens/FavoritosScreen.kt | Lista de favoritos y estado vacío |
| ui/screens/ConsultaScreen.kt | Campos, cámara, galería y errores |
| ui/screens/ResumenScreen.kt | Datos válidos, imagen, edición y finalización |
| ui/components/PantallaBase.kt | Título, ancho adaptable, espaciado y scroll |
| ui/components/ImagenConsulta.kt | Lectura reducida y vista previa de la imagen |
| ui/utils/WindowSizeUtils.kt | Distingue ancho compacto de ventanas más amplias |
| ui/utils/ImagenUtils.kt | Crea una URI temporal para TakePicture |
| ui/theme/Theme.kt | Colores de madera y naturaleza, claros y oscuros |
| res/xml/file_paths.xml | Limita FileProvider a la carpeta imagenes de la caché |
| AndroidManifest.xml | Actividad inicial y FileProvider privado |

## Funciones de sesión y preferencias

### PreferenciasRepository.kt

El archivo de DataStore se llama `disena_madera`. Solo guarda el indicador de sesión
y los IDs de favoritos. Las funciones de escritura usan `suspend` y se llaman desde
una corrutina del ViewModel.

| Función o propiedad | Qué hace | Dónde se utiliza |
| --- | --- | --- |
| `sesionIniciada` | Emite el indicador guardado; usa false si todavía no existe | LoginViewModel elige el estado inicial de sesión |
| `favoritos` | Emite los IDs guardados; comienza con un conjunto vacío | SolucionViewModel actualiza el listado |
| `guardarSesion(iniciada)` | Escribe true al entrar o false al salir | LoginViewModel |
| `guardarFavoritos(ids)` | Guarda el conjunto completo de IDs, sin repetirlos | SolucionViewModel |

### LoginViewModel.kt

| Función | Qué hace |
| --- | --- |
| `actualizarCorreo(valor)` | Cambia el correo y limpia su error y el mensaje general |
| `actualizarClave(valor)` | Cambia la clave y limpia su error y el mensaje general |
| `validarFormulario()` | Comprueba campos obligatorios, formato de correo y credenciales demo; devuelve Boolean |
| `iniciarSesion()` | Valida, activa carga, guarda la sesión y limpia la clave en memoria |
| `cerrarSesion()` | Guarda false y reinicia el estado del login; conserva los favoritos |

Al crearse observa `sesionIniciada`. Mientras el dato es null, AppNavigation muestra
carga. `cargando` evita repetir el ingreso o cierre durante una escritura. Si falla
el guardado, el estado contiene un mensaje para mostrar en pantalla.

## Funciones del catálogo y favoritos

| Archivo y función | Qué hace |
| --- | --- |
| SolucionRepository: `obtenerSoluciones()` | Entrega las seis fichas incluidas en la app |
| SolucionRepository: `obtenerSolucionPorId(id)` | Busca una ficha; devuelve null si no existe |
| SolucionViewModel: `actualizarBusqueda(texto)` | Cambia el texto usado por el filtro |
| SolucionViewModel: `seleccionarCategoria(categoria)` | Cambia la categoría elegida |
| SolucionViewModel: `reiniciarFiltros()` | Limpia búsqueda y mensaje y selecciona Todas |
| SolucionViewModel: `obtenerSolucion(id)` | Consulta el repositorio para abrir el detalle |
| SolucionViewModel: `alternarFavorito(id)` | Agrega o quita un ID válido y solicita el guardado |
| SolucionViewModel: `limpiarFavoritos()` | Vacía expresamente el conjunto; no se llama al cerrar sesión |
| SolucionViewModel: `guardarFavoritos(ids, mensaje)` | Función privada que escribe preferencias y actualiza carga o resultado |

`solucionesFiltradas` combina texto y categoría. `solucionesFavoritas` obtiene las
fichas correspondientes a los IDs guardados. Ambas propiedades están en SolucionUiState.
`guardandoFavorito` evita escrituras repetidas y `mensaje` informa si se guardó o quitó.

## Funciones de consulta e imagen

Todas las funciones de esta tabla están en ConsultaViewModel.

| Función | Qué hace |
| --- | --- |
| `actualizarNombre(valor)` | Cambia nombre y limpia solo su error |
| `actualizarCorreo(valor)` | Cambia correo y limpia solo su error |
| `actualizarCategoria(valor)` | Cambia categoría y limpia solo su error |
| `actualizarDescripcion(valor)` | Cambia descripción y limpia solo su error |
| `validarFormulario()` | Comprueba los cuatro campos y la descripción mínima; devuelve Boolean |
| `prepararFoto(uri)` | Guarda la URI donde la cámara escribirá una posible foto |
| `recibirFoto(guardada)` | Acepta la foto si el contrato devuelve true; al cancelar conserva la imagen anterior |
| `actualizarImagen(uri)` | Guarda una selección de galería; con null quita la imagen |
| `mostrarErrorImagen()` | Limpia la captura pendiente y muestra un mensaje si no se pudo abrir el recurso |
| `limpiarConsulta()` | Borra campos, errores, imagen y URI pendiente |

La pantalla solo llama estas funciones. Consulta y Resumen reciben el mismo estado,
por eso editar conserva los datos y finalizar permite comenzar otra consulta vacía.

## Funciones de presentación y utilidades

| Archivo y función | Aplicación |
| --- | --- |
| AppNavigation: `AppNavigation()` | Crea repositorio y ViewModels, observa el estado y conecta las seis pantallas |
| Routes: `detalle(id)` | Construye la ruta codificando el ID de la ficha |
| ImagenUtils: `crearUriFoto(contexto)` | Crea un JPG temporal en caché y devuelve una URI de FileProvider |
| ImagenConsulta: `ImagenConsulta(uri)` | Lee una copia reducida fuera del hilo principal y muestra la imagen en consulta y resumen |
| WindowSizeUtils: `esPantallaAncha()` | Consulta Window Size Classes y devuelve si el ancho no es compacto |
| PantallaBase: `PantallaBase(...)` | Aplica título, scroll, ancho máximo y espaciado según la ventana |
| Theme: `DLogMobileTheme(...)` | Selecciona colores claros u oscuros |

La cámara usa `TakePicture`; la galería usa `GetContent`. Cancelar la galería no
llama a `actualizarImagen`, de modo que conserva la selección. FileProvider comparte
únicamente `cache/imagenes`, según `res/xml/file_paths.xml` y AndroidManifest.xml.

DetalleScreen anima el color del botón con `animateColorAsState`. Los mensajes de
login, imagen y favoritos usan `AnimatedVisibility`; los estados de lectura o
escritura muestran `CircularProgressIndicator`. MainActivity usa `imePadding` para
ajustar el contenido al teclado. El catálogo dispone acciones en columna o fila
según el ancho; el resto comparte el ancho limitado y scroll de PantallaBase.

## Pruebas

- LoginViewModelTest: validaciones y credenciales de prueba.
- SolucionViewModelTest: catálogo, búsqueda, filtros e IDs favoritos.
- ConsultaViewModelTest: campos obligatorios, correo, longitud y limpieza.
- FlujoAppTest: navegación, login, favoritos, consulta y regreso para editar.
- RecursosNativosTest: preferencias compartidas, URI y cancelación de fotografía.

Las pruebas locales están en src/test y las del dispositivo en src/androidTest.

Ver [recorridos completos](ESTRUCTURA_MVVM.md) y [resultados de pruebas](PRUEBAS.md).
