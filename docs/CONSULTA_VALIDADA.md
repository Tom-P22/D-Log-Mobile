# Consulta validada e imagen de referencia

## Archivos y responsabilidades

| Archivo | Responsabilidad |
| --- | --- |
| model/ConsultaUiState.kt | Guarda los cuatro campos, errores, imagen seleccionada y captura pendiente |
| model/ConsultaErrores.kt | Un mensaje de error opcional por campo |
| viewmodel/ConsultaViewModel.kt | Actualiza campos e imagen, valida y recibe el resultado de cámara |
| ui/screens/ConsultaScreen.kt | Dibuja campos, errores, botones de cámara/galería y vista previa |
| navigation/AppNavigation.kt | Valida antes de navegar; comparte el ViewModel con Resumen |
| ui/components/ImagenConsulta.kt | Muestra una copia reducida de la imagen seleccionada |
| ui/utils/ImagenUtils.kt | Crea la URI temporal para la cámara |
| res/xml/file_paths.xml y AndroidManifest.xml | Configuran la carpeta compartida por FileProvider |
| res/drawable/ic_error.xml | Icono local reutilizado cuando hay error |
| ConsultaViewModelTest.kt | Ocho pruebas locales de las reglas del formulario |

La imagen es opcional y temporal. El archivo compartido está en la caché privada;
no se sube a un servidor ni se guarda como parte de los favoritos.

## Reglas

- Nombre, correo, categoría y descripción son obligatorios; solo espacios no cuentan.
- El correo tiene una comprobación básica de texto@dominio.extensión, sin espacios
  interiores ni más de un @. Se ignoran espacios de los extremos al validarlo.
- Descripción: mínimo diez caracteres, sin contar espacios de los extremos.
- La categoría se escribe como texto libre, con ejemplos del catálogo; solo se exige que no esté vacía.
- El botón Ver resumen permanece disponible para poder mostrar todos los errores
  al pulsarlo. Si hay errores, la navegación se bloquea.
- Al editar se limpia solo el error de ese campo. Al pulsar de nuevo se vuelve a validar.
- Editar desde Resumen conserva los campos. Finalizar o abrir una consulta nueva los limpia.

## Funciones de fotografía

- `prepararFoto(uri)` guarda el destino de una captura sin sustituir todavía la imagen actual.
- `recibirFoto(true)` acepta la captura; `recibirFoto(false)` conserva la selección anterior.
- `actualizarImagen(uri)` recibe una selección de galería; con null permite quitarla.
- `mostrarErrorImagen()` muestra un mensaje si no se pudo abrir el recurso.
- `limpiarConsulta()` elimina también la imagen al finalizar o empezar una nueva consulta.

ConsultaScreen ejecuta los contratos con `rememberLauncherForActivityResult`.
La lectura de la vista previa se realiza en ImagenConsulta. Resumen recibe la misma
URI del ViewModel compartido. Si Android termina el proceso, los campos y la imagen
pueden perderse; no se escriben en DataStore.

## Verificaciones ejecutadas

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:lintDebug --console=plain
```

Resultado: BUILD SUCCESSFUL; veinte pruebas locales sin fallos, incluidas ocho de consulta;
lint sin errores. Se generó el APK de depuración.
El formulario se comprobó en emulador, incluyendo regreso para editar, limpieza
y cancelación de cámara y galería. El equipo también confirmó ejecución manual
en un POCO X5 Pro con HyperOS; el detalle de comprobaciones está en PRUEBAS.md.

## Cómo probar en Android Studio

1. Ejecutar app → ingresar con la cuenta demo → Nueva consulta.
2. Pulsar Ver resumen sin escribir: debe seguir en Consulta, con cuatro errores e iconos.
3. Probar campos con solo espacios, correo sin @ y descripción de nueve caracteres.
4. Corregir el nombre y comprobar que solo desaparece el error de ese campo.
5. Completar: Ana Pérez, ana@correo.cl, Muros, «Necesito información sobre un muro».
6. Pulsar Ver resumen: comprobar los cuatro datos.
7. Pulsar Editar consulta: comprobar que se conservan; borrar un dato y verificar
   que no permite avanzar al resumen hasta corregirlo.
8. Finalizar y abrir Nueva consulta: comprobar campos y errores vacíos.
9. Revisar pantalla pequeña, scroll y teclado: se debe poder alcanzar Ver resumen y Volver.

La consulta permite adjuntar una fotografía desde cámara o galería. Resumen muestra
la misma imagen. Cancelar mantiene la selección anterior; al finalizar se limpia.
Ver [estado de las pruebas](PRUEBAS.md).
