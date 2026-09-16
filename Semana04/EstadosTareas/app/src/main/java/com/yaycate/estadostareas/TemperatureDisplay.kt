package com.yaycate.estadostareas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TemperatureDisplay() {
    var temperatura by remember { mutableStateOf(20) }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$temperatura°C",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTemperatureDisplay() {
    MaterialTheme {
        TemperatureDisplay()
    }
}