# Lab03 - Registro de Producto

**Curso:** Diseño y Desarrollo de Software
**Alumno:** Eder Marcelo
**Tecnología:** Jetpack Compose (Kotlin)

## Descripción

Hice una pantalla de registro de productos con Jetpack Compose. Tiene tres
campos (nombre, precio y cantidad) y un botón "Agregar Producto"; al
presionarlo, se muestra una tarjeta con el resumen del producto y el
importe total (precio × cantidad), junto con un mensaje confirmando que
se registró correctamente.

## Capturas

**Pantalla vacía:**

![Captura de pantalla 2026-09-02 184217.png](../../../../../OneDrive/Im%C3%A1genes/Screenshots/Captura%20de%20pantalla%202026-09-02%20184217.png)

**Producto registrado:**

![Captura de pantalla 2026-09-02 185822.png](../../../../../OneDrive/Im%C3%A1genes/Screenshots/Captura%20de%20pantalla%202026-09-02%20185822.png)

## Pregunta de reflexión

**¿Qué pasaría si declaras las variables de estado SIN remember?**

Le quité el remember a la variable nombre y probé escribiendo algo y
girando la pantalla del emulador. El texto se borró. Entendí que sin
remember, Compose crea la variable de cero cada vez que recompone, por
eso el dato se pierde remember es lo que hace que ese valor se
mantenga entre recomposiciones.

## Mejora con IA (rama mejora-ia)

Para esta parte usé IA, como lo pide el laboratorio: le pedí ayuda para
que la pantalla valide campos vacíos y tenga un botón para limpiar todo.

### B1 - Prompt que usé
Tengo una pantalla de registro de producto en Jetpack Compose con campos
Nombre, Precio y Cantidad. Agrega validación para que no se pueda agregar
el producto si algún campo está vacío, mostrando un mensaje de error.
Además agrega un botón 'Limpiar' que reinicie todos los campos.

### Mejora con IA - Prompt y decisiones

| Prompt que usé | Qué generó Gemini | Qué acepté o corregí (y por qué) |
|---|---|---|
| "Agrega validación para que no se pueda agregar el producto si algún campo está vacío, mostrando un mensaje de error. Además agrega un botón 'Limpiar' que reinicie todos los campos." | Estados `mostrarError`/`mensajeError`, validación en el botón Agregar, y un `TextButton` "Limpiar" | Acepté la lógica tal cual porque funcionaba bien; solo corregí el paquete y el tema para que calcen con mi proyecto |
### B3 - Cómo me quedó
Lo probé en el emulador: valida los campos vacíos, registra bien con
datos completos, y Limpiar deja todo en blanco. Funcionó a la primera.