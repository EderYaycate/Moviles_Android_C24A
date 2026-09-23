package com.yaycate.navegaciontec.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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

data class StudentItem(val id: Int, val name: String, val career: String, val initials: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    onNavigateToDetail: (Int) -> Unit,
    onBack: () -> Unit
) {
    val students = listOf(
        StudentItem(1, "Eder Yaycate", "Diseño y Desarrollo de Software", "EY"),
        StudentItem(3, "María García", "Arquitectura", "MG"),
        StudentItem(4, "Carlos Pérez", "Medicina", "CP"),
        StudentItem(5, "Ana López", "Derecho", "AL"),
        StudentItem(6, "Luis Ramírez", "Administración", "LR")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Directorio de Alumnos",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = PurpleHeaderDark
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = PurpleHeaderDark
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFEBE3F5)
                )
            )
        },
        containerColor = BackgroundLavender
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(students) { student ->
                StudentCardItem(student = student) {
                    onNavigateToDetail(student.id)
                }
            }
        }
    }
}

@Composable
fun StudentCardItem(student: StudentItem, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color.Black.copy(alpha = 0.04f)
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = SurfaceWhite
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = PurpleHeaderDark,
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = student.initials,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = student.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Text(
                    text = student.career,
                    fontSize = 12.sp,
                    color = TextGray,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = TextGray.copy(alpha = 0.6f)
            )
        }
    }
}
