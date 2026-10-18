package com.yaycate.cafecampus.data.local

import com.yaycate.cafecampus.data.local.entity.Producto
import com.yaycate.cafecampus.data.local.entity.Usuario
import com.yaycate.cafecampus.model.Rol

object DatosIniciales {

    suspend fun sembrar(db: CafeDatabase) {
        if (db.usuarioDao().contar() > 0) return

        val productoDao = db.productoDao()
        listOf(
            Producto(nombre = "Café americano", descripcion = "Café recién hecho", categoria = "Bebidas", precio = 4.00, icono = "cafe"),
            Producto(nombre = "Sándwich de pollo", descripcion = "Pan integral con pollo", categoria = "Comidas", precio = 9.00, icono = "plato"),
            Producto(nombre = "Menú del día", descripcion = "Entrada, segundo y bebida", categoria = "Comidas", precio = 12.00, icono = "plato"),
            Producto(nombre = "Jugo de naranja", descripcion = "Natural, recién exprimido", categoria = "Bebidas", precio = 5.00, icono = "jugo"),
            Producto(nombre = "Empanada", descripcion = "De carne o pollo", categoria = "Panadería", precio = 4.50, icono = "pan"),
            Producto(nombre = "Chicha morada", descripcion = "Vaso de 500 ml", categoria = "Bebidas", precio = 3.50, icono = "botella")
        ).forEach { productoDao.insertar(it) }

        db.usuarioDao().insertar(
            Usuario(nombre = "Juan", correo = "juan@campus.test", clave = "1234", rol = Rol.CLIENTE.name)
        )
        db.usuarioDao().insertar(
            Usuario(nombre = "Administrador", correo = "admin@cafe.test", clave = "admin", rol = Rol.ADMIN.name)
        )
    }
}