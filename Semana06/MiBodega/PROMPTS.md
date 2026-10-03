Estoy desarrollando la aplicación "Mi Bodega - App Cliente" en Android Studio utilizando Kotlin, Jetpack Compose y Material3 (paquete com.yaycate.mibodega). Necesito que me ayudes a implementar y corregir el flujo completo de compra y las mejoras de la app según las siguientes especificaciones:

Inicio y Buscador (Mejora Obligatoria):
En InicioScreen.kt, implementa un campo de búsqueda (OutlinedTextField) que filtre la lista de productos en tiempo real mientras el usuario escribe.
El buscador debe combinarse con los chips de categorías (Todos, Bebidas, Abarrotes, Snacks). Ambos filtros deben funcionar juntos. Si no hay resultados, debe mostrar el mensaje "No se encontraron productos".
La barra superior debe tener el título "Mi Bodega" (con "Bodega" en color verde) y un ícono de carrito con contador que al hacer clic lleve a la pantalla del carrito (onIrAlCarrito).
La lista debe ser una cuadrícula de 2 columnas ("Productos destacados") mostrando imagen, nombre, presentación, precio en verde y un botón + para agregar al carrito.

Navegación y Flujo de Pantallas (Figura 2):
Configura en ClienteApp.kt el NavHost con las 7 pantallas: Bienvenida, Registro, Inicio, Detalle del producto (con parámetro productoId), Carrito, Datos de Entrega y Confirmación.
Añade una barra inferior (NavigationBar) con los 4 destinos principales (Inicio, Categorías, Pedidos y Perfil).
En el Carrito, calcula de forma dinámica el subtotal, el costo de delivery y el total al incrementar, decrementar o eliminar productos.
En Datos de Entrega, incluye campos para nombre, teléfono, dirección, referencia y selección de método de pago (Efectivo, Yape, Plin).
En Confirmación, usa popUpTo para limpiar el historial y evitar que el botón atrás regrese al carrito.

Pantalla de Perfil:
Crea PerfilScreen.kt con avatar de iniciales, datos editables del usuario (nombre, teléfono, dirección, referencia), contador de ítems en carrito y botón de "Cerrar sesión" que limpie los datos y vuelva a Bienvenida.

Modelos y Datos Fake:
Asegúrate de que el modelo Producto coincida con los campos (id, nombre, precio, presentacion, categoria, imagen) y que las imágenes en DatosFake.kt usen los identificadores correctos de R.drawable (img_1, img_2, img_3, img) asignando la foto correspondiente a cada producto.

Por favor, dame los archivos completos y listos para usar en el proyecto, utilizando únicamente componentes de Material3 e íconos nativos.
