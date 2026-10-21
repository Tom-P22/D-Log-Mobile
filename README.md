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

Base con Kotlin, Jetpack Compose, Material 3 y estructura MVVM. Las seis pantallas
están conectadas mediante Navigation Compose, con seis fichas sintéticas, búsqueda
por texto y filtros de categoría, detalle, favoritos temporales y estado compartido
entre Consulta y Resumen. La entrada es un recorrido
de prueba, sin autenticación ni validaciones; no hay credenciales demo todavía.
El 21 de octubre de 2026 se verificaron compilación, siete pruebas unitarias y lint
desde terminal. La ejecución del flujo integrado en dispositivo sigue pendiente.
Consultar [la estructura y los pasos de prueba](docs/ESTRUCTURA_MVVM.md).

## Alcance acordado

- Login local simulado, validaciones y persistencia de sesión.
- Catálogo local con 6 a 8 soluciones sintéticas, búsqueda y categorías.
- Detalle con información y recursos relacionados de prueba.
- Favoritos persistentes.
- Formulario de consulta validado y pantalla de resumen.
- Fotografía desde cámara o galería, con vista previa y manejo de cancelación.
- Adaptabilidad y animaciones que entreguen retroalimentación.

El catálogo, la búsqueda, los filtros y el detalle ya están implementados;
el resto mantiene las limitaciones descritas en el estado actual.
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
5. Confirmar que aparece «Diseña Madera» con el botón «Abrir catálogo de prueba»
   y seguir el recorrido descrito en [ESTRUCTURA_MVVM.md](docs/ESTRUCTURA_MVVM.md).

Se requiere internet para la primera descarga de herramientas y dependencias.
Usar siempre el Wrapper del repositorio; no instalar Gradle globalmente.

El desarrollo se integra en `dev`; `main` recibirá la versión completa y verificada.

## Organización y colaboración

- [Qué construiremos y cómo lo desarrollaremos](docs/ALCANCE_DEL_PROYECTO.md).
- [Instalación, comandos y solución de problemas](docs/ENTORNO.md).
- [Ramas, commits, revisiones y acuerdos de trabajo](CONTRIBUTING.md).
- [Tareas iniciales, responsables y dependencias](docs/PLAN_DE_TRABAJO.md).
- [Archivos MVVM, conexión de vistas y pruebas del recorrido](docs/ESTRUCTURA_MVVM.md).
- [Descripción de cada archivo y sus funciones](docs/GUIA_DE_ARCHIVOS.md).

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

La estructura inicial ya existe. Las pantallas muestran estado y emiten acciones;
los ViewModels procesan cambios y el repositorio entrega los datos de prueba.
Cada tarea ampliará esta base con las reglas del MVP y persistencia.

## Validación y entrega

Los comandos de compilación y pruebas están en [ENTORNO.md](docs/ENTORNO.md).
La [validación inicial](docs/VALIDACION_ENTORNO.md) conserva el bloqueo histórico
del daemon. La [integración del catálogo](docs/INTEGRACION_CATALOGO.md) registra
la compilación y las pruebas exitosas del 21 de octubre de 2026.
Las pruebas incluyen seis casos del catálogo y favoritos, además del ejemplo inicial.
El recorrido visual debe comprobarse manualmente siguiendo ESTRUCTURA_MVVM.md.
Cada funcionalidad deberá registrar sus pasos de prueba en su pull request.

Antes de entregar: actualizar funcionalidades disponibles, usuario demo y evidencias;
comprobar acceso del docente y visibilidad exigida del repositorio; entregar el código
en AVA grupal y completar la activación individual. Respetar la fecha de cierre y
la restricción de cambios posteriores indicada en el encargo.
