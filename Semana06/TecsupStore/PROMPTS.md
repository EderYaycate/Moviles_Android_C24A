# Prompt de mejora con IA

Estoy haciendo una actividad de Programacion en Moviles en Android Studio con Kotlin, Jetpack Compose y Material3 (paquete com.yaycate.tecsupstore). Ya tengo una app "TECSUP Store" que funciona: una lista de productos con chips de categorias (LazyRow + LazyColumn), un DropdownMenu de 3 puntos en cada tarjeta con Favoritos, Compartir y Reportar, y un NavigationDrawer con navegacion entre Inicio, Mis pedidos, Favoritos y Perfil. El estado de favoritos vive en AppNavegacion y el drawer muestra un badge con la cantidad.

Quiero cambiar solo el diseno visual para que se parezca a la Figura 1 del enunciado, sin tocar la logica:
Barra superior: fondo morado , titulo "TECSUP Store" en negrita y blanco, un subtitulo pequeno "Mas vendidos" debajo, y el icono ☰ en blanco.
Tarjetas de producto: fondo lila claro con esquinas redondeadas de 16 dp. A la izquierda, un cuadro redondeado de 56 dp con el icono de carrito en morado. En el centro, el nombre en negrita, el precio en morado y la categoria en texto pequeno. A la derecha, el icono de 3 puntos con su DropdownMenu, igual que ahora.
Drawer: fondo blanco. Encabezado con un avatar circular lila (#E6D9F5) con las iniciales en negrita y morado, el nombre en negrita y el correo en gris, y una linea divisoria debajo. Los destinos Inicio, Mis pedidos, Favoritos y Perfil, con el item activo resaltado con fondo lila (#EBDDF7) y texto e icono morados, y el badge de Favoritos igual que ahora. Al final, una opcion extra "Cerrar sesion" con icono de salida, que por ahora solo cierra el drawer.

Dame los archivos completos de TarjetaProducto.kt, AppDrawer.kt y AppNavegacion.kt, con todos los imports, listos para pegar y reemplazarlos en la actividad. Usa solo Material3 y los iconos que ya tengo, sin librerias nuevas.
