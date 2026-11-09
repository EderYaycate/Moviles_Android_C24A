# Prompt: Fase 2 de AnimeTec (rama anime-ia)

## Contexto del proyecto

Trabajas sobre AnimeTec, una app Android en Kotlin con Jetpack Compose, Material 3, MVVM, Retrofit, Room y Firebase (Authentication y Firestore). Consume la API de Kitsu (https://kitsu.io/api/edge/). El paquete es com.yaycate.animetec y el proyecto está en Semana12/AnimeTec. La Fase 1 ya está terminada y funcionando en la rama main. Crea y trabaja en la rama anime-ia.

Estado actual (Fase 1), que debes respetar y leer antes de cambiar nada:
- Capas: data/remote (ApiService, RetrofitClient, dto), data/local (ItemEntity, ItemDao, AppDatabase con Room), data/repository (ItemRepository interface e ItemRepositoryImpl, Room como fuente de verdad), data/auth (AuthRepository interface con iniciarSesion, sesionActual y cerrarSesion; FirebaseAuthRepository con las excepciones propias CredencialesInvalidasException y SinConexionException), domain/model (Item, Sesion, Rol), notification (interface Notificador y NotificadorAndroid), ui (UiState, ViewModelFactory, DetalleViewModelFactory, components, theme, navigation/AppNav, y las carpetas login, lista, detalle, favoritos y admin).
- AppContainer(context) crea la base de datos, los repositorios y el notificador; AnimeApp lo guarda. Los ViewModels se crean con ViewModelFactory.
- La navegación empieza en login. La barra inferior tiene Lista, Favoritos y Admin, y Admin solo se muestra si el rol es ADMIN. Hay un botón Salir para cerrar sesión. El rol se lee de Firestore (usuarios/{uid}, campo rol).
- Ya están en Gradle: Coil 2.7.0, Room 2.8.5, Retrofit, firebase-bom 34.17.0, firebase-auth, firebase-firestore y kotlinx-coroutines-play-services 1.8.1. El proyecto usa AGP 9.3.3 y Kotlin 2.2.10 con catálogo libs.versions.toml.
- Ya existen 0 pruebas unitarias.

## Reglas generales (valen para todas las etapas)

1. Usa solo Kotlin, coroutines, Jetpack Compose y Material 3. Usa solo íconos de material-icons-core.
2. No uses Firebase, Retrofit ni Room dentro de composables. Los ViewModel no reciben Context ni importan clases de Android (salvo ViewModel).
3. No renombres ni cambies la firma de los métodos que ya existen en las interfaces ItemRepository, AuthRepository y Notificador: solo agrega métodos nuevos.
4. Todos los colores deben salir de MaterialTheme.colorScheme, nunca colores fijos en las pantallas.
5. No toques Gradle ni google-services.json, salvo las dos dependencias que se indican expresamente (kotlinx-coroutines-test en la etapa 1 y com.google.mlkit:translate en la etapa 3). google-services.json nunca se sube al repositorio.
6. No modifiques el ListaViewModel salvo en la etapa 1 si fuera necesario para probarlo, y no cambies su comportamiento.
7. Cada ViewModel nuevo debe registrarse en ViewModelFactory (en un caso de su when), usando las dependencias del AppContainer. Antes de terminar cada etapa, comprueba que todos los ViewModels que pide AppNav tienen su caso; de lo contrario la app se cierra con "ViewModel desconocido".
8. Mantén las firmas de los composables públicos existentes; si necesitas parámetros nuevos, ponlos con valor por defecto.
9. Los errores de Firebase y de los flujos no se tragan: se atrapan, se registran con Log.e (etiquetas AUTH, PERFIL, NOVEDADES) y se muestran al usuario con un mensaje claro. La app nunca debe cerrarse por un error de red o de permisos.
10. Después de cada etapa: compila, corre .\gradlew testDebugUnitTest (las pruebas deben seguir en verde) y haz un commit con el mensaje indicado. Si no puedes terminar todo en una respuesta, termina la etapa en curso, compila y espera a que te diga "continúa".
11. Al final de cada etapa explícame qué archivos creaste o modificaste y por qué.

## Etapa 1: pruebas unitarias del ListaViewModel

Agrega en app/build.gradle.kts la dependencia testImplementation de kotlinx-coroutines-test (versión 1.8.1). En app/src/test crea:
- MainDispatcherRule: una TestWatcher de JUnit 4 que reemplaza Dispatchers.Main por un StandardTestDispatcher o UnconfinedTestDispatcher en starting() y lo restaura en finished().
- FakeItemRepository, que implementa ItemRepository con listas en memoria (un MutableStateFlow para items y favoritos), un resultado configurable para refrescar() (éxito con N o fallo con una excepción, como IOException), y contadores de llamadas.
- FakeNotificador, que implementa Notificador y guarda los mensajes que recibe.
- ListaViewModelTest con al menos 5 pruebas: (1) el estado inicial es Cargando; (2) tras un refresco exitoso con datos el estado es Exito con la lista; (3) un fallo sin datos guardados deja UiState.Error con el mensaje "No hay conexión a internet"; (4) un fallo con datos guardados mantiene los datos y deja el aviso "Sin conexión: mostrando datos guardados"; (5) un refresco exitoso llama al notificador con "Se guardaron N elementos". Usa runTest y los repositorios falsos, sin tocar Room ni internet.
  Commit: "test: pruebas del ListaViewModel con repositorio falso".

## Etapa 2: colores de marca, tema y título

- Color.kt: colores de marca. Claro: AnimeRojo = 0xFFE0605E y TecAzul = 0xFF5B8DEF. Oscuro: AnimeRojoOscuro = 0xFFFF8A80 y TecAzulOscuro = 0xFF8AB4FF.
- Theme.kt: AnimeTecTheme con dynamicColor = false por defecto y los parámetros darkTheme y dynamicColor. lightColorScheme y darkColorScheme con primary = rojo; secondary y tertiary = azul; sus onPrimary/onSecondary legibles y containers suaves derivados del mismo tono.
- ui/components/TituloAnimeTec.kt: composable TituloAnimeTec(modifier, style) que dibuja "AnimeTec" con buildAnnotatedString: "Anime" en colorScheme.primary y "Tec" en colorScheme.secondary, en negrita.
- En las TopAppBar de Lista, Favoritos y Admin, usa TituloAnimeTec como título.
  Commit: "feat: tema con colores de marca y titulo AnimeTec".

## Etapa 3: login, recuperar contraseña y registro

Logo y diseño del login (el recurso R.drawable.logo_animetec lo agrega el usuario; si no existe, usa un placeholder y avísame):
- LoginScreen: fondo colorScheme.background, logo de 120.dp con RoundedCornerShape(24.dp) y ContentScale.Crop, TituloAnimeTec en headlineLarge, campos con RoundedCornerShape(16.dp) e íconos Email y Lock, el botón Ingresar con Brush.horizontalGradient de primary a secondary (alpha 0.9), texto blanco en negrita, esquinas de 26.dp y alto de 52.dp (opacidad menor si está deshabilitado, conservando el indicador de carga). "¿Olvidaste tu contraseña?" en color secondary y "¿Eres nuevo? Regístrate" con "Regístrate" en color primary. Contenido con verticalScroll e imePadding, y entrada animada (AnimatedVisibility con fadeIn y slideInVertically). No cambies la lógica de iniciar sesión.

Recuperar contraseña:
- AuthRepository: agrega suspend fun recuperarClave(correo: String): Result<Unit>.
- FirebaseAuthRepository: recorta el correo (trim) y pásalo a minúsculas; llama a auth.useAppLanguage() antes de sendPasswordResetEmail(...).await() para que el correo salga en el idioma del teléfono; registra cualquier excepción con Log.e("AUTH", "recuperarClave", e). Errores: FirebaseAuthInvalidUserException se trata como ÉXITO (respuesta neutra, para no revelar si el correo existe); FirebaseAuthInvalidCredentialsException => "El correo no tiene un formato válido"; FirebaseTooManyRequestsException => "Demasiados intentos. Espera unos minutos e inténtalo de nuevo"; falta de red => SinConexionException con su mensaje actual; cualquier otra => "No se pudo enviar el correo. Inténtalo de nuevo".
- LoginViewModel: recuperarClave(correo) valida SIN llamar a Firebase que el correo no esté vacío y contenga "@". Expón el resultado como una data class MensajeRecuperacion(texto, esExito) en un StateFlow, con una función para limpiarlo. El mensaje de éxito es neutro: "Si el correo está registrado, te enviamos un enlace para restablecer tu contraseña".
- LoginScreen: un TextButton "¿Olvidaste tu contraseña?" abre un AlertDialog con un campo de correo (RoundedCornerShape(16.dp), ícono Email, precargado con el correo del login), botones Cancelar y Enviar (con indicador de carga y deshabilitado mientras envía). El éxito se muestra en colorScheme.secondary con ✅, y los errores en colorScheme.error. La visibilidad del diálogo se guarda con remember.

Registro:
- AuthRepository: agrega suspend fun registrar(correo: String, clave: String): Result<Sesion>.
- FirebaseAuthRepository: createUserWithEmailAndPassword(...).await() y luego la Sesion con el mismo método privado que usa iniciarSesion para leer el rol. Si el usuario nuevo no tiene documento en usuarios/{uid}, el rol resultante es Rol.USUARIO y no se lanza excepción: nadie puede registrarse como administrador. No escribas nada en Firestore. Traduce los errores con el estilo de excepciones existente: correo ya registrado (FirebaseAuthUserCollisionException), clave débil (FirebaseAuthWeakPasswordException), correo inválido (FirebaseAuthInvalidCredentialsException) y red (SinConexionException).
- RegistroViewModel: valida SIN llamar a Firebase que correo y clave no estén vacíos, que el correo contenga "@", que la clave tenga mínimo 6 caracteres y que la confirmación coincida, con mensajes en español. Al tener éxito, la sesión se maneja igual que tras un login exitoso.
- RegistroScreen: mismo estilo que el login (fondo colorScheme.background, logo, título "Crear cuenta" en headlineMedium en negrita), campos Correo, Contraseña y Confirmar contraseña con RoundedCornerShape(16.dp), íconos Email y Lock y un IconButton Visibility/VisibilityOff (estado local con remember), botón "Crear cuenta" con degradado de primary a secondary (26.dp, alto 52.dp, indicador de carga), errores en una Card de errorContainer con AnimatedVisibility, "¿Ya tienes cuenta? Inicia sesión" con "Inicia sesión" en primary, y entrada animada con verticalScroll e imePadding.
- AppNav y ViewModelFactory: ruta "registro"; el LoginScreen navega a ella. Tras registrarse, navega al catálogo sin dejar el login ni el registro en la pila. Registra RegistroViewModel en ViewModelFactory.
  Commit: "feat: logo, recuperar contrasena y registro de usuarios".

## Etapa 4: traducir la sinopsis (ML Kit)

Kitsu solo entrega la sinopsis en inglés. Agrega en app/build.gradle.kts la dependencia implementation("com.google.mlkit:translate:17.0.3") (y sincroniza). Luego:
- Paquete com.yaycate.animetec.translation: interface Traductor con suspend fun traducirAEspanol(texto: String): Result<String>.
- TraductorMlKit (sin recibir Context): un Translator por lazy con TranslatorOptions.Builder().setSourceLanguage(TranslateLanguage.ENGLISH).setTargetLanguage(TranslateLanguage.SPANISH). Si el texto está en blanco, devuelve éxito con el mismo texto. Si no, downloadModelIfNeeded(DownloadConditions.Builder().build()).await() y luego translate(texto).await() dentro de un try/catch que devuelva Result; en el catch vuelve a lanzar CancellationException. Usa el await() de kotlinx.coroutines.tasks.
- AppContainer: val traductor: Traductor by lazy { TraductorMlKit() }.
- DetalleViewModel: recibe el traductor como segundo parámetro (repo, traductor, id). Crea sealed interface EstadoTraduccion (Original, Traduciendo, Traducida(texto), Fallo(mensaje)) y expón traduccion como StateFlow. traducirSinopsis() toma la sinopsis cuando el estado es UiState.Exito (si es nula o está en blanco, no hace nada; si ya está en Traduciendo, ignora la llamada), pasa a Traduciendo, traduce en viewModelScope y pasa a Traducida o a Fallo("No se pudo traducir. Revisa tu conexión e inténtalo de nuevo"). verOriginal() vuelve a Original. cargar() reinicia la traducción a Original.
- ViewModelFactory: DetalleViewModelFactory crea DetalleViewModel(container.itemRepository, container.traductor, id). Ojo: es una clase aparte de ViewModelFactory, no un miembro suyo.
- DetalleScreen: en la sección "Sinopsis" muestra el texto traducido si el estado es Traducida, y si no, el original. Debajo, solo si hay sinopsis, un TextButton: "Traducir al español" (Original); deshabilitado con CircularProgressIndicator de 16.dp y "Traduciendo…" (Traduciendo); "Ver original" (Traducida); "Reintentar traducción" con el mensaje de error en colorScheme.error encima (Fallo).
- En el mapper del detalle, decodifica las entidades HTML de la sinopsis (por ejemplo &amp; y &quot;) con HtmlCompat.fromHtml(...).toString(), manejando el caso nulo.
  Commit: "feat: traducir la sinopsis del detalle al espanol con ML Kit".

## Etapa 5: animaciones, buscador y diseño de lista, favoritos y detalle

Animaciones y diseño:
- ListaScreen.ItemCard: ElevatedCard; portada con RoundedCornerShape; el puntaje con un ícono Star en colorScheme.secondary y el número; el tipo (TV, MOVIE...) con color tertiary suave; subtítulo en onSurfaceVariant. El corazón de favorito escala con animateFloatAsState y un spring al marcarlo y usa colorScheme.primary cuando está marcado (onSurfaceVariant si no). Los elementos de la LazyColumn usan Modifier.animateItem() conservando la key. El banner de aviso aparece con AnimatedVisibility (expandVertically + fadeIn y shrinkVertically + fadeOut). El cambio entre carga, vacío y lista usa Crossfade.
- Muestra el banner de aviso con un componente AvisoBanner con colores errorContainer/onErrorContainer.
- DetalleScreen: cambio entre Cargando, Error y Contenido con AnimatedContent o Crossfade; estado, tipo y fecha de estreno como AssistChip en un FlowRow (solo los que no sean nulos, con el @OptIn necesario); puntaje destacado con ícono Star y texto grande; la sinopsis dentro de un contenedor con animateContentSize().
- AppNav: transiciones del NavHost: al entrar al detalle, deslizamiento horizontal con fade; entre las pestañas de la barra inferior, solo fadeIn y fadeOut. No cambies rutas ni argumentos.

Buscador:
- ui/lista/FiltroItems.kt: función pura fun List<Item>.filtrarPorTitulo(consulta: String): List<Item> que devuelve la misma lista si la consulta recortada está vacía y, si no, los items cuyo título la contenga ignorando mayúsculas. No toques ListaViewModel ni FavoritosViewModel.
- Composable público BuscadorAnime: OutlinedTextField con RoundedCornerShape(28.dp), ícono Search, placeholder "Buscar anime..." y un IconButton Close solo si hay texto.
- ListaRoute y FavoritosRoute guardan la consulta con rememberSaveable y pasan la lista filtrada. Si no hay resultados, estado vacío con 🔍 y "No encontramos animes con ese nombre" (AnimatedVisibility). Debajo del buscador, un texto con el número de resultados ("20 animes").
- FavoritosScreen: encabezado con una Card primaryContainer "❤️ Tus favoritos" con el contador, el buscador debajo, y estado vacío con 💔, "Aún no tienes favoritos" y "Toca el corazón de un anime para guardarlo aquí". Mantén animateItem().
  Commit: "feat: animaciones, buscador y nuevo diseno en lista, favoritos y detalle".

## Etapa 6: modo oscuro seleccionable

- ui/theme/ModoTema: enum SISTEMA, CLARO, OSCURO.
- Interface AjustesTema (leer(): ModoTema y guardar(modo)) y AjustesTemaPrefs con SharedPreferences ("ajustes_prefs", clave "modo_tema"; si el valor no existe o es inválido, SISTEMA), que recibe el applicationContext por constructor.
- AppContainer: ajustesTema por lazy. TemaViewModel(ajustes) con un StateFlow<ModoTema> inicial ajustes.leer() y cambiar(modo). Regístralo en ViewModelFactory.
- MainActivity: crea el TemaViewModel, observa su estado y pasa darkTheme = when(modo) { SISTEMA -> isSystemInDarkTheme(); CLARO -> false; OSCURO -> true } a AnimeTecTheme. Pasa a AppNav el modo y un callback onCambiarModo, que AppNav entrega a la pantalla de perfil (etapa 7).
- Revisa que ninguna pantalla use colores fijos; todo sale de colorScheme.
  Commit: "feat: selector de modo claro, oscuro o del sistema".

## Etapa 7: perfil de usuario con foto

- domain/model/Perfil(nombre, correo, rol, fotoBase64: String?).
- PerfilRepository (interface): obtener(uid): Result<Perfil> (lee usuarios/{uid}; si no hay nombre, usa la parte del correo antes del @), guardarNombre(uid, nombre) y guardarFoto(uid, fotoBase64). FirestorePerfilRepository: lee el campo con el nombre exacto "fotoBase64". Para guardar, si el documento existe usa update del campo; si no existe, lo crea con set(mapOf(campo to valor, "rol" to "usuario")). Nunca escribe el campo rol en un documento existente. Atrapa excepciones y devuelve Result.failure: PERMISSION_DENIED => "No tienes permiso para guardar. Revisa las reglas de Firestore"; falta de red => mensaje claro. Registra los errores con Log.e("PERFIL", ...).
- ProcesadorFoto (interface: aBase64(uri: String): Result<String>) y ProcesadorFotoAndroid (recibe el applicationContext): abre el Uri con contentResolver, decodifica con BitmapFactory usando inSampleSize, corrige la orientación leyendo los metadatos EXIF con android.media.ExifInterface (constructor con InputStream; aplica rotación/volteo con Matrix; si no se puede leer el EXIF, sigue sin rotar), recorta al centro en cuadrado, escala a 256x256, comprime a JPEG calidad 80 y devuelve Base64.NO_WRAP (android.util.Base64). Todo en Dispatchers.IO.
- AppContainer: perfilRepository y procesadorFoto por lazy.
- PerfilViewModel(authRepository, perfilRepository, procesadorFoto, ...): UiState<Perfil> (Cargando, Exito, Error con Reintentar); obtiene el uid con authRepository.sesionActual(); cargar(); guardarNombre(nombre) (valida que no esté vacío ni pase de 30 caracteres, sin llamar a Firestore si no es válido); cambiarFoto(uri: String) (procesa, guarda y actualiza el estado con la foto nueva sin recargar); cerrarSesion() usando el mecanismo actual de cierre de sesión. StateFlow<String?> de mensajes ("Perfil actualizado" o el error) y StateFlow<Boolean> "guardando". Regístralo en ViewModelFactory.
- PerfilScreen (PerfilRoute + contenido), Material 3:
    - Encabezado con un Box de fondo Brush.verticalGradient (primaryContainer hacia surface), avatar circular de 128.dp con anillo de borde Brush.sweepGradient de primary a secondary, y un SmallFloatingActionButton con ícono Edit en su esquina que lanza ActivityResultContracts.PickVisualMedia (solo imágenes, sin permisos).
    - La foto NO se pasa como ByteArray a Coil (Coil 2.7.0 no lo soporta): decodifica el Base64 a ByteArray y a Bitmap con BitmapFactory.decodeByteArray dentro de remember/produceState keyed por fotoBase64, fuera del hilo principal (Dispatchers.Default), y muéstralo con Image(bitmap.asImageBitmap()), ContentScale.Crop y clip CircleShape. Si el decode falla o no hay foto, muestra las iniciales en un círculo primaryContainer. Crossfade al cambiar la foto.
    - Nombre en headlineMedium y el rol como AssistChip con ícono (Star si es admin, Person si es usuario), con colores de la marca (admin: primary; usuario: secondary).
    - Una ElevatedCard "👤 Cuenta" con el correo (ícono Email), el rol y el nombre editable en un OutlinedTextField con trailing IconButton Check para guardar (deshabilitado si no cambió) y contador x/30.
    - Una ElevatedCard "🎨 Apariencia" llamativa, con fondo Brush.horizontalGradient de primaryContainer a secondaryContainer, título en titleMedium en negrita, la ayuda "Elige cómo quieres ver AnimeTec" y un SingleChoiceSegmentedButtonRow con "📱 Sistema", "☀️ Claro" y "🌙 Oscuro" que llama a onCambiarModo.
    - Botón "Cerrar sesión" como FilledTonalButton con colores errorContainer/onErrorContainer y el ícono Icons.AutoMirrored.Filled.ExitToApp, con un AlertDialog de confirmación.
    - Contenido desplazable, tarjetas con entrada escalonada (AnimatedVisibility con fadeIn + slideInVertically), SnackbarHost para los mensajes, estados Cargando (componente Cargando) y Error (ErrorView con Reintentar), y barra superior transparente con flecha de volver.
- AppNav: ruta "perfil". En las TopAppBar de Lista y Favoritos, a la derecha, un IconButton con un avatar pequeño (ícono Person dentro de un círculo) que navega a "perfil". Quita el botón Salir de donde esté y deja el cierre de sesión solo en el perfil; al cerrar sesión navega a login limpiando la pila, como hoy.
  Commit: "feat: perfil de usuario con foto, nombre editable y cierre de sesion".

## Etapa 8: novedades (campana del usuario y gestión del administrador)

Lado del usuario:
- domain/model/Novedad(id, titulo, mensaje, animeId: Int?, fecha: Long).
- NovedadesRepository (interface) con fun observar(): Flow<List<Novedad>>. FirestoreNovedadesRepository: callbackFlow con addSnapshotListener sobre la colección "novedades", ordenada por "fecha" descendente, limit(50); campos titulo (String), mensaje (String), animeId (número opcional) y fecha (número en milisegundos, leído con getLong). Ignora los documentos mal formados. Si el listener da error, cierra el flujo con esa excepción. Usa awaitClose.
- LecturaNovedades (interface: ultimaVista(uid): Long, marcarVistas(uid, hasta)) y LecturaNovedadesPrefs con SharedPreferences ("novedades_prefs", una clave por uid), con el applicationContext. AppContainer: novedadesRepository y lecturaNovedades por lazy.
- NovedadesViewModel(authRepository, novedadesRepository, lecturaNovedades, notificador): obtiene el uid con authRepository.sesionActual() (si no hay sesión, no escucha; debe empezar a escuchar en cuanto haya uid y no quedarse sin escuchar para siempre). Escucha observar() en viewModelScope en un Job que se pueda cancelar; atrapa los errores del flujo (no debe cerrarse la app), deja la última lista y un campo de error, y registra con Log.e("NOVEDADES", ...). Expón un StateFlow con la lista, sinLeer (cuántas tienen fecha mayor que ultimaVista(uid), empezando en 0), ultimaVistaAnterior y el error. La primera emisión del flujo NO genera notificación; en las siguientes, por cada novedad nueva con fecha mayor que ultimaVista(uid), llama a notificador.mostrar(titulo, mensaje). marcarVistas() guarda el valor previo en ultimaVistaAnterior, llama a lecturaNovedades.marcarVistas(uid, fechaMasReciente) y deja sinLeer en 0; solo se llama al abrir la pantalla Novedades, nunca al crear la campana. Agrega reintentar(): cancela el Job anterior, limpia el error y vuelve a escuchar (un flujo de Firestore se cierra al fallar por permisos y no se reconecta solo). Regístralo en ViewModelFactory.
- NovedadesScreen (NovedadesRoute + contenido): TopAppBar con flecha de volver y título "🔔 Novedades". LazyColumn con animateItem(); cada novedad en una ElevatedCard con un círculo a la izquierda con degradado de primary a secondary y el ícono Notifications, el título en negrita, el mensaje, la fecha relativa (DateUtils.getRelativeTimeSpanString) en un AssistChip pequeño, un Badge "Nuevo" si es posterior a ultimaVistaAnterior y un FilledTonalButton "Ver anime" que llama a onVerAnime(animeId) si tiene animeId. Al abrir la pantalla llama a viewModel.marcarVistas() (LaunchedEffect). Estado vacío con 🔕 y "Aún no hay novedades". Si hay error: una Card de errorContainer con un TextButton "Reintentar" (ícono Refresh) que llama a reintentar(); el mensaje es "No tienes permiso para ver las novedades. Revisa las reglas de Firestore" si el error contiene PERMISSION_DENIED, y "No se pudieron cargar las novedades" en otro caso.
- AppNav: crea el NovedadesViewModel UNA sola vez, con viewModel(key = "novedades-" + uid de la sesión actual), solo cuando haya sesión (para que Lista y Favoritos compartan el contador y se cree uno nuevo al entrar con otra cuenta; usa el uid que ya guarda SesionViewModel). En la TopAppBar de Lista y Favoritos, a la izquierda del avatar, un IconButton con ícono Notifications dentro de un BadgedBox; el Badge con el número solo se muestra si sinLeer > 0 y aparece con una animación de escala. Rutas "novedades" y, desde "Ver anime", "detalle/{id}".

Lado del administrador:
- NovedadesRepository: agrega publicar(titulo, mensaje, animeId: Int?), actualizar(id, titulo, mensaje, animeId: Int?) y eliminar(id), todos Result<Unit>. FirestoreNovedadesRepository: publicar guarda un documento nuevo con titulo, mensaje, animeId (solo si no es nulo) y fecha = System.currentTimeMillis() (Long); actualizar hace update de titulo, mensaje y animeId (FieldValue.delete() si animeId es nulo) sin tocar fecha; PERMISSION_DENIED => "No tienes permiso para publicar" / "No tienes permiso para editar".
- NovedadesAdminViewModel(novedadesRepository): lista de novedades publicadas (con observar(), atrapando errores), StateFlow<Boolean> "publicando", StateFlow<String?> de mensajes y StateFlow<Novedad?> "enEdicion". publicar(titulo, mensaje, animeIdTexto) y guardarEdicion(...) validan SIN llamar a Firestore: título no vacío y máximo 60, mensaje máximo 300, ID de anime solo dígitos. editar(novedad), cancelarEdicion() y eliminar(id) (si se elimina la que se está editando, se cancela la edición). Mensajes "Novedad publicada" y "Novedad actualizada". Regístralo en ViewModelFactory.
- NovedadesAdminSection (ElevatedCard dentro de AdminScreen): título "📢 Publicar novedad"; campos de título (contador x/60), mensaje (minLines 3, contador x/300) e ID del anime (opcional, teclado numérico, solo dígitos, apoyo "Opcional: abre el detalle de ese anime"); botón Publicar con degradado de primary a secondary y CircularProgressIndicator mientras publica (al terminar, limpia los campos). Debajo "📋 Publicadas": cada novedad en una Card con un círculo con 🔔, título, fecha, y dos IconButton: Edit y Delete. Delete abre un AlertDialog ("¿Eliminar esta novedad?", Cancelar/Eliminar). Edit rellena el formulario, cambia el título a "Editar novedad" y el botón a "Guardar cambios", con un TextButton "Cancelar"; el cambio se anima con AnimatedContent o Crossfade y la novedad en edición se resalta. Estado vacío con 📭 "Aún no has publicado novedades". Mensajes en Snackbar o Text, con AnimatedVisibility y animateItem().
  Commit: "feat: novedades para el usuario y gestion de novedades para el admin".

## Etapa 9: panel Admin y notificación del sistema

- AdminScreen: encabezado con un Box de fondo Brush.verticalGradient de primaryContainer a surface y el texto "⚙️ Panel de administración"; los totales (guardados y favoritos) en dos ElevatedCard lado a lado con emoji (📚 y ❤️), el número en headlineLarge y una etiqueta pequeña; botón Sincronizar con ícono Refresh y colores primary; Limpiar caché como OutlinedButton con color de error, con un AlertDialog de confirmación ("¿Borrar los datos guardados?" / "Los favoritos también se borrarán.", botones Cancelar y Borrar) cuya visibilidad se guarda con remember; debajo, NovedadesAdminSection. La pantalla es desplazable (verticalScroll). No cambies AdminViewModel.
- NotificadorAndroid: mantén el canal, el permiso POST_NOTIFICATIONS y el PendingIntent a MainActivity. Mejora la notificación: título con prefijo "🔔 ", NotificationCompat.BigTextStyle con el texto completo, setColor con el rojo de la marca (0xFFE0605E), setAutoCancel(true), setCategory(CATEGORY_MESSAGE) y prioridad alta. Debe seguir funcionando igual para "Se guardaron N elementos".
  Commit: "feat: estilo del panel admin y notificacion del sistema".

