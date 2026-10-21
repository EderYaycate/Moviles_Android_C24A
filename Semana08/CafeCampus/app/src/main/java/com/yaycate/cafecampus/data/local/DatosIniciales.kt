package com.yaycate.cafecampus.data.local

import com.yaycate.cafecampus.data.local.entity.Producto
import com.yaycate.cafecampus.data.local.entity.Usuario
import com.yaycate.cafecampus.model.Rol

object DatosIniciales {

    suspend fun sembrar(db: CafeDatabase) {
        if (db.usuarioDao().contar() > 0) return

        val productoDao = db.productoDao()
        listOf(
            // Bebidas
            Producto(nombre = "Café americano", descripcion = "Café recién hecho", categoria = "Bebidas", precio = 4.00, icono = "cafe"),
            Producto(nombre = "Capuchino", descripcion = "Espresso con leche espumosa", categoria = "Bebidas", precio = 6.50, icono = "cafe"),
            Producto(nombre = "Jugo de naranja", descripcion = "Natural, recién exprimido", categoria = "Bebidas", precio = 5.00, icono = "jugo"),
            Producto(nombre = "Limonada natural", descripcion = "Con hielo y menta", categoria = "Bebidas", precio = 4.00, icono = "jugo"),
            Producto(nombre = "Chicha morada", descripcion = "Vaso de 500 ml", categoria = "Bebidas", precio = 3.50, icono = "botella"),
            Producto(nombre = "Té helado", descripcion = "De durazno o limón", categoria = "Bebidas", precio = 4.50, icono = "botella"),

            // Comidas
            Producto(nombre = "Sándwich de pollo", descripcion = "Pan integral con pollo", categoria = "Comidas", precio = 9.00, icono = "plato"),
            Producto(nombre = "Menú del día", descripcion = "Entrada, segundo y bebida", categoria = "Comidas", precio = 12.00, icono = "plato"),
            Producto(nombre = "Pollo a la brasa (cuarto)", descripcion = "Con papas fritas y ensalada", categoria = "Comidas", precio = 14.00, icono = "plato"),
            Producto(nombre = "Arroz chaufa", descripcion = "Con pollo y huevo", categoria = "Comidas", precio = 11.00, icono = "plato"),
            Producto(nombre = "Hamburguesa clásica", descripcion = "Carne, queso y vegetales", categoria = "Comidas", precio = 13.00, icono = "plato"),

            // Panadería
            Producto(nombre = "Empanada", descripcion = "De carne o pollo", categoria = "Panadería", precio = 4.50, icono = "pan"),
            Producto(nombre = "Croissant de jamón y queso", descripcion = "Horneado en el día", categoria = "Panadería", precio = 6.00, icono = "pan"),
            Producto(nombre = "Pan con chicharrón", descripcion = "Pan crocante con chicharrón", categoria = "Panadería", precio = 7.50, icono = "pan"),
            Producto(nombre = "Queque de vainilla", descripcion = "Porción individual", categoria = "Panadería", precio = 3.50, icono = "pan"),

            // Snacks
            Producto(nombre = "Papas fritas", descripcion = "Porción mediana", categoria = "Snacks", precio = 5.00, icono = "plato"),
            Producto(nombre = "Nachos con queso", descripcion = "Con salsa de queso", categoria = "Snacks", precio = 6.50, icono = "plato"),
            Producto(nombre = "Galleta de avena", descripcion = "Hecha en casa", categoria = "Snacks", precio = 2.50, icono = "pan")
        ).forEach { productoDao.insertar(it) }

        db.usuarioDao().insertar(
            Usuario(nombre = "Eder", correo = "eder@campus.test", clave = "1234", rol = Rol.CLIENTE.name)
        )
        db.usuarioDao().insertar(
            Usuario(nombre = "Administrador", correo = "admin@cafe.test", clave = "admin", rol = Rol.ADMIN.name)
        )
    }
}