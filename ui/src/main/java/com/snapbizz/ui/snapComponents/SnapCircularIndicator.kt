package com.snapbizz.ui.snapComponents

import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SnapCircularIndicator(
    modifier: Modifier = Modifier,
    color: Color = Color.Blue,
    strokeWidth: Float = 2f
) {
    CircularProgressIndicator(
        modifier = modifier.size(20.dp),
        color = color,
        strokeWidth = strokeWidth.dp
    )
}
