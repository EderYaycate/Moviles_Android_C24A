Hola, tengo una app Android en Kotlin con Jetpack Compose y Navigation Compose llamada "Clínica Salud+" (sin ViewModel/MVVM). La app ya tiene su flujo principal de reserva de citas médicas y menú lateral (ModalNavigationDrawer) funcionando en la rama main. Ahora me encuentro trabajando en la rama "mejora-ia" y necesito implementar la función de cancelar una cita desde la pantalla "Mis Citas" mediante un AlertDialog de confirmación.

Estilo visual y comportamientos requeridos:
. Tarjetas con elevación suave (Card) para cada cita.
. Botón de eliminación en color de error (MaterialTheme.colorScheme.error) que solo aparezca en las citas con estado "Confirmada".
. Diálogo flotante AlertDialog con fondo y botones limpios de Material 3, pidiendo confirmación al usuario antes de borrar.
. Manejo de estado 100% en Compose usando remember { mutableStateOf(...) } sin arquitectura MVVM.

Archivos a modificar o generar:
° MisCitasScreen.kt:
- Añadir el parámetro de callback onCancelarCita: (Cita) -> Unit.
- Agregar un IconButton con Icons.Filled.Delete en la tarjeta de las citas confirmadas.
- Crear el estado local var citaAEliminar by remember { mutableStateOf<Cita?>(null) }.
- Mostrar un AlertDialog cuando citaAEliminar != null con título "Cancelar cita", mensaje con el doctor/fecha/hora, botón para confirmar la acción y botón para cancelar y cerrar el diálogo.
  ° MainActivity.kt:
- En la ruta Screen.MisCitas.route del NavHost, pasar el nuevo parámetro onCancelarCita = { cita -> misCitas.remove(cita) } para eliminar la cita de la lista global mutable.
  ° PROMPTS.md:
- Crear este archivo en la raíz para documentar la solicitud hecha a la IA y los ajustes que debieron corregirse manualmente en el código.

Por favor, dame el código limpio en Jetpack Compose para implementar esta mejora cumpliendo con la estructura requerida.

## Requerimientos Funcionales de la Mejora (Rama mejora-ia)

1. Parámetro Callback de Cancelación: Agregué el parámetro onCancelarCita en la pantalla MisCitasScreen para poder notificar a la actividad principal cada vez que el usuario decida eliminar una cita.

2. Acción de Cancelación en UI (IconButton): Coloqué un botón con el ícono de basurero en la tarjeta de la cita, configurándolo para que únicamente aparezca cuando el estado sea Confirmada.

3. Diálogo de Confirmación (AlertDialog): Manejé un estado local con citaAEliminar para mostrar un AlertDialog que le confirme al usuario la acción, mostrándole el nombre del doctor, la fecha y la hora antes de borrarla.

4. Actualización de Estado Global en NavHost: En MainActivity.kt, dentro de la ruta de Mis Citas, conecté el callback para ejecutar la eliminación en la lista y así refrescar la lista global de la aplicación.