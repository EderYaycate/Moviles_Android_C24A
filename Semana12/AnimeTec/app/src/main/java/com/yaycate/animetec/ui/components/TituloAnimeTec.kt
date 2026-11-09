package com.yaycate.animetec.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

@Composable
fun TituloAnimeTec(
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.headlineLarge
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary

    val annotatedString = buildAnnotatedString {
        withStyle(SpanStyle(color = primaryColor, fontWeight = FontWeight.Bold)) {
            append("Anime")
        }
        withStyle(SpanStyle(color = secondaryColor, fontWeight = FontWeight.Bold)) {
            append("Tec")
        }
    }

    Text(
        text = annotatedString,
        style = style,
        modifier = modifier
    )
}
