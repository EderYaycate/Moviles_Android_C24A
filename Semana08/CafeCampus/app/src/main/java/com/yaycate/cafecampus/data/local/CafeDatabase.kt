package com.yaycate.cafecampus.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yaycate.cafecampus.data.local.dao.PedidoDao
import com.yaycate.cafecampus.data.local.dao.ProductoDao
import com.yaycate.cafecampus.data.local.dao.UsuarioDao
import com.yaycate.cafecampus.data.local.entity.DetallePedido
import com.yaycate.cafecampus.data.local.entity.Pedido
import com.yaycate.cafecampus.data.local.entity.Producto
import com.yaycate.cafecampus.data.local.entity.Usuario

@Database(
    entities = [Producto::class, Usuario::class, Pedido::class, DetallePedido::class],
    version = 1,
    exportSchema = false
)
abstract class CafeDatabase : RoomDatabase() {

    abstract fun productoDao(): ProductoDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun pedidoDao(): PedidoDao

    companion object {
        @Volatile
        private var INSTANCE: CafeDatabase? = null

        fun obtener(context: Context): CafeDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    CafeDatabase::class.java,
                    "cafecampus.db"
                ).build().also { INSTANCE = it }
            }
    }
}