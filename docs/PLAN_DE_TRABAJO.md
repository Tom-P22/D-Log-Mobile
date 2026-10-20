# Plan de inicio y reparto

La referencia funcional es la planificación actualizada de Diseña Madera. Esta tabla
es una propuesta de reparto para Tomás Peña, Marcelo Salas y Fernando Villalobos, que deben
revisar antes de asignar los Issues.
Todas las tareas están pendientes salvo la preparación documental local descrita abajo.
Las plantillas del repositorio no crean Issues ni configuran GitHub automáticamente.

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
por integrante en su Issue. Este trabajo aún debe revisarse e integrarse al repositorio remoto.

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

## Etapas posteriores

1. Detalle y recursos relacionados.
2. Favoritos persistentes.
3. Consulta validada y resumen con estado compartido.
4. Cámara y galería, con vista previa y cancelaciones.
5. Adaptabilidad, animaciones, pruebas y entrega.

Crear un Issue por tarea con responsable, alcance, criterios, dependencias y evidencia.
Vincularlo al PR y moverlo por Por hacer → En curso → En revisión → Finalizado.
La fecha límite exacta debe registrarse desde las indicaciones del docente; no se ha
inferido una fecha a partir del cronograma de referencia.
