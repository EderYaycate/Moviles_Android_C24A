# CafeCampus

Es una app Android para pedir en la cafetería del campus sin hacer cola. El estudiante arma su pedido desde el celular, elige la hora de recojo y puede seguir su pedido hasta que esté listo. Del lado de la cafetería, el encargado ve los pedidos nuevos, los pasa a preparando y los marca como listos o entregados.

Lo hice para el laboratorio de Programación en Móviles (Semana 08), usando MVVM y Room.

## Qué usé

- Kotlin y Jetpack Compose con Material 3
- Room 2.7.2 con KSP para guardar productos, pedidos y usuarios
- DataStore para recordar la sesión, el modo oscuro y algunas opciones de la cafetería
- Navigation Compose para moverme entre pantallas
- Un tema distinto por rol: naranja para el estudiante y morado para el administrador

## Cómo está organizado

```
app/src/main/java/com/yaycate/cafecampus/
├── data/        base de datos Room, DataStore y el repositorio
├── model/       Rol, EstadoPedido e ItemCarrito
├── ui/          pantallas del cliente y del admin, componentes, navegación y tema
└── viewmodel/   los ViewModels y su factory
```

## Cuentas para probar

| Rol           | Correo           | Contraseña                         |
|---------------|------------------|------------------------------------|
| Estudiante    | juan@campus.test | la que está en `DatosIniciales.kt` |
| Administrador | admin@cafe.test  | la que está en `DatosIniciales.kt` |

## Cómo se usa

1. Entro como estudiante, reviso el menú y abro un producto para elegir cantidad y, si quiero, una nota para la cocina.
2. Confirmo el pedido con hora de recojo y forma de pago.
3. Sigo el pedido en Seguimiento o en Mis pedidos, donde separo los activos del historial.
4. Como administrador, el pedido aparece en Nuevos. Lo paso a Preparando, después a Listo y finalmente a Entregado. También puedo cancelarlo.
5. En el panel veo las ventas del día, cuántos pedidos hay en cada estado y los pedidos por hora.

## Cosas que cambié o no llegué a hacer

- Usé Room 2.7.2 porque con la 2.6.1 me salía un error de KSP en el proyecto.
- La sesión se queda guardada: si cierro la app, al abrirla de nuevo sigo con mi cuenta.
- En el celular del estudiante, la pestaña Perfil abre Ajustes.
- "Cambiar de perfil" cierra la sesión y me manda al login. No lo hice cambiar de rol directo porque cualquiera podría entrar como admin sin contraseña.
- El switch de Notificaciones guarda la preferencia, pero todavía no oculta el contador de la campana.
- El tiempo de preparación es fijo (10 – 15 min) para todos los productos, porque el modelo no tiene un tiempo por producto.
- La hora de recojo y el pago tienen opciones fijas.
- El saludo dice solo "¡Hola!", porque no guardo el nombre al iniciar sesión.
- El cargo por servicio sale en S/ 0.00. Lo puse en el resumen pero no lo cobro.

## Cómo correrlo

Abrir `Semana08/CafeCampus` en Android Studio, esperar a que sincronice Gradle y correr la app en un emulador o celular con Android 8.0 o superior.

## Capturas

Captura de Usuario
![img.png](img.png)
![img_2.png](img_2.png)
![img_3.png](img_3.png)
![img_4.png](img_4.png)
![img_5.png](img_5.png)


Captura de Admin
![img_1.png](img_1.png)
![img_6.png](img_6.png)
![img_7.png](img_7.png)
![img_8.png](img_8.png)
![img_9.png](img_9.png)

## PROMPT

Para la Fase 2 del laboratorio usé el agente de Gemini en Android Studio. Le di un solo prompt con todas las mejoras que quería, y lo fui revisando por partes antes de hacer cada commit.

Estoy trabajando en la app CafeCampus con MVVM y Room, y quiero mejorarla en varios puntos a la vez. En la pantalla de Menú quiero una barra de búsqueda que filtre los productos por nombre mientras escribo, sin importar mayúsculas, y además quiero ordenarlos por precio, de menor a mayor o de mayor a menor, y una sección de favoritos donde cada producto tenga un corazón para marcarlo. En Mis pedidos, en la pestaña Historial, quiero un botón “Repetir pedido” que vuelva a cargar los productos al carrito con la cantidad original y el precio actual; si algún producto ya no está disponible, que me avise cuáles no se pudieron agregar. Al pagar con Yape o Plin quiero un recargo fijo de S/ 1.00 que se sume al total y se muestre como una fila aparte en la confirmación, y que ese total sea el mismo que se guarda. En el Panel del administrador quiero un gráfico de barras con los pedidos por hora usando datos reales de la base de datos; si no hay pedidos, que muestre de 8:00 a 13:00 en cero, que se pueda desplazar si hay muchas horas, y que las barras se coloreen según el ranking de ventas: verde para la más vendida, rojo para la menos vendida y gris para las que no tienen ventas. Corrige también las flechas de regreso de las pestañas inferiores, que no estaban funcionando. Para el estilo, quiero colores desvanecidos con degradados en Material 3, fotos de productos en el menú, en el carrito, en Mis pedidos y en el panel del administrador, imágenes de pago para pago en caja, Yape y Plin, y un perfil con foto. En el perfil del cliente quiero que aparezca “Eder Yaycate” y quitar la opción “Recibir pedidos”; en el del administrador, “Administrador” con su foto. Y en el panel del administrador, que aparezcan las iniciales del cliente y las miniaturas de los productos. Por último, agrega más productos en Bebidas, Comidas, Panadería y Snacks en DatosIniciales.kt. Respeta la arquitectura: la vista no debe usar el DAO ni el Repository, y el ViewModel usa la interfaz CafeRepository. Antes de aplicar cambios, muéstrame los archivos que vas a modificar.

