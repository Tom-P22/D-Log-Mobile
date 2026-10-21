# Integración del catálogo en dev

## Origen y decisiones

La rama `feature/C01-modelo-catalogo`, hasta `b957051`, partía del primer commit.
La base de integración es `dev`, actualizada hasta `2776aa6`. Se preparó un merge
sin commit para conservar los cuatro commits originales de Marcelo al confirmarlo.

- Se mantienen MainActivity, el NavHost y las seis pantallas conectadas de dev.
- Se incorporan las seis fichas, búsqueda por nombre o descripción, categorías y tarjetas.
- AppNavigation conecta búsqueda y categoría al SolucionViewModel compartido.
- El diseño DetailScreen de Marcelo se adapta a DetalleScreen; no se duplica el destino.
- Favoritos y consulta conservan sus conexiones. Los favoritos siguen siendo temporales.
- Un ID inexistente no puede guardarse como favorito y el detalle permite volver.
- Las fichas se identifican como ejemplos sintéticos, sin certificaciones técnicas.
- Se conserva la configuración versionada del IDE de dev y el cambio local previo
  de misc.xml queda fuera de los cambios preparados para el commit.
- CONTRIBUTING ahora indica ramas y PR hacia dev; main queda para la versión final verificada.

No se agregaron librerías ni se cambiaron versiones.

## Verificación ejecutada

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest --console=plain
.\gradlew.bat :app:testDebugUnitTest :app:lintDebug --console=plain
```

Ambas ejecuciones finalizaron con BUILD SUCCESSFUL.

- APK: app/build/outputs/apk/debug/app-debug.apk.
- Siete pruebas unitarias: seis de SolucionViewModelTest y el ejemplo inicial; cero fallos.
- Lint: cero errores y once advertencias (etiqueta redundante, versiones nuevas
  disponibles y recursos de colores de la plantilla sin uso).
- Sin archivos con conflictos pendientes.

## Comprobación manual pendiente

1. Abrir el recorrido de prueba y comprobar las seis tarjetas.
2. Buscar MURO, seleccionar Muros y luego Pisos: comprobar coincidencias y estado vacío.
3. Limpiar la búsqueda y elegir Todas: recuperar el catálogo completo.
4. Abrir una ficha, marcar favorito, volver y comprobarla en Favoritos; quitarla.
5. Abrir Nueva consulta, escribir datos, ver Resumen, editar y finalizar.
6. Volver al catálogo, salir y comprobar el comportamiento de Atrás del sistema.

No se ejecutó este recorrido integrado en emulador ni teléfono desde esta revisión.
No se implementó autenticación, validación de formularios, persistencia ni cámara.

## Estado de Git al preparar la entrega

El merge queda preparado en dev, sin conflictos, pendiente de commit del usuario.
No se ha hecho push. Main y la rama remota de Marcelo se conservan.
Al cerrar el merge con un commit, el historial de Marcelo pasa a formar parte de dev.
