data class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int

)
fun calcularSubtotal(productos: List<Producto>): Double {
    var subtotal = 0.0
    for (p in productos) {
        subtotal += p.precio * p.cantidad
    }
    return subtotal
}
fun calcularIGV(subtotal: Double): Double {
     return  subtotal*0.18
}

fun calcularTotal(subtotal: Double, igv: Double): Double {
    return subtotal + igv
}

fun main() {
    println("=========================================")
    println("   CARRITO DE COMPRAS - TIENDA TECSUP   ")
    println("=========================================")
    val nombreCliente = "Eder Yaycate"
    val carrito = mutableListOf<Producto>()
    carrito.add(Producto("Laptop HP", 2500.0, 1))
    carrito.add(Producto("Mouse Logitech", 45.5, 2))
    carrito.add(Producto("Audifonos Xiomi", 120.0, 1))
    carrito.add(Producto("Teclado Logitech", 150.0, 3))
    println("Cliente: $nombreCliente")
    println()
    for (producto in carrito) {
        println("Producto agregado: ${producto.nombre}")
        }
        println()
        val subtotal = calcularSubtotal(carrito)
        val igv = calcularIGV(subtotal)
        val total = calcularTotal(subtotal, igv)

        println("Subtotal : S/ $subtotal")
        println("IGV (18%): S/ $igv")
        println("TOTAL    : S/ $total")
    }
