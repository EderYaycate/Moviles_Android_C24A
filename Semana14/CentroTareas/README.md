# CentroTareas

App Android hecha con Kotlin, Jetpack Compose y Material 3 (sin XML) que muestra cómo evitar que la pantalla se congele usando corrutinas. Incluye una tarea con barra de progreso y cancelación, el conteo de números primos en segundo plano y dos cargas simuladas (una por una frente a a la vez con `async`), con manejo de errores y reintento.

Todos los datos son ficticios: el perfil y los pedidos son simulados con `delay`.

## Estructura de paquetes

```
com.yaycate.centrotareas/
├── MainActivity.kt
├── model/
│   └── Estados.kt                 PrimosUi, CargaUi
├── data/
│   ├── TareasRepository.kt        interfaz
│   └── TareasRepositoryLocal.kt   cargas simuladas y contarPrimos
├── viewmodel/
│   ├── TareasViewModel.kt
│   └── TareasViewModelFactory.kt
└── ui/
    ├── PantallaTareas.kt
    └── theme/
```

## Diagrama MVVM + Repository

```
View (PantallaTareas)
   | eventos: botones              ^ collectAsState()
   v                               |
ViewModel (TareasViewModel) --- StateFlow --->
   | viewModelScope.launch { ... }
   v
TareasRepository (interface) ---> TareasRepositoryLocal
                                  (cargas simuladas y contarPrimos)
```

## Observación del Paso 1

Pendiente de confirmar en el emulador. Se espera que al pulsar "Tarea larga (MAL)" el hilo principal quede bloqueado 4 segundos por `Thread.sleep(4000)`: el contador "Tócame" no suma y la pantalla no se redibuja. Por eso el texto "Trabajando..." no llega a verse: la pantalla solo se actualiza cuando termina la función y ya muestra "Terminó". Si la espera fuera de 10 segundos, Android mostraría el diálogo "La aplicación no responde" (ANR), porque el hilo principal no responde a tiempo.

## Pruebas de aceptación

| Prueba | Resultado esperado | OK |
|---|---|---|
| Tarea larga: tocar Tócame mientras trabaja | El contador sigue sumando | ☐ |
| Logcat con filtro HILO | main antes y después; DefaultDispatcher-worker dentro | ☐ |
| Iniciar la tarea con progreso | La barra avanza de 0 a 100 % en ≈ 4 s | ☐ |
| Cancelar a mitad | La barra se detiene, dice Cancelado y se puede iniciar otra vez | ☐ |
| Pulsar Iniciar dos veces seguidas | No se lanzan dos tareas (el botón está deshabilitado) | ☐ |
| Primos hasta 1000 | Resultado 168 | ☐ |
| Primos con N grande | La pantalla no se congela y Cancelar funciona | ☐ |
| Una por una | ≈ 5 s y resultado correcto | ☐ |
| A la vez | ≈ 3 s y resultado correcto | ☐ |
| Error simulado + Reintentar | Recuadro rojo; al desactivar el interruptor y reintentar, éxito | ☐ |
| Rotar la pantalla durante la tarea | La tarea continúa y el progreso no se reinicia | ☐ |
| Revisar el código | git grep no encuentra GlobalScope, runBlocking ni Thread.sleep en el código final | ☐ |

## Para pensar

1. **¿Por qué no se veía "Trabajando..." en el Paso 1?** Porque Compose no puede redibujar mientras el hilo principal está bloqueado. El mensaje cambia a "Trabajando..." pero la pantalla no se actualiza hasta que termina `Thread.sleep`, y entonces ya dice "Terminó".
2. **¿Qué diferencia hay entre Thread.sleep y delay?** `Thread.sleep` bloquea el hilo donde se ejecuta. `delay` suspende la corrutina y libera el hilo para otras tareas, y la reanuda al cumplirse el tiempo.
3. **¿Por qué Dispatchers.Default para los primos y Dispatchers.IO para la red?** `Default` está pensado para cálculo intensivo de CPU, con un pool de hilos del tamaño de los núcleos. `IO` tiene un pool más grande para operaciones que pasan mucho tiempo esperando, como disco o red.
4. **¿Qué pasaría con la corrutina si el usuario cierra la pantalla?** `viewModelScope` cancela sus corrutinas cuando el ViewModel se destruye, es decir, al cerrar definitivamente la pantalla. Al rotar, el ViewModel sobrevive, así que la tarea continúa.
5. **¿Por qué async tarda 3 s y no 5 s? ¿Qué pasa si una de las dos cargas falla?** Con `async` las dos llamadas corren a la vez, así que el tiempo total es el de la más lenta (3 s) y no la suma (2 s + 3 s). Si una falla, `coroutineScope` cancela la otra y relanza el error, que el `catch` convierte en el estado Error.
