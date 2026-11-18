# PROMPTS.md · Fase 2 (rama CentroTarea-ia)

Nota: las respuestas de esta tabla resumen la asistencia recibida durante la fase, no respuestas de Gemini. Si el profesor exige prompts de Gemini, hay que añadir filas con esos prompts.

| Prompt que escribí | Qué respondió | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Implementar cuatro mejoras en CentroTareas: tiempo máximo configurable, reintento automático (3 veces), historial de últimos resultados en memoria y comparación de tiempos entre "Una por una" y "A la vez" | Tiempo máximo con withTimeout en tareas y cargas; reintento con espera creciente en la carga de pedidos; lista de 5 resultados en un StateFlow; tiempos guardados por modo | Historial en memoria; el PDF sugiere DataStore, pero se eligió memoria para simplificar. Límite por defecto de 20 s en lugar de los 2 s del ejemplo del PDF, con chips para elegir 2, 5 o 20 s | Con 2 s por defecto, "A la vez" y "Una por una" superan siempre el límite y no se llega al error del Paso 7 |
