package com.snapbizz.ui.snapComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.snapbizz.ui.theme.SnapButtonStyle.borderWidth
import com.snapbizz.ui.theme.SnapButtonStyle.deBounceTimeMs
import com.snapbizz.ui.theme.SnapButtonStyle.defaultBackgroundColor
import com.snapbizz.ui.theme.SnapButtonStyle.defaultBorderColor
import com.snapbizz.ui.theme.SnapButtonStyle.defaultCornerRadius
import com.snapbizz.ui.theme.SnapButtonStyle.defaultHeight
import com.snapbizz.ui.theme.SnapButtonStyle.defaultTextColor
import com.snapbizz.ui.theme.SnapButtonStyle.disabledBackgroundColor
import com.snapbizz.ui.theme.SnapButtonStyle.disabledTextColor
import com.snapbizz.ui.theme.SnapButtonStyle.horizontalPadding
import com.snapbizz.ui.theme.SnapButtonStyle.iconSize
import com.snapbizz.ui.theme.SnapButtonStyle.outLinedButtonBackground
import com.snapbizz.ui.theme.SnapButtonStyle.strokeWidth
import com.snapbizz.ui.theme.SnapButtonStyle.textFontSize
import com.snapbizz.ui.theme.SnapButtonStyle.verticalPadding
import kotlinx.coroutines.launch

@Composable
fun SnapButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = defaultBackgroundColor,
    textColor: Color = defaultTextColor,
    isLoading: Boolean = false,
    isEnabled: Boolean=true,
    iconResId: Int? = null,
    cornerRadius: Dp = defaultCornerRadius,
    debounce: Boolean = false,
    debounceTimeMs: Long = deBounceTimeMs
) {
    var lastClickTime by remember { mutableLongStateOf(0L) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .height(defaultHeight)
            .clip(RoundedCornerShape(cornerRadius))
            .background(if(isEnabled) backgroundColor else disabledBackgroundColor)
            .clickable(enabled = !isLoading&&isEnabled) {
                val currentTime = System.currentTimeMillis()
                if (!debounce || currentTime - lastClickTime > debounceTimeMs) {
                    lastClickTime = currentTime
                    coroutineScope.launch {
                        onClick()
                    }
                }
            }
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (iconResId != null && !isLoading) {
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = null,
                    modifier = Modifier.size(iconSize),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(verticalPadding))
            }

            // Fixed-size container for text to avoid button size shrinking
            Box(modifier = Modifier.defaultMinSize(80.dp), contentAlignment = Alignment.Center) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(iconSize),
                        color = textColor,
                        strokeWidth = strokeWidth
                    )
                } else {
                    BasicText(
                        text = text,
                        style = TextStyle(
                            fontSize = textFontSize,
                            color = textColor,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun SnapOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textColor: Color = defaultTextColor,
    backgroundColor: Color = outLinedButtonBackground,
    borderColor: Color = defaultBorderColor,
    cornerRadius: Dp = defaultCornerRadius
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .height(defaultHeight)
            .clip(RoundedCornerShape(cornerRadius))
            .border(borderWidth, borderColor, RoundedCornerShape(cornerRadius)),
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = backgroundColor,
            contentColor = textColor,
            disabledContentColor = disabledTextColor
        ),
    ) {
        BasicText(
            text = text,
            style = TextStyle(
                fontSize = textFontSize,
                color = textColor,
                textAlign = TextAlign.Center
            )
        )
    }
}


@Preview
@Composable
fun PreviewSnapButton() {
    SnapButton(text = "Click Me", onClick = {}, debounce = true)
}
