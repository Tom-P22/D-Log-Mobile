# Preparación del entorno

## Herramientas comunes

Usar las versiones declaradas en el proyecto. El equipo no debe cambiar SDK, AGP,
Gradle o plugins para resolver unilateralmente un problema de instalación.

- Git con acceso a GitHub.
- Android Studio compatible con AGP 9.4.1. Referencia detectada en el equipo revisado:
  instalación identificada como AndroidStudio2026.1.4.
- JDK 25 para la JVM del daemon, tal como pide `gradle/gradle-daemon-jvm.properties`.
- Android SDK Platform 37, Platform-Tools y las Build-Tools que solicite Gradle.
- Android Emulator y una imagen de sistema para el dispositivo virtual, o teléfono Android.

La configuración actual admite dispositivos desde API 24. Para las primeras pruebas
del equipo se propone usar el mismo emulador con API 37; eso no cambia el SDK mínimo.

## Android Studio

1. Clonar y abrir la raíz que contiene `settings.gradle.kts`.
2. En SDK Manager, instalar Platform 37 y las herramientas indicadas arriba.
3. En Settings > Build, Execution, Deployment > Build Tools > Gradle, revisar el JDK.
   Usar JDK 25; comprobar la versión del JBR incluido antes de seleccionarlo.
4. Ejecutar Sync Project with Gradle Files. Aceptar las licencias que solicite el SDK.
5. Dejar que Android Studio genere `local.properties` con la ubicación local del SDK.
   Ese archivo no se comparte ni se copia desde el equipo de otra persona.
6. Crear el dispositivo en Device Manager o conectar un teléfono con depuración USB.
7. Ejecutar la configuración `app`. La base actual muestra «Diseña Madera» y el botón
   «Abrir catálogo de prueba»; consultar [ESTRUCTURA_MVVM.md](ESTRUCTURA_MVVM.md).

No es necesario instalar Kotlin ni Gradle por separado. El Wrapper fija Gradle y el
proyecto declara sus plugins. Los criterios del daemon pueden descargar un JDK 25
si no encuentran uno instalado, por lo que la primera sincronización necesita internet.

## Terminal de Windows (PowerShell)

Ejecutar los comandos desde la raíz del repositorio. Si la terminal no encuentra el
Java correcto, definirlo solo para esa sesión. Este ejemplo usa la instalación habitual;
ajustar las rutas al equipo real y comprobar que ese JBR es Java 25:

```powershell
$env:JAVA_HOME = 'C:\Program Files\Android\Android Studio\jbr'
$env:ANDROID_HOME = "$env:LOCALAPPDATA\Android\Sdk"
& "$env:JAVA_HOME\bin\java.exe" -version
.\gradlew.bat --version
```

`JAVA_HOME` permite iniciar el Wrapper. Los criterios versionados del daemon tienen
precedencia para seleccionar la JVM que ejecuta la compilación. La configuración de
Java 11 en `compileOptions` controla el nivel de compilación del código, no la JVM del daemon.

Compilar el APK de depuración y ejecutar las pruebas unitarias locales:

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest --console=plain
```

APK esperado: `app/build/outputs/apk/debug/app-debug.apk`.
Informe de pruebas: `app/build/reports/tests/testDebugUnitTest/index.html`.
Las pruebas locales incluyen el ejemplo y seis casos de catálogo, filtros y favoritos
temporales. No sustituyen las pruebas de navegación y recursos nativos en dispositivo.
El recorrido base se comprueba manualmente siguiendo ESTRUCTURA_MVVM.md.

Cuando exista un dispositivo disponible, ejecutar pruebas instrumentadas:

```powershell
.\gradlew.bat :app:connectedDebugAndroidTest --console=plain
```

Para revisión estática de Android:

```powershell
.\gradlew.bat :app:lintDebug --console=plain
```

En macOS/Linux, usar `./gradlew` y configurar las rutas locales de JDK/SDK según el equipo.

## Qué se comparte

| Versionar | Mantener local |
| --- | --- |
| Código, recursos y documentación | `local.properties`, rutas personales |
| Scripts y JAR del Gradle Wrapper | `.gradle/`, `.kotlin/`, carpetas `build/` |
| Catálogo de versiones y criterios JVM | Configuración personal del IDE |
| Plantillas de Issues y pull requests | Capturas temporales, claves de firma y secretos |

Usar UTF-8. `.gitattributes` define finales de línea para evitar cambios masivos
entre Windows y otros sistemas; no hacer una renormalización global durante una tarea funcional.

## Problemas frecuentes

- **Java no encontrado o incorrecto:** revisar `JAVA_HOME`, el JDK de Android Studio
  y `toolchainVersion=25`. No cambiar el criterio compartido para adaptarlo a un solo PC.
- **SDK location not found:** abrir SDK Manager y sincronizar; revisar el archivo local
  `local.properties` o `ANDROID_HOME`.
- **Platform 37 ausente:** instalarla desde SDK Manager antes de compilar.
- **Dependencia o plugin no resuelto:** comprobar conexión, proxy y modo offline;
  compartir el primer error de Gradle antes de cambiar versiones.
- **Emulador no inicia:** comprobar virtualización y aceleración; usar un teléfono si procede.
- **Acceso denegado a caché:** comprobar permisos y que no haya otro proceso bloqueando
  el archivo. No borrar toda la caché ni ejecutar siempre como administrador.

## Checklist por integrante

Copiar este checklist en el Issue de incorporación de cada persona. Registrar fecha,
versiones, resultado y evidencia; no dar por validados los demás equipos a partir de una sola prueba.

- [ ] Invitación de GitHub aceptada y permisos comprobados.
- [ ] Identidad Git configurada con la cuenta propia.
- [ ] Proyecto clonado y abierto desde su raíz.
- [ ] JDK 25 y SDK 37 disponibles.
- [ ] Sincronización Gradle completada.
- [ ] `assembleDebug` y `testDebugUnitTest` finalizan correctamente.
- [ ] App ejecutada y recorrido base comprobado según ESTRUCTURA_MVVM.md.
- [ ] Rama propia y primer pull request revisado por otro integrante.
- [ ] Nombres, grupo y reparto de tareas completados.

## Referencias

- [Configuración del JDK de Android](https://developer.android.com/build/jdks).
- [JVM del daemon y precedencia de criterios](https://docs.gradle.org/current/userguide/gradle_daemon.html#sec:daemon_jvm_criteria).
- [Administrar paquetes del SDK](https://developer.android.com/studio/intro/update#sdk-manager).
