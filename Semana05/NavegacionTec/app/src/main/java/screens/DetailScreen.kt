package com.yaycate.navegaciontec.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yaycate.navegaciontec.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    itemId: Int,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Expediente Académico",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PurpleHeaderDark
                )
            )
        },
        containerColor = BackgroundLavender
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(HeaderCardGradient)
                    .padding(bottom = 24.dp, top = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(
                        modifier = Modifier
                            .size(76.dp)
                            .border(2.dp, Color.White.copy(alpha = 0.9f), CircleShape),
                        shape = CircleShape,
                        color = Color(0xFFD6C8EA)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "EY",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = PurpleHeaderDark
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Eder Yaycate",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = "Diseño y Desarrollo de Software",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    color = SurfaceWhite
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        ProfileFieldItem(
                            icon = Icons.Default.Badge,
                            label = "ID Estudiante",
                            value = "2024-000$itemId"
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = BackgroundLavender)
                        ProfileFieldItem(
                            icon = Icons.Default.Email,
                            label = "Correo Electrónico",
                            value = "eder.yaycate@tecsup.edu.pe"
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = BackgroundLavender)
                        ProfileFieldItem(
                            icon = Icons.Default.School,
                            label = "Facultad / Carrera",
                            value = "Diseño y Desarrollo de Software (4to Ciclo)"
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Biografía",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Estudiante de 4to ciclo enfocado en el diseño y desarrollo de software con Jetpack Compose y Android Studio.",
                            fontSize = 13.sp,
                            color = TextGray,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}