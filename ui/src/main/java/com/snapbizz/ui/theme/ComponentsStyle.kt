package com.snapbizz.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object SnapButtonStyle {
    val DefaultBackground = Color.Blue
    val DefaultTextColor = Color.White
    val DefaultCornerRadius = 12.dp
}

object SnapTextStyle {
    val Default = TextStyle(
        fontSize = 16.sp,
        color = Color.Black,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Normal
    )

    val Bold = TextStyle(
        fontSize = 18.sp,
        color = Color.Black,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold
    )

    val Large = TextStyle(
        fontSize = 22.sp,
        color = Color.Black,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Medium
    )
}
