package com.snapbizz.ui.snapComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snapbizz.common.config.SnapThemeConfig

@Composable
fun SnapEditText(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: ((String) -> Unit)? = null,
    label: String? = null,
    hint: String = "",
    backgroundColor: Color = SnapThemeConfig.PrimaryBg,
    textColor: Color = SnapThemeConfig.Text,
    hintColor: Color = SnapThemeConfig.Hint,
    cornerRadius: Int = 8,
    leadingIconResId: Int? = null,
    trailingIconResId: Int? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    isPassword: Boolean = false,
    enabled: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    focusBorderColor: Color = SnapThemeConfig.Primary,
    unfocusedBorderColor: Color = Color.Gray,
    cursorColor: Color = SnapThemeConfig.Primary,
    selectionHandleColor: Color = SnapThemeConfig.Primary,
    selectionBackgroundColor: Color = SnapThemeConfig.Primary.copy(alpha = 0.3f)
) {
    val passwordVisible by remember { mutableStateOf(!isPassword) }
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val customSelectionColors = TextSelectionColors(
        handleColor = selectionHandleColor,
        backgroundColor = selectionBackgroundColor
    )

    Column(modifier = modifier.fillMaxWidth()) {
        label?.let {
            SnapText(
                text = it,
                color = Color.DarkGray,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clip(RoundedCornerShape(cornerRadius.dp))
                .background(backgroundColor)
                .border(
                    1.dp,
                    if (isFocused) focusBorderColor else unfocusedBorderColor,
                    RoundedCornerShape(cornerRadius.dp)
                )
                .padding(horizontal = 12.dp)
                .focusable(interactionSource = interactionSource)
            ,
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize()
            ) {
                leadingIconResId?.let {
                    Image(
                        painter = painterResource(id = it),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 8.dp)
                    )
                }

                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        SnapText(
                            text = hint,
                            color = hintColor,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterStart)
                        )
                    }

                    CompositionLocalProvider(LocalTextSelectionColors provides customSelectionColors) {
                        BasicTextField(
                            value = value,
                            onValueChange = { onValueChange?.invoke(it) },
                            textStyle = TextStyle(fontSize = 14.sp, color = textColor),
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = if (!passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                            enabled = enabled,
                            interactionSource = interactionSource,
                            cursorBrush = SolidColor(cursorColor)
                        )
                    }
                }

                trailingIconResId?.let {
                    Image(
                        painter = painterResource(id = it),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onTrailingIconClick?.invoke() }
                    )
                }
            }
        }
    }
}

