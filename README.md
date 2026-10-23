# D-Log-Mobile · Diseña Madera

Aplicación Android para explorar un catálogo de ejemplo de soluciones constructivas
en madera. Proyecto académico DSY1105, basado en el contexto Diseña Madera – CIM UC.

## Equipo

- Tomás Peña
- Marcelo Salas
- Fernando Villalobos
- Número de grupo: pendiente de completar por el equipo.

## Funciones

- Inicio de sesión local con errores por campo y cuenta de demostración.
- Sesión y favoritos guardados con DataStore Preferences.
- Seis soluciones con búsqueda por nombre o descripción y filtro por categoría.
- Ficha de detalle con imagen de referencia y recurso técnico de ejemplo.
- Listado de favoritos, mensaje de lista vacía y acceso al detalle.
- Consulta con nombre, correo, categoría y descripción de al menos diez caracteres.
- Imagen opcional seleccionada desde galería o capturada con la cámara del dispositivo.
- Resumen que conserva los datos al volver a editar.
- Animación de favoritos y mensajes; indicadores durante lectura o escritura.
- Scroll, ajuste al teclado y distribución según Window Size Classes.

## Ejecutar

1. Abrir la carpeta del repositorio en Android Studio.
2. Sincronizar Gradle. Se necesita internet para descargar las dependencias.
3. Seleccionar un emulador o teléfono Android API 24 o superior.
4. Ejecutar el módulo app.

Cuenta de prueba:

```text
Correo: demo@disenamadera.cl
Contraseña: Demo1234
```

## Tecnología y estructura

Kotlin, Jetpack Compose, Material 3, ViewModel, StateFlow, Navigation Compose,
DataStore Preferences y contratos de cámara/galería. Un solo módulo app.

```text
com.duoc.d_log_mobile/
  MainActivity.kt
  model/          Datos y estados
  repository/     Catálogo local y preferencias
  viewmodel/      Validaciones y acciones
  navigation/     Rutas y conexión de pantallas
  ui/screens/     Pantallas
  ui/components/  Contenedor e imagen de consulta
  ui/utils/       Tamaño de ventana y URI temporal
  ui/theme/       Colores y tipografía
```

Las pantallas muestran datos y llaman funciones. Los ViewModels cambian el estado,
y los repositorios entregan las fichas o guardan preferencias. No hay interfaces
propias, inyección de dependencias ni capas adicionales.

Se conserva la configuración base: AGP 9.4.1, Gradle 9.6.0, Java 25 para el daemon,
Compose BOM 2026.02.01 y SDK de compilación/destino 37.

## Pruebas

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:lintDebug
.\gradlew.bat :app:connectedDebugAndroidTest
```

El segundo comando requiere emulador o teléfono conectado. Ver los resultados y el
recorrido de comprobación en [PRUEBAS.md](docs/PRUEBAS.md).

## Límites

- El login es una simulación local. No es autenticación para producción.
- Solo se guarda el indicador de sesión y los IDs favoritos; no se guarda la contraseña.
- Los favoritos son del dispositivo y se mantienen al cerrar sesión.
- La consulta no se envía. Los campos y la imagen son temporales y se pueden perder
  si Android termina el proceso; los ViewModels los conservan durante cambios de orientación.
- Las fotos capturadas se guardan en la caché privada. La selección de galería usa
  acceso temporal. No se crea un archivo permanente ni se carga a un servidor.
- La app usa cámara y selector externos. Solo comparte la carpeta temporal mediante
  FileProvider; no solicita acceso general al almacenamiento ni permiso directo de cámara.
- Las fichas e imágenes son ejemplos, no prestaciones certificadas ni documentación oficial.
- Antes de entregar, completar el grupo y las evidencias, revisar el acceso del docente
  y comprobar cámara/galería en un teléfono real.

El trabajo se integra en dev. Main recibe la versión verificada mediante PR.
Los commits y el push los realiza cada integrante con su cuenta.

[Plan de trabajo](docs/PLAN_DE_TRABAJO.md) · [Acuerdos de colaboración](CONTRIBUTING.md)
