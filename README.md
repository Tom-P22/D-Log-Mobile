# D-Log-Mobile · Diseña Madera

Proyecto académico de Desarrollo de Aplicaciones Móviles (DSY1105), Unidad 2.
El MVP permitirá explorar soluciones constructivas en madera desde Android.

El problema, los usuarios, los criterios de aceptación y el enfoque de implementación
se detallan en [Alcance y desarrollo del MVP](docs/ALCANCE_DEL_PROYECTO.md).

## Equipo

| Dato | Valor |
| --- | --- |
| Integrante 1 | Tomás Peña |
| Integrante 2 | Marcelo Salas |
| Integrante 3 | Fernando Villalobos |
| Número de grupo | Pendiente de completar |
| Repositorio | [Tom-P22/D-Log-Mobile](https://github.com/Tom-P22/D-Log-Mobile) |

`D-Log-Mobile` es el nombre técnico actual del proyecto. El contexto funcional es
Diseña Madera. El nombre visible definitivo se acordará antes de personalizar la UI.

## Estado actual

Base generada con Kotlin, Jetpack Compose y Material 3. La pantalla muestra
`Hello Android!`; el tema y las pruebas son los ejemplos iniciales de Android Studio.
Las funcionalidades del MVP y las capas MVVM todavía no están implementadas.
No hay credenciales de demostración disponibles aún.

## Alcance acordado

- Login local simulado, validaciones y persistencia de sesión.
- Catálogo local con 6 a 8 soluciones sintéticas, búsqueda y categorías.
- Detalle con información y recursos relacionados de prueba.
- Favoritos persistentes.
- Formulario de consulta validado y pantalla de resumen.
- Fotografía desde cámara o galería, con vista previa y manejo de cancelación.
- Adaptabilidad y animaciones que entreguen retroalimentación.

Estas son funcionalidades planificadas, no una lista de funciones disponibles.
No se contempla backend, Firebase, autenticación remota, catálogo productivo,
integración BIM ni publicación en tiendas. Las fichas de prueba no representan
especificaciones técnicas oficiales. El login será una simulación académica.

## Entorno del proyecto

| Componente | Configuración versionada |
| --- | --- |
| Android Gradle Plugin | 9.4.1 |
| Gradle Wrapper | 9.6.0 |
| JVM del daemon de Gradle | Java 25 |
| Plugin Kotlin Compose | 2.2.10 |
| Compose BOM | 2026.02.01 |
| SDK de compilación / destino | 37 |
| SDK mínimo | 24 (Android 7.0) |
| Identificador de aplicación | `com.duoc.d_log_mobile` |

Las fuentes de verdad son `gradle/libs.versions.toml`,
`gradle/wrapper/gradle-wrapper.properties`, `gradle/gradle-daemon-jvm.properties`
y `app/build.gradle.kts`. El nivel Java 11 de compilación del código no sustituye
la JVM 25 configurada para ejecutar Gradle.

## Primer inicio

1. Instalar Git y Android Studio compatible con las versiones del proyecto.
2. Clonar el repositorio:

   ```powershell
   git clone https://github.com/Tom-P22/D-Log-Mobile.git
   cd D-Log-Mobile
   ```

3. Abrir esa carpeta desde Android Studio y seguir [la guía de entorno](docs/ENTORNO.md).
4. Sincronizar Gradle, seleccionar un emulador o teléfono y ejecutar `app`.
5. Confirmar que aparece `Hello Android!` y completar el checklist de incorporación.

Se requiere internet para la primera descarga de herramientas y dependencias.
Usar siempre el Wrapper del repositorio; no instalar Gradle globalmente.

## Organización y colaboración

- [Qué construiremos y cómo lo desarrollaremos](docs/ALCANCE_DEL_PROYECTO.md).
- [Instalación, comandos y solución de problemas](docs/ENTORNO.md).
- [Ramas, commits, revisiones y acuerdos de trabajo](CONTRIBUTING.md).
- [Tareas iniciales, responsables y dependencias](docs/PLAN_DE_TRABAJO.md).

La planificación se llevará en GitHub Issues/Projects, conforme al acuerdo
registrado en el plan actualizado de trabajar sin Trello. Conservar la confirmación
del docente y evidencias de tareas y aportes de los tres integrantes.

## Arquitectura prevista

Un solo módulo Android `app`, con paquetes bajo `com.duoc.d_log_mobile`:

```text
model/          Datos y estados de pantalla
repository/     Catálogo local y preferencias persistentes
viewmodel/      Lógica de presentación y validaciones
navigation/     Rutas y navegación
ui/screens/     Pantallas Compose
ui/components/  Componentes reutilizables
ui/theme/       Tema existente
```

Esta estructura se creará al implementar cada tarea. Las pantallas mostrarán
estado y emitirán acciones; los ViewModels procesarán la lógica y los repositorios
administrarán los datos. No se añadirán capas o dependencias sin una necesidad concreta.

## Validación y entrega

Los comandos de compilación y pruebas están en [ENTORNO.md](docs/ENTORNO.md).
La [validación inicial](docs/VALIDACION_ENTORNO.md) confirmó Gradle y Java, pero la
compilación quedó bloqueada por la conexión local del daemon; sigue pendiente
confirmar compilación y ejecución en Android Studio en los equipos de los tres integrantes.
Las pruebas actuales son ejemplos y no validan reglas del MVP.
Cada funcionalidad deberá registrar sus pasos de prueba en su pull request.

Antes de entregar: actualizar funcionalidades disponibles, usuario demo y evidencias;
comprobar acceso del docente y visibilidad exigida del repositorio; entregar el código
en AVA grupal y completar la activación individual. Respetar la fecha de cierre y
la restricción de cambios posteriores indicada en el encargo.
