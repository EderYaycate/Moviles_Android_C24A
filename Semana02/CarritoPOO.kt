package poo

abstract class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int
) {
    fun calcularImporte(): Double = precio * cantidad

    abstract fun calcularImpuesto(): Double

    open fun mostrarInfo(): String {
        return String.format("%-20s x%d S/ %8.2f", nombre, cantidad, calcularImporte())
    }
}

class ProductoGravado(nombre: String, precio: Double, cantidad: Int) : Producto(nombre, precio, cantidad) {
    override fun calcularImpuesto(): Double = calcularImporte() * 0.18
}

class ProductoExonerado(nombre: String, precio: Double, cantidad: Int) : Producto(nombre, precio, cantidad) {
    override fun calcularImpuesto(): Double = 0.0

    override fun mostrarInfo(): String {
        return super.mostrarInfo() + "  (exonerado de IGV)"
    }

}
class ProductoImportado(nombre: String, precio: Double, cantidad: Int) : Producto(nombre, precio, cantidad) {
    override fun calcularImpuesto(): Double {
        val igv = calcularImporte() * 0.18
        val arancel = calcularImporte() * 0.06
        return igv + arancel
    }

    override fun mostrarInfo(): String {
        return super.mostrarInfo() + "  (importado, incluye arancel)"
    }
}
class Carrito {
    private val productos = mutableListOf<Producto>()

    fun agregarProducto(producto: Producto) {
        productos.add(producto)
        println("Producto agregado: ${producto.nombre}")
    }

    fun calcularSubtotal(): Double = productos.sumOf { it.calcularImporte() }

    fun calcularIGV(): Double = productos.sumOf { it.calcularImpuesto() }

    fun calcularTotal(): Double = calcularSubtotal() + calcularIGV()

    fun calcularDescuento(total: Double): Double {
        return when {
            total > 5000 -> total * 0.10
            total > 3000 -> total * 0.05
            else -> 0.0
        }
    }

    fun productoMasCaro(): Producto? = productos.maxByOrNull { it.precio }

    fun mostrarDetalle() {
        println("--------- DETALLE DEL CARRITO ---------")
        var i = 1
        for (p in productos) {
            println("$i. ${p.mostrarInfo()}")
            i++
        }
        println("---------------------------------------")
    }

    fun cantidadProductos(): Int = productos.size
}

fun main() {
    println("=========================================")
    println("   CARRITO DE COMPRAS - TIENDA TECSUP   ")
    println("        (Version POO con Polimorfismo)   ")
    println("=========================================")

    val nombreCliente = "Eder Yaycate"
    val carrito = Carrito()

    println("Cliente: $nombreCliente")
    println()

    carrito.agregarProducto(ProductoGravado("Laptop HP", 2500.0, 1))
    carrito.agregarProducto(ProductoGravado("Mouse Logitech", 45.5, 2))
    carrito.agregarProducto(ProductoGravado("Audifonos Xiomi", 120.0, 1))
    carrito.agregarProducto(ProductoExonerado("Libro Kotlin desde cero", 60.0, 1))
    carrito.agregarProducto(ProductoImportado("Smartwatch Xiaomi", 350.0, 1))

    println()
    carrito.mostrarDetalle()
    println("Cantidad de productos : ${carrito.cantidadProductos()}")

    val subtotal = carrito.calcularSubtotal()
    val igv = carrito.calcularIGV()
    val total = carrito.calcularTotal()

    println(String.format("Subtotal               : S/ %8.2f", subtotal))
    println(String.format("IGV                    : S/ %8.2f", igv))
    println(String.format("TOTAL A PAGAR          : S/ %8.2f", total))

    val masCaro = carrito.productoMasCaro()
    if (masCaro != null) {
        println("Producto mas caro: ${masCaro.nombre} " + String.format("(S/ %.2f)", masCaro.precio))
    }

    val descuento = carrito.calcularDescuento(total)
    val totalConDescuento = total - descuento

    when {
        total > 5000 -> println("Descuento aplicado: 10% por compra mayor a S/ 5000")
        total > 3000 -> println("Descuento aplicado: 5% por compra mayor a S/ 3000")
        else -> println("Sin descuento aplicado")
    }

    println(String.format("TOTAL CON DESCUENTO    : S/ %8.2f", totalConDescuento))
}