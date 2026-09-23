**PROMPT DE GEMINI**
Hola, tengo una app en Jetpack Compose con Material 3 y
Navigation Compose llamada NavegacionTec. Necesito mejorar el flujo de
pantallas usando NavHost y NavController para conectar un Login, Inicio, Lista,
Detalle (que recibe un ID tipo Int) y Perfil.
Mis datos de alumno:

Nombre: Eder Yaycate

Carrera: Diseño y Desarrollo de Software
(4to Ciclo)
Correo: eder.yaycate@tecsup.edu.pe

**Estilo visual que quiero:**

1. Tonos morados (oscuro para barras, morado medio y
lavanda claro de fondo) con un degradado vertical suave.

2. Tarjetas flotantes blancas con bordes redondeados y
sombras suaves.

3. Usa solo íconos nativos (Icons.Default) para que
compile rápido sin agregar librerías extra.

**Estructura de archivos y lo que necesito:**

1. Color.kt (ya existe): actualizar los
colores y degradados morados del tema.

2. LoginScreen.kt (nueva pantalla a agregar):
Pantalla de login en una tarjeta flotante con campo de correo, contraseña
(con ojito para ocultar/mostrar), botón "INICIAR SESIÓN" y
enlace de recuperación.

3. HomeScreen.kt (ya existe): Añade Pantalla
principal con saludo "Bienvenido, Eder Yaycate", dos
tarjetas para ir al directorio y perfil, y botón rojo de cerrar sesión
abajo.

4. ListScreen.kt (ya existe): Añade Lista
(LazyColumn) de alumnos con avatares circulares de sus iniciales.

5. DetailScreen.kt (ya existe): Añade Expediente
del alumno seleccionado con avatar, carrera, correo y biografía.

6. ProfileScreen.kt (ya existe): Añade Perfil
personal dividido en "INFORMACIÓN PERSONAL" y "ACADÉMICO"
con mi carrera y ciclo.

7. AppNavigation.kt (ya existe): Añade Manejo
de rutas (login, home, list, detail/{itemId}, profile) conectando la nueva
pantalla de login al inicio del flujo.

Por favor, dame el código actualizado en Jetpack Compose
listo para aplicar estas mejoras a mi proyecto sin errores de compilación.
 