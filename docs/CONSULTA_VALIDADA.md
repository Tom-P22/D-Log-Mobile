# C09 — Consulta validada

## Archivos y responsabilidades

| Archivo | Responsabilidad |
| --- | --- |
| model/ConsultaUiState.kt | Guarda los cuatro campos y sus errores |
| model/ConsultaErrores.kt | Un mensaje de error opcional por campo |
| viewmodel/ConsultaViewModel.kt | Actualiza datos, limpia errores y valida el formulario |
| ui/screens/ConsultaScreen.kt | Dibuja campos, mensajes e iconos; no valida |
| navigation/AppNavigation.kt | Valida antes de navegar; comparte el ViewModel con Resumen |
| res/drawable/ic_error.xml | Icono local reutilizado cuando hay error, sin librerías extra |
| ConsultaViewModelTest.kt | Ocho pruebas locales de las reglas del formulario |

Se conserva la estructura del proyecto y no se agregan dependencias.

## Reglas

- Nombre, correo, categoría y descripción son obligatorios; solo espacios no cuentan.
- El correo tiene una comprobación básica de texto@dominio.extensión, sin espacios
  interiores ni más de un @. Se ignoran espacios de los extremos al validarlo.
- Descripción: mínimo diez caracteres, sin contar espacios de los extremos.
- La categoría se escribe como texto libre, con ejemplos del catálogo; solo se exige que no esté vacía.
- El botón Ver resumen permanece disponible para poder mostrar todos los errores
  al pulsarlo. Si hay errores, la navegación se bloquea.
- Al editar se limpia solo el error de ese campo. Al pulsar de nuevo se vuelve a validar.
- Editar desde Resumen conserva los campos. Finalizar o abrir una consulta nueva los limpia.

## Verificaciones ejecutadas

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:lintDebug --console=plain
```

Resultado: BUILD SUCCESSFUL; quince pruebas sin fallos, incluidas ocho de consulta;
lint con cero errores y once advertencias. Se generó el APK de depuración.
No se ejecutó la pantalla en emulador ni teléfono durante esta implementación.

## Cómo probar en Android Studio

1. Ejecutar app → Abrir catálogo de prueba → Nueva consulta.
2. Pulsar Ver resumen sin escribir: debe seguir en Consulta, con cuatro errores e iconos.
3. Probar campos con solo espacios, correo sin @ y descripción de nueve caracteres.
4. Corregir el nombre y comprobar que solo desaparece el error de ese campo.
5. Completar: Ana Pérez, ana@correo.cl, Muros, «Necesito información sobre un muro».
6. Pulsar Ver resumen: comprobar los cuatro datos.
7. Pulsar Editar consulta: comprobar que se conservan; borrar un dato y verificar
   que no permite avanzar al resumen hasta corregirlo.
8. Finalizar y abrir Nueva consulta: comprobar campos y errores vacíos.
9. Revisar pantalla pequeña, scroll y teclado: se debe poder alcanzar Ver resumen y Volver.

La fotografía y las animaciones quedan para sus tareas correspondientes.
