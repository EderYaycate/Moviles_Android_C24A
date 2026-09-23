package com.yaycate.navegaciontec.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yaycate.navegaciontec.ui.theme.*

@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLavender)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(HeaderCardGradient)
                .statusBarsPadding()
                .padding(bottom = 24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "Configuración de Perfil",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    modifier = Modifier
                        .size(80.dp)
                        .border(2.dp, Color.White.copy(alpha = 0.9f), CircleShape),
                    shape = CircleShape,
                    color = Color(0xFFD6C8EA)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "EY",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = PurpleHeaderDark
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Eder Yaycate",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "INFORMACIÓN PERSONAL",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextGray,
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(2.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                color = SurfaceWhite
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ProfileFieldItem(
                        icon = Icons.Default.Person,
                        label = "Nombre Completo",
                        value = "Eder Yaycate"
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = BackgroundLavender)
                    ProfileFieldItem(
                        icon = Icons.Default.Email,
                        label = "Correo",
                        value = "eder.yaycate@tecsup.edu.pe"
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = BackgroundLavender)
                    ProfileFieldItem(
                        icon = Icons.Default.Phone,
                        label = "Teléfono",
                        value = "+51 987 654 321"
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "ACADÉMICO",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextGray,
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(2.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                color = SurfaceWhite
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ProfileFieldItem(
                        icon = Icons.Default.School,
                        label = "Carrera",
                        value = "Diseño y Desarrollo de Software"
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = BackgroundLavender)
                    ProfileFieldItem(
                        icon = Icons.Default.CalendarMonth,
                        label = "Ciclo Actual",
                        value = "4to Ciclo"
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = LogoutRed),
                border = androidx.compose.foundation.BorderStroke(1.dp, LogoutRed.copy(alpha = 0.3f))
            ) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = null,
                    tint = LogoutRed,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Cerrar Sesión",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = LogoutRed
                )
            }
        }
    }
}

@Composable
fun ProfileFieldItem(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            shape = CircleShape,
            color = BackgroundLavender,
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = PurpleHeaderDark,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = label,
                fontSize = 11.sp,
                color = TextGray
            )
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDark,
                modifier = Modifier.padding(top = 1.dp)
            )
        }
    }
}