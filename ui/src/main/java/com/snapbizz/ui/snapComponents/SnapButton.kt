package com.snapbizz.ui.snapComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snapbizz.common.config.SnapThemeConfig
import kotlinx.coroutines.launch

@Composable
fun SnapButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = SnapThemeConfig.Primary,
    textColor: Color = SnapThemeConfig.OnPrimary,
    isLoading: Boolean = false,
    isEnabled: Boolean=true,
    iconResId: Int? = null,
    cornerRadius: Int = 8,
    debounce: Boolean = false,
    debounceTimeMs: Long = 3000
) {
    var lastClickTime by remember { mutableLongStateOf(0L) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(cornerRadius.dp))
            .background(if(isEnabled) backgroundColor else Color.Gray)
            .clickable(enabled = !isLoading&&isEnabled) {
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
    backgroundColor: Color = Color.Transparent,
    borderColor: Color = SnapThemeConfig.Primary,
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, borderColor, RoundedCornerShape(8.dp)),
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = backgroundColor,
            contentColor = textColor,
            disabledContentColor = textColor.copy(alpha = 0.4f)
        ),
    ) {
        BasicText(
            text = text,
            style = TextStyle(
                fontSize = 14.sp,
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
