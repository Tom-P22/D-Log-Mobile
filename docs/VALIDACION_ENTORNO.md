# Registro de validación inicial

Fecha: 20 de octubre de 2026. Verificación desde el entorno de ejecución de Codex
en Windows; no sustituye la incorporación de cada integrante en Android Studio.

## Comprobado

- El repositorio está en `main` y tiene remoto `Tom-P22/D-Log-Mobile`.
- El Gradle Wrapper, incluido su JAR, está versionado.
- `gradlew.bat --version` ejecuta Gradle 9.6.0.
- La JVM de inicio comprobada es JetBrains OpenJDK 25.0.3; el criterio del daemon pide Java 25.
- Se detectó el SDK local con una plataforma `android-37.0` instalada.
- Se revisaron exclusiones de Git para SDK local, cachés, builds, secretos y claves de firma.
- Se comprobaron enlaces Markdown locales y espacios de los cambios con `git diff --check`.

## Compilación y pruebas pendientes

Se intentó ejecutar:

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest --console=plain
```

El primer intento restringido no pudo abrir el archivo de bloqueo de la caché de
Gradle. Al ejecutar con permiso para esa caché, el Wrapper funcionó, pero la
comunicación local del daemon falló:

```text
java.io.IOException: Unable to establish loopback connection
Caused by: java.net.SocketException: Invalid argument: connect
```

Se repitió con `--no-daemon --stacktrace`; Gradle necesitó un daemon de un solo uso
y se reprodujo el mismo fallo en `UnixDomainSockets.connect` / `PipeImpl`.
El diagnóstico apunta a la comunicación local del proceso Java en este entorno;
no establece un error del código de la aplicación ni confirma su compilación.

No se obtuvo un APK validado ni resultados de pruebas. Tampoco se ejecutó la app
en emulador o teléfono ni se comprobaron individualmente los entornos de Tomás Peña,
Marcelo Salas y Fernando Villalobos.
No se modificaron las versiones del proyecto ni la configuración global de Windows.

## Siguiente comprobación

Los tres integrantes deben seguir [ENTORNO.md](ENTORNO.md), sincronizar y ejecutar el
proyecto desde Android Studio. Registrar resultado y evidencia en sus respectivos Issues.
Si el fallo se reproduce fuera del entorno de Codex, revisar el JDK elegido y el log
del daemon antes de cambiar Gradle, SDK o reglas de red del equipo.
