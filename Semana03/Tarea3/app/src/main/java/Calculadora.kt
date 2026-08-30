val cuotasPermitidas = mapOf(
    6 to 0.20,
    12 to 0.40,
    24 to 0.60
)

fun main() {
    println("=========================================")
    println("   CALCULADORA DE CUOTAS ")
    println("=========================================")

    print("Ingrese el nombre del producto: ")
    val nombre = readLine() ?: "Producto sin nombre"

    print("Ingrese el precio: ")
    val precio = readLine()?.toDoubleOrNull() ?: 0.0

    print("Ingrese la cantidad: ")
    val cantidad = readLine()?.toIntOrNull() ?: 0

    var cuotas: Int
    while (true) {
        print("Ingrese el número de cuotas (6, 12 o 24): ")
        val valor = readLine()?.toIntOrNull()
        if (valor != null && cuotasPermitidas.containsKey(valor)) {
            cuotas = valor
            break
        } else {
            println("Error: solo se permiten 6, 12 o 24 cuotas. Intente de nuevo.")
        }
    }

    val montoInicial = precio * cantidad
    val tasaInteres = cuotasPermitidas[cuotas]!!
    val interes = montoInicial * tasaInteres
    val montoAPagar = montoInicial + interes

    println()
    println("=========================================")
    println("Producto        : $nombre")
    println("Monto Inicial   : S/ ${"%.2f".format(montoInicial)}")
    println("Interes (${(tasaInteres * 100).toInt()}%)   : S/ ${"%.2f".format(interes)}")
    println("Monto a Pagar   : S/ ${"%.2f".format(montoAPagar)}")
    println("=========================================")
}
