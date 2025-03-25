package com.snapbizz.ui.snapComponents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.snapbizz.common.config.SnapThemeConfig

@Composable
fun SnapDivider(
    modifier: Modifier = Modifier,
    color: Color = SnapThemeConfig.SurfaceBg,
    thickness: Dp = 1.dp,
    topPadding: Dp = 0.dp,
    bottomPadding: Dp = 0.dp,
    startPadding: Dp = 0.dp,
    endPadding: Dp = 0.dp,
    cornerRadius: Dp = 0.dp
) {
    Divider(
        color = color,
        thickness = thickness,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = topPadding,
                bottom = bottomPadding,
                start = startPadding,
                end = endPadding
            )
            .then(if (cornerRadius > 0.dp) Modifier.clip(RoundedCornerShape(cornerRadius)) else Modifier)
    )
}
