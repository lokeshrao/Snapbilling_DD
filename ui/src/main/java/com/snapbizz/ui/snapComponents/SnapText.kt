package com.snapbizz.ui.snapComponents

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import com.snapbizz.ui.theme.SnapTextComponent
import com.snapbizz.ui.theme.SnapTextStyle

@Composable
fun SnapText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = SnapTextComponent.defaultFontSize,
    color: Color = SnapTextComponent.defaultTextColor,
    fontWeight: FontWeight = SnapTextComponent.defaultFontWeight,
    textAlign: TextAlign = SnapTextComponent.defaultTextAlign
) {
    BasicText(
        text = text,
        modifier = modifier,
        style = TextStyle(
            fontSize = fontSize,
            color = color,
            fontWeight = fontWeight,
            textAlign = textAlign
        )
    )
}
