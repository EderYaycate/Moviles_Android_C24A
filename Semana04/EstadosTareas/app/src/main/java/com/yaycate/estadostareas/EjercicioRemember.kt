package com.yaycate.estadostareas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ContadorRoto() {
    var contador = 0
    Column {
        Text("Contador: $contador")
        Button(onClick = { contador++ }) {
            Text("Incrementar")
        }
    }
}

@Composable
fun ContadorConRemember() {
    var contador by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Contador: $contador",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { contador++ }) {
            Text("Incrementar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewContadorRoto() {
    MaterialTheme {
        ContadorRoto()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewContadorConRemember() {
    MaterialTheme {
        ContadorConRemember()
    }
}