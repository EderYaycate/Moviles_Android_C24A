package com.yaycate.campusgps.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.yaycate.campusgps.model.LugarConDistancia
import com.yaycate.campusgps.util.formatearDistancia

private val VerdeCercano = Color(0xFF34A853)

@Composable
fun ListaLugares(
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    masCercanoId: Int?,
    onSeleccionar: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(12.dp)
    val acento = acentoTema()
    val fondoSeleccion = fondoSeleccionTema()

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(lugares, key = { it.lugar.id }) { item ->
            val esSeleccionado = item.lugar.id == seleccionado
            val esMasCercano = item.lugar.id == masCercanoId
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = if (esSeleccionado) 2.dp else 1.dp,
                        color = if (esSeleccionado) acento
                        else MaterialTheme.colorScheme.outlineVariant,
                        shape = forma
                    )
                    .background(
                        color = if (esSeleccionado) fondoSeleccion else Color.Transparent,
                        shape = forma
                    )
                    .clickable { onSeleccionar(item.lugar.id) }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                if (esMasCercano) VerdeCercano else acento,
                                CircleShape
                            )
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            text = item.lugar.nombre,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = item.lugar.categoria,
                            style = MaterialTheme.typography.bodySmall
                        )
                        if (esSeleccionado) {
                            Text(
                                text = "Último lugar visitado",
                                style = MaterialTheme.typography.labelSmall,
                                color = acento
                            )
                        }
                        if (esMasCercano) {
                            Text(
                                text = "Lugar más cercano",
                                style = MaterialTheme.typography.labelSmall,
                                color = VerdeCercano
                            )
                        }
                    }
                }
                Text(
                    text = formatearDistancia(item.metros),
                    style = MaterialTheme.typography.titleSmall,
                    color = acento
                )
            }
        }
    }
}