# Estructura y funcionamiento

## Modelo

Solucion describe cada ficha. SolucionUiState contiene el catálogo, la búsqueda,
la categoría, los favoritos y el estado de guardado. LoginUiState contiene los
campos, errores, carga y sesión. ConsultaUiState contiene datos, errores e imagen.

## Repositorios

SolucionRepository entrega seis fichas locales. PreferenciasRepository tiene una
única instancia de DataStore y dos claves: sesion_iniciada y favoritos.
La contraseña y la consulta no se escriben en preferencias.

## ViewModels

- LoginViewModel valida el formato del correo, la clave y la cuenta demo; guarda
  o elimina el indicador de sesión. Durante una escritura evita pulsaciones repetidas.
- SolucionViewModel busca por ID, actualiza búsqueda y categoría, observa favoritos
  y guarda sus cambios. Cerrar sesión conserva los favoritos del dispositivo.
- ConsultaViewModel valida los cuatro campos y mantiene la imagen seleccionada.
  Cancelar la cámara conserva la imagen anterior. Abrir una consulta nueva la limpia.

Los ViewModels de sesión y soluciones reciben el repositorio desde AppNavigation.
Sin él, sus pruebas unitarias comprueban reglas en memoria, sin iniciar Android.
No se usan interfaces, fábricas propias ni servicios externos.

## Pantallas y navegación

AppNavigation crea los ViewModels compartidos y observa sus StateFlow con
collectAsState. Mientras se lee la sesión muestra un indicador de carga.
Si existe sesión comienza en catálogo; si no, en login. Al iniciar o cerrar sesión
se crea la pila correspondiente, evitando volver a una sesión cerrada con Atrás.

El detalle recibe un ID. Consulta y Resumen comparten el mismo ViewModel.
Editar conserva los campos; finalizar limpia la consulta y regresa al catálogo.

## Recursos nativos y presentación

ConsultaScreen usa GetContent y TakePicture. La cámara escribe en una URI temporal
creada con FileProvider. ImagenConsulta decodifica una copia reducida fuera del hilo
principal para mostrarla en consulta y resumen.
La lectura aplica también la orientación EXIF de la fotografía. El fondo de Login y
Detalle usa ImagenBosque, con una copia reducida y sin escalado automático por densidad.
El login utiliza la tipografía del sistema definida por el tema.

PantallaBase adapta padding y ancho máximo según Window Size Classes. El catálogo
coloca sus acciones en columna en compacto y en fila cuando hay más espacio.
El formulario tiene scroll y MainActivity ajusta el contenido al teclado.
El cambio de favorito anima el color del botón y muestra el resultado al guardar.
El mensaje se asocia al ID de la ficha para que no aparezca como confirmación en otra.

## Recorridos de las funciones

### Inicio y cierre de sesión

1. LoginScreen envía cambios a `actualizarCorreo` y `actualizarClave`.
2. Al pulsar Iniciar sesión, `iniciarSesion` llama a `validarFormulario`.
3. Si los datos son correctos, `guardarSesion(true)` actualiza DataStore.
4. LoginViewModel recibe el nuevo valor; AppNavigation presenta el catálogo.
5. Al salir, `cerrarSesion` guarda false. AppNavigation reinicia filtros y consulta
   y muestra el login con una pila nueva. Los favoritos no se eliminan.

### Guardar un favorito

1. DetalleScreen llama a `alternarFavorito(id)`.
2. SolucionViewModel comprueba el ID y construye el conjunto actualizado.
3. `guardarFavoritos` escribe los IDs en PreferenciasRepository.
4. La lectura de DataStore actualiza `favoritos`; catálogo, detalle y listado
   reciben ese mismo estado. El botón cambia de color y aparece el resultado.

### Adjuntar una imagen

1. Para galería, ConsultaScreen abre `GetContent` con el tipo image/*.
2. Una selección válida llama a `actualizarImagen(uri)`; cancelar no cambia la imagen.
3. Para cámara, `crearUriFoto` prepara el archivo y `prepararFoto` guarda su URI pendiente.
4. `TakePicture` abre la cámara. `recibirFoto` acepta la nueva imagen solo si se confirmó.
5. `ImagenConsulta` muestra la URI del estado tanto en el formulario como en Resumen.

### Validar, revisar y finalizar

1. Los campos llaman a las funciones actualizar de ConsultaViewModel.
2. AppNavigation llama a `validarFormulario` al pulsar Ver resumen.
3. Con errores permanece en Consulta; sin errores navega a Resumen.
4. Editar vuelve al formulario conservando su estado. Finalizar llama a
   `limpiarConsulta` y vuelve al catálogo.

Ver [pruebas y limitaciones](PRUEBAS.md).
