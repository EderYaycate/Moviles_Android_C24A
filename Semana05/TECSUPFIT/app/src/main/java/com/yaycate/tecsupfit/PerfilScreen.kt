package com.yaycate.tecsupfit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PerfilScreen(usuario: UsuarioPerfil) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Mi perfil",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )

        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Color(0xFFE8F5E9), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = usuario.iniciales,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF005A44)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(text = usuario.nombre, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(text = usuario.plan, color = Color.Gray)

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatCard(modifier = Modifier.weight(1f), valor = "${usuario.clasesTomadas}", etiqueta = "Clases")
            StatCard(modifier = Modifier.weight(1f), valor = "${usuario.rachaDias}", etiqueta = "Rachas")
        }
    }
}

@Composable
fun StatCard(modifier: Modifier = Modifier, valor: String, etiqueta: String) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = valor, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(text = etiqueta, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
    }
}