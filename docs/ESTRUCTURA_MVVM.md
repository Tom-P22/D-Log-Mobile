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

PantallaBase adapta padding y ancho máximo según Window Size Classes. El catálogo
coloca sus acciones en columna en compacto y en fila cuando hay más espacio.
El formulario tiene scroll y MainActivity ajusta el contenido al teclado.
El cambio de favorito anima el color del botón y muestra el resultado al guardar.

Ver [pruebas y limitaciones](PRUEBAS.md).
