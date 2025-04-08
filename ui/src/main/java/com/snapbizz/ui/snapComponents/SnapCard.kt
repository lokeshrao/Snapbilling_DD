package com.snapbizz.ui.snapComponents

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.snapbizz.common.config.SnapThemeConfig
import com.snapbizz.core.utils.Dimens

@Composable
fun SnapCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = SnapThemeConfig.ContainerBg,
    cornerRadius: Dp = Dimens.cardCornerRadiusLarge,
    elevation: Dp = Dimens.cardElevationMedium,
    padding: Dp = Dimens.paddingMedium,
    isOutlined: Boolean = false,
    isVisible: Boolean = true,
    showAnimation: Boolean = false,
    borderColor: Color = SnapThemeConfig.Primary,
    borderWidth: Dp = Dimens.borderWidth,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    val baseModifier = modifier
        .then(
            if (isOutlined)
                Modifier.border(borderWidth, borderColor, shape)
            else
                Modifier.shadow(elevation, shape)
        )
        .background(backgroundColor, shape)
        .then(
            if (onClick != null)
                Modifier.clickable { onClick() }
            else Modifier
        )
        .padding(padding)

    if (showAnimation) {
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(
                modifier = baseModifier,
                verticalArrangement = Arrangement.spacedBy(Dimens.paddingSmall),
                content = content
            )
        }
    } else if (isVisible) {
        Column(
            modifier = baseModifier,
            verticalArrangement = Arrangement.spacedBy(Dimens.paddingSmall),
            content = content
        )
    }
}
