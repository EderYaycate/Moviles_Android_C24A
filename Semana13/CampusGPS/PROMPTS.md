
## PROMPTS
# Mejoras que eleji del documento 
#### 1 Implementar filtro por categoría con chips en mapa Jetpack Compose.
Tengo un proyecto Android con Kotlin y Jetpack Compose, arquitectura MVVM + Repository.
- MapaViewModel expone StateFlow: estado (UbicacionUi), lugares (List<LugarConDistancia>) y seleccionado (Int?).
- LugaresRepositoryLocal devuelve 5 lugares con categoria: "Estudio", "Comida", "Eventos" o "Servicios".
- La UI está en PantallaMapa, MapaUbicacion (GoogleMap) y ListaLugares (LazyColumn).

Quiero agregar un filtro por categoría con chips (Estudio, Comida, Eventos, Servicios) que muestre solo los lugares de esa categoría en el mapa y en la lista.

Reglas: el ViewModel no puede tener Context ni lógica de UI; el Composable no debe calcular distancias ni filtrar datos por su cuenta. Dame solo los cambios necesarios, indicando en qué archivo va cada uno y qué capa cambia.


#### 2   Guardar el último lugar visitado con DataStore Preferences, con repositorio (interfaz e implementación) y restaurar la selección al abrir la app. |
Proyecto Android con Kotlin, Jetpack Compose y MVVM + Repository.
- MapaViewModel expone StateFlow: estado, lugares, seleccionado (Int?) y masCercano.
- CampusViewModelFactory crea el ViewModel con el Context de la aplicación.
- La UI selecciona lugares en PantallaMapa mediante vm.seleccionar(id).

Quiero recordar el último lugar seleccionado con DataStore Preferences y restaurarlo al abrir la app.

Reglas:
- Crea una interfaz UltimoLugarRepository en el paquete data, con guardarUltimo(id: Int) y un Flow<Int?> llamado ultimoLugar.
- La implementación con DataStore va en data y recibe el Context en su constructor, no el ViewModel.
- El ViewModel recibe la interfaz por constructor, nunca un Context. Al seleccionar un lugar guarda el id, y al iniciar restaura seleccionado con el último valor guardado.
- La UI no debe guardar ni leer preferencias.
- Indica en qué archivo va cada cambio y en qué capa está, y dame solo lo necesario.


#### 3   Estilo oscuro del mapa con MapStyleOptions según el tema de la app, solo en la capa View (MapaUbicacion.kt), con el JSON en res/raw/mapa_oscuro.json.
Proyecto Android con Kotlin, Jetpack Compose y maps-compose 8.4.0.
En MapaUbicacion.kt tengo un GoogleMap con cameraPositionState y marcadores.

Quiero que el mapa use un estilo oscuro cuando el tema del sistema sea oscuro, y el estilo normal cuando sea claro.

Reglas:
- Solo cambia la capa View (MapaUbicacion.kt). No toques el ViewModel ni el Repository.
- Usa isSystemInDarkTheme() y LocalContext.current dentro del Composable.
- Pasa el estilo con MapProperties(mapStyleOptions = ...) usando MapStyleOptions.loadRawResourceStyle(context, R.raw.mapa_oscuro) cuando sea oscuro, y null cuando no lo sea.
- Dame el archivo res/raw/mapa_oscuro.json completo y válido (sin comentarios), y el código completo de MapaUbicacion.kt.
# Mejoras que propuse
#### 4   Interruptor de modo claro y oscuro en la barra superior, guardado con DataStore, con el modo aplicado al tema y al mapa. 
Proyecto Android con Kotlin, Jetpack Compose, Material 3, DataStore Preferences y maps-compose 8.4.0.
Quiero un interruptor en la barra superior que cambie la app entre modo claro y oscuro, y que guarde la elección para la siguiente vez que se abra la app.

Reglas:
- El repositorio (interfaz e implementación con DataStore) va en la capa data y recibe el Context en su constructor.
- El ViewModel expone el modo como StateFlow y tiene una función para cambiarlo. No recibe Context.
- La UI solo observa el modo y envía el cambio. No lee ni guarda preferencias.
- MainActivity aplica el tema según el modo, y el mapa recibe el mismo modo como parámetro.
- Indica en qué archivo va cada cambio y dame solo lo necesario.
#### 5   Etiquetas "Último lugar visitado" y "Lugar más cercano" dentro de cada tarjeta de la lista. 
Proyecto Android con Kotlin y Jetpack Compose. En ListaLugares.kt, cada tarjeta muestra el nombre, la categoría y la distancia.
Quiero que la tarjeta seleccionada muestre la etiqueta "Último lugar visitado" y la del lugar más cercano muestre "Lugar más cercano".

Reglas:
- Solo cambia la capa ui (ListaLugares.kt). No cambies el ViewModel ni el Repository.
- Si un lugar cumple las dos condiciones, muestra ambas etiquetas.
- Usa los colores ya definidos en la app para cada etiqueta.
- Dame el archivo completo.
#### 6   Degradado azul marino en la barra superior y en el botón principal.
Proyecto Android con Jetpack Compose y Material 3. La barra superior y el botón principal usan un azul fijo.
Quiero que usen un azul marino con degradado horizontal.

Reglas:
- Define el degradado una sola vez en Colores.kt con Brush.horizontalGradient.
- La barra superior va dentro de un Box con el degradado, y TopAppBar usa containerColor transparente.
- El botón va dentro de un Box con clip circular y el degradado, y el Button usa colores transparentes.
- No cambies la lógica ni los estados de los componentes.
- Dame los archivos completos.
#### 7  Corrección de legibilidad en modo oscuro y bloqueo de orientación vertical. 
Proyecto Android con Jetpack Compose y Material 3, con modo claro y oscuro controlado por la app.
En modo oscuro, algunos textos y elementos en azul marino casi no se ven, y el texto del botón principal sale oscuro. Además, al girar el teléfono la pantalla se corta.

Reglas:
- Crea funciones que devuelvan el color de acento y el fondo de selección según el tema, usando la luminancia del fondo del tema.
- Usa color blanco explícito en el botón principal.
- Bloquea la orientación de MainActivity en vertical en AndroidManifest.xml.
- Indica en qué archivo va cada cambio y dame los archivos completos.