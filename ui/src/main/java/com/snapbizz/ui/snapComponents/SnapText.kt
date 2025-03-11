package com.snapbizz.ui.snapComponents

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun SnapText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onPrimary,
    fontSize: TextUnit = 16.sp,
    textAlign: TextAlign = TextAlign.Center,
    minLines:Int=1,
    maxLines:Int=Int.MAX_VALUE,
    fontWeight: FontWeight = FontWeight.Normal
) {
    Text(
        text = text,
        color = color,
        fontSize = fontSize,
        textAlign = textAlign,
        modifier = modifier,
        minLines = minLines,
        fontWeight=fontWeight,
        maxLines = maxLines
    )
}