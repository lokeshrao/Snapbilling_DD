package com.snapbizz.ui.snapComponents

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.snapbizz.common.config.SnapThemeConfig

@Composable
fun SnapText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = SnapThemeConfig.Text,
    fontSize: TextUnit = 16.sp,
    textAlign: TextAlign = TextAlign.Center,
    fontWeight: FontWeight = FontWeight.Normal
) {
    BasicText(
        text = text,
        modifier = modifier,
        style = TextStyle(
            color = color,
            fontSize = fontSize,
            textAlign = textAlign,
            fontWeight = fontWeight
        )
    )
}