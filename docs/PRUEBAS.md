# Pruebas del MVP

## Resultado

- Compilación de depuración: correcta; APK en app/build/outputs/apk/debug/app-debug.apk.
- Veinte pruebas unitarias aprobadas: cinco de login, seis de catálogo/favoritos,
  ocho de consulta y el ejemplo inicial.
- Diez pruebas instrumentadas aprobadas en Medium Phone API 36.0 (Android 16).
- La suite instrumentada pasó en 1080 × 2400, 420 dpi, y en un tamaño compacto
  de 840 × 1680, 420 dpi (320 dp de ancho). También se comprobó orientación horizontal.
- Lint: sin errores. Sus avisos restantes son de versiones disponibles, recursos
  de colores de la plantilla sin uso, etiqueta redundante y ubicación del fondo bitmap.
- Se detuvo el proceso de la app y se volvió a abrir: recuperó sesión y favorito.
  Después se eliminó el favorito de prueba y se dejó la app en login.

## Qué se comprobó

| Flujo | Comprobación |
| --- | --- |
| Login | Campos vacíos, correo inválido, clave incorrecta y credenciales demo válidas |
| Sesión | Se guarda al entrar; al recrear o detener/reabrir se recupera el catálogo |
| Cerrar sesión | Vuelve al login y conserva los favoritos del dispositivo |
| Catálogo | Seis IDs únicos, búsqueda y categoría combinadas; recuperación tras limpiar filtros |
| Detalle | Selección por ID, ID inexistente y cambio de favorito |
| Favoritos | Guardar, quitar, listado reactivo, estado vacío y lectura desde DataStore |
| Consulta | Cuatro errores por campo, formato de correo, longitud mínima y corrección |
| Resumen | Mismos datos del formulario, regreso para editar y limpieza al finalizar |
| Cámara | Apertura de la cámara del emulador, captura, confirmación y vista previa |
| Galería | Selector del sistema, imagen de prueba y vista previa |
| Cancelar recursos | Regreso sin cerrar la app, datos conservados y estado de imagen anterior preservado |
| FileProvider | URI content privada, escritura y lectura de imagen en caché |
| Adaptación | Login y formulario en compacto, scroll, teclado y botones accesibles en horizontal |

La parte de almacenamiento y los contratos nativos se comprueba en RecursosNativosTest.
Los recorridos visuales están en FlujoAppTest. UI Automator se usa solo en esas pruebas
para operar la cámara y el selector externos; no forma parte del código de la app.
Los selectores de la prueba de captura corresponden a la cámara Camera2 del emulador.
En otros dispositivos, probar el recurso manualmente o adaptar esos selectores de test.

## Repetir

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:lintDebug
.\gradlew.bat :app:connectedDebugAndroidTest
```

Los informes se generan en:

- app/build/reports/tests/testDebugUnitTest/index.html
- app/build/reports/androidTests/connected/debug/index.html
- app/build/reports/lint-results-debug.html

## Capturas del emulador

- [Catálogo](capturas/catalogo.png)
- [Validación de consulta](capturas/consulta_errores.png)
- [Foto capturada con la cámara del emulador](capturas/foto_capturada.png)
- [Imagen de prueba seleccionada desde galería](capturas/foto_galeria.png)
- [Formulario horizontal](capturas/consulta_horizontal.png)

## Comprobación final en teléfono real

No se dispone de un teléfono físico conectado durante esta revisión.
Antes de entregar:

1. Instalar y entrar con demo@disenamadera.cl / Demo1234.
2. Guardar un favorito, cerrar la app, volver a abrir y comprobarlo.
3. Cerrar sesión; pulsar Atrás y comprobar que no se recupera el catálogo.
4. Probar búsqueda, filtros, detalle y quitar el favorito.
5. Probar consulta vacía, datos incorrectos y luego datos válidos.
6. Seleccionar una imagen, cancelar otra selección y confirmar que conserva la anterior.
7. Capturar una foto, confirmarla y cancelar una segunda captura.
8. Revisar la imagen en Resumen y volver a editar.
9. Probar orientación, teclado y scroll; finalizar y comprobar una nueva consulta vacía.

La consulta y las URI son temporales. Si Android termina el proceso, no se promete
recuperación del formulario o de la imagen. Sesión y favoritos sí se persisten.
