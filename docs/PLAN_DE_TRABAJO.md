# Plan de inicio y reparto

La referencia funcional es [Alcance y desarrollo del MVP](ALCANCE_DEL_PROYECTO.md),
basado en la planificación actualizada de Diseña Madera. Esta tabla
es una propuesta de reparto para Tomás Peña, Marcelo Salas y Fernando Villalobos, que deben
revisar antes de asignar los Issues.
La implementación de C01 a C13 está disponible en el código. Las tablas siguientes
conservan el reparto previsto. La verificación de C14 se registra en PRUEBAS.md;
la entrega, los datos del grupo y las evidencias de los integrantes corresponden a C15.

## Etapa 0 · Entorno común

| ID | Tarea | Responsable propuesto | Criterio de aceptación |
| --- | --- | --- | --- |
| E01 | Revisar e integrar esta documentación | Todos | README y acuerdos revisados mediante PR |
| E02 | Completar datos del equipo | Todos | Tomás Peña, Marcelo Salas y Fernando Villalobos registrados; grupo y nombre visible acordados |
| E03 | Revisar permisos, Issues y Project | Propietario del repo | Tres integrantes con escritura; acceso docente según encargo; tareas visibles |
| E04 | Validar entorno de Tomás Peña | Tomás | Checklist de ENTORNO con evidencia propia |
| E05 | Validar entorno de Marcelo Salas | Marcelo | Checklist de ENTORNO con evidencia propia |
| E06 | Acordar contratos e integración | Todos | Modelo, rutas y responsables de archivos compartidos definidos |
| E07 | Validar entorno de Fernando Villalobos | Fernando | Checklist de ENTORNO con evidencia propia |

Preparado localmente: README, guía de entorno, acuerdos, planificación, plantillas de
GitHub y reglas de archivos. La validación de compilación se registra en
[VALIDACION_ENTORNO.md](VALIDACION_ENTORNO.md); la ejecución en dispositivo se registra
por integrante en su Issue. Cada actualización documental debe revisarse mediante PR.

## Etapa 1 · Primer código, después de validar el entorno

| ID | Tarea | Responsable propuesto | Depende de | Terminado cuando… |
| --- | --- | --- | --- | --- |
| C01 | Modelo Solucion y catálogo local | Marcelo | E04, E05, E06, E07 | Existen 6–8 fichas sintéticas con IDs estables y categorías acordadas |
| C02 | Login: UiState, ViewModel y pantalla | Tomás | E04, E05, E06, E07 | Campos inválidos muestran errores; credenciales demo válidas permiten avanzar |
| C03 | Catálogo: UI, búsqueda y filtros | Marcelo | C01 | Búsqueda y categoría se combinan; se muestra estado vacío |
| C04 | Navegación inicial y conexión de pantallas | Fernando, con revisión de Tomás y Marcelo | C02, C03 | Login → catálogo funciona y el regreso se comporta según lo acordado |
| C05 | Persistir sesión y cerrar sesión | Tomás | C04 | Reiniciar conserva sesión; logout vuelve al login y evita regresar al catálogo con Atrás |

Distribuir trabajo por funcionalidad completa. Los tres integrantes deben participar
en implementación, pruebas y revisiones. Los cambios de navegación y Gradle se coordinan en el Issue.

## Acuerdos previos a trabajar en paralelo

- Mantener por ahora `com.duoc.d_log_mobile` y el módulo `app`.
- Acordar campos y categorías de `Solucion` antes de construir la UI del catálogo.
- Acordar nombres de rutas y callbacks entre login y catálogo.
- Cada tarea crea sus paquetes necesarios; no agregar clases vacías para aparentar MVVM.
- Acordar credenciales demo al implementar login; documentarlas entonces y no usar datos reales.
- Agregar solo dependencias necesarias para el incremento y avisar al resto del equipo.

## Etapas posteriores y responsables propuestos

| ID | Tarea | Responsable | Depende de | Criterio de aceptación |
| --- | --- | --- | --- | --- |
| C06 | Detalle y recursos relacionados | Marcelo | C04 | La ficha correcta abre por ID y muestra un recurso de prueba; ID inválido permite volver |
| C07 | Preferencias y favoritos | Tomás | C05, C06 | Se agregan y quitan favoritos desde detalle y se conservan tras reiniciar |
| C08 | Listado de favoritos y navegación | Fernando | C07 | Lista reactiva, estado vacío y acceso a detalle funcionan |
| C09 | Consulta validada | Fernando | C04 | Los campos inválidos muestran errores desde ViewModel y bloquean Continuar |
| C10 | Resumen y edición de consulta | Marcelo | C09 | Comparte datos del formulario y permite regresar para editar |
| C11 | Cámara, galería y vista previa | Tomás | C09, C10 | Ambos recursos muestran imagen; cancelar conserva estado válido |
| C12 | Adaptación de pantallas | Marcelo | C06, C08, C10 | Se comprueban tamaño compacto y otra configuración, incluido teclado |
| C13 | Animaciones y estados visuales | Fernando | C07, C10 | Acciones y mensajes tienen feedback coherente sin bloquear la UI |
| C14 | Pruebas cruzadas y correcciones | Todos | C11, C12, C13 | RF01–RF12 verificados con resultados y dispositivo/API registrados |
| C15 | Documentación y entrega | Todos | C14 | README, cuenta demo, evidencias y entrega revisados antes del cierre |

El responsable implementa y registra pruebas; otro integrante revisa su PR. Estos
responsables deben confirmarse antes de asignar los Issues.

## Cronograma de referencia

| Semana | Objetivo | Entregable |
| --- | --- | --- |
| 1 | Entorno, acuerdos y primer flujo | Incorporación de los tres, login y base de catálogo |
| 2 | Exploración y datos locales | Búsqueda, categorías, detalle, sesión y favoritos |
| 3 | Formularios y navegación | Consulta validada, resumen y edición |
| 4 | Recursos y experiencia visual | Cámara, galería, adaptación y animaciones |
| 5 | Calidad y entrega | Pruebas cruzadas, correcciones, documentación y evidencias |

Las semanas son una referencia de secuencia, no fechas comprometidas. Ajustar según
el calendario del docente y la disponibilidad del equipo; validar cada incremento
antes de integrar el siguiente.

Crear un Issue por tarea con responsable, alcance, criterios, dependencias y evidencia.
Vincularlo al PR y moverlo por Por hacer → En curso → En revisión → Finalizado.
La fecha límite exacta debe registrarse desde las indicaciones del docente; no se ha
inferido una fecha a partir del cronograma de referencia.

## Estado de implementación

| Tareas | Estado |
| --- | --- |
| C01, C03, C06 | Catálogo, filtros y detalle implementados |
| C02, C04, C05 | Login demo, navegación y sesión persistente implementados |
| C07, C08 | Favoritos persistentes y listado conectados |
| C09, C10 | Consulta validada, resumen y edición conectados |
| C11 | Cámara, galería, vista previa y cancelación implementadas |
| C12 | Window Size Classes, ancho máximo, scroll y teclado |
| C13 | Color animado de favoritos, mensajes e indicadores de carga |
| C14 | Pruebas locales y del emulador aprobadas; ejecución manual confirmada en POCO X5 Pro con HyperOS; completar registro por función |
| C15 | Documentación actualizada; completar número de grupo y evidencias del equipo |

Ver [resultados y recorrido de prueba](PRUEBAS.md).
