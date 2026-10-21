# Acuerdos de colaboración

## Antes de comenzar

1. Completar la instalación de [ENTORNO.md](docs/ENTORNO.md).
2. Configurar la identidad de Git con los datos de la propia cuenta:

   ```powershell
   git config user.name "Nombre Apellido"
   git config user.email "correo-verificado-o-noreply-de-GitHub"
   ```

3. Comprobar que Tomás Peña, Marcelo Salas y Fernando Villalobos pueden acceder al
   repositorio con permisos de escritura. El propietario gestiona las invitaciones en GitHub.
4. Elegir una tarea de [PLAN_DE_TRABAJO.md](docs/PLAN_DE_TRABAJO.md), crear su Issue
   y asignarse como responsable antes de modificar archivos.

Cada integrante usa su cuenta y realiza sus propios commits. No compartir credenciales.

## Flujo por tarea

Partir de una copia actualizada de `dev`, con el árbol de trabajo limpio:

```powershell
git status
git switch dev
git pull --ff-only origin dev
git switch -c feature/entorno
```

Usar una rama por tarea: `feature/entorno`, `feature/login`, `feature/catalogo`
o `feature/navegacion`. Para correcciones, usar `fix/descripcion`; para documentación,
`docs/descripcion`.

Durante el desarrollo:

1. Limitar los cambios al alcance del Issue y acordar cualquier archivo compartido.
2. Compilar y probar lo afectado antes de pedir revisión.
3. Revisar `git diff` y agregar archivos concretos, evitando incluir datos locales.
4. Hacer commits pequeños y descriptivos; por ejemplo:

   ```powershell
   git add README.md docs/ENTORNO.md
   git commit -m "docs: documentar instalación del entorno"
   git push -u origin feature/entorno
   ```

   Adaptar archivos, mensaje y rama a la tarea real. Este ejemplo corresponde
   a una tarea de documentación del entorno.

5. Abrir un pull request hacia `dev`, enlazar el Issue con `Closes #numero` y
   completar la plantilla. Otro integrante revisa y prueba el cambio.
6. Integrar después de resolver observaciones y comprobar que compila.
7. Actualizar el Issue y la planificación; cada integrante actualiza su `dev` antes de otra tarea.

No cambiar de rama con trabajo pendiente ni usar comandos destructivos para resolver
conflictos. Si dos tareas necesitan el mismo archivo, acordar quién lo modifica primero.
Para sincronizar una rama compartida, se puede integrar `origin/dev` con un merge;
evitar reescribir el historial que ya usan otros integrantes.

Las funcionalidades se integran y prueban en `dev`. Cuando el equipo tenga una
version completa y verificada, se abre un PR de `dev` hacia `main` para la entrega.

## Archivos compartidos

Avisar en el Issue antes de cambiar `MainActivity.kt`, navegación, tema o archivos Gradle.
Quien tenga la tarea de integración coordina esos cambios para evitar conflictos.
Agregar dependencias mediante `gradle/libs.versions.toml` y su uso en el módulo `app`.
No aceptar actualizaciones automáticas de versiones sin acordarlas y probarlas en los equipos del grupo.

## Criterio de terminado

- Cumple los criterios del Issue y mantiene separación MVVM.
- Compila y registra las verificaciones realizadas y sus límites.
- Incluye pruebas de reglas cuando corresponda y evidencias visuales de cambios de UI.
- No contiene rutas personales, credenciales ni archivos generados.
- Al menos otro integrante revisó el pull request.
- Documentación y tarea quedan actualizadas.

La base de pantallas y contratos está documentada en [ESTRUCTURA_MVVM.md](docs/ESTRUCTURA_MVVM.md).
Desarrollar las reglas del MVP dentro de su Issue y coordinar los cambios a esos contratos.

## Configuración sugerida en GitHub

El propietario debe revisar estas opciones; este documento no las configura:

- Acceso de escritura para los tres integrantes y acceso del docente según el encargo.
- Issues habilitados y un Project con Por hacer, En curso, En revisión y Finalizado.
- Protección de `main` mediante pull requests y una revisión, si el plan de GitHub lo permite.
- No exigir comprobaciones automáticas inexistentes: aún no hay un workflow de CI.
- Registrar la fecha límite de entrega y conservar las evidencias antes del cierre.
