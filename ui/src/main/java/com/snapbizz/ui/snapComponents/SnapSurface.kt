package com.snapbizz.ui.snapComponents

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun SnapSurface(
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    elevation: Dp = 4.dp,
    cornerRadius: Dp = 12.dp,
    padding: PaddingValues = PaddingValues(16.dp),
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier.padding(padding),
        color = color,
        elevation = elevation,
        shape = RoundedCornerShape(cornerRadius)
    ) {
        content()
    }
}
