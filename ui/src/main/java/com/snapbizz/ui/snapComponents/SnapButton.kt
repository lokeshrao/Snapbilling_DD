package com.snapbizz.ui.snapComponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import com.snapbizz.common.config.SnapThemeConfig
import com.snapbizz.ui.theme.SnapTextStyle
import kotlinx.coroutines.launch

@Composable
fun SnapButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = SnapThemeConfig.Primary,
    textColor: Color = SnapThemeConfig.OnPrimary,
    isLoading: Boolean = false,
    iconResId: Int? = null,
    cornerRadius: Int = 12,
    debounce: Boolean = false,
    debounceTimeMs: Long = 3000
) {
    var lastClickTime by remember { mutableLongStateOf(0L) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(cornerRadius.dp))
            .background(backgroundColor)
            .clickable(enabled = !isLoading) {
                val currentTime = System.currentTimeMillis()
                if (!debounce || currentTime - lastClickTime > debounceTimeMs) {
                    lastClickTime = currentTime
                    coroutineScope.launch {
                        onClick()
                    }
                }
            }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = textColor,
                    strokeWidth = 2.dp
                )
            } else {
                if (iconResId != null) {
                    Image(
                        painter = painterResource(id = iconResId),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                BasicText(
                    text = text,
                    style = TextStyle(fontSize = 14.sp, color = textColor, textAlign = TextAlign.Center)
                )
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
    textColor: Color = SnapThemeConfig.OnPrimary,
    borderColor: Color = SnapThemeConfig.Primary,
    backgroundColor: Color = Color.Transparent,
    contentPadding: Modifier = Modifier,
    textStyle: TextUnit = SnapTextStyle.Default.fontSize
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.then(contentPadding),
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = backgroundColor,
            contentColor = textColor,
            disabledContentColor = textColor.copy(alpha = 0.4f)
        ),
        border = BorderStroke(1.dp, borderColor)
    ) {
        SnapText(modifier = Modifier.padding(8.dp,4.dp), text = text, fontSize = textStyle, color = SnapThemeConfig.OnPrimary)
    }
}

@Preview
@Composable
fun PreviewSnapButton() {
    SnapButton(text = "Click Me", onClick = {}, debounce = true)
}
