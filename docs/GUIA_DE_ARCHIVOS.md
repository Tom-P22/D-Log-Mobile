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

## Pruebas

- LoginViewModelTest: validaciones y credenciales de prueba.
- SolucionViewModelTest: catálogo, búsqueda, filtros e IDs favoritos.
- ConsultaViewModelTest: campos obligatorios, correo, longitud y limpieza.
- FlujoAppTest: navegación, login, favoritos, consulta y regreso para editar.
- RecursosNativosTest: preferencias compartidas, URI y cancelación de fotografía.

Las pruebas locales están en src/test y las del dispositivo en src/androidTest.
