package com.snapbizz.ui.snapComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import com.snapbizz.common.config.SnapThemeConfig

@Composable
fun SnapEditText(
    value: String,
    onValueChange: ((String) -> Unit)?=null,
    modifier: Modifier = Modifier,
    label: String? = null,
    hint: String = "",
    backgroundColor: Color = SnapThemeConfig.PrimaryBg,
    textColor: Color = SnapThemeConfig.Text,
    hintColor: Color = SnapThemeConfig.Hint,
    cornerRadius: Int = 12,
    leadingIconResId: Int? = null,
    trailingIconResId: Int? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    isPassword: Boolean = false,
    enabled: Boolean = true,
) {
    var textState by remember { mutableStateOf(value) }

    Column(modifier = modifier.fillMaxWidth()) {
        label?.let {
            SnapText(
                text = it,
                fontSize = 14.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(cornerRadius.dp))
                .background(backgroundColor)
                .border(1.dp, Color.Gray, RoundedCornerShape(cornerRadius.dp))
                .padding(horizontal = 12.dp),
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
                    if (textState.isEmpty()) {
                        SnapText(
                            text = hint,
                            color = hintColor,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterStart)
                        )
                    }

                    BasicTextField(
                        value = textState,
                        onValueChange = {
                            textState = it
                            onValueChange?.invoke(it)
                        },
                        textStyle = TextStyle(fontSize = 16.sp, color = textColor),
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
                        enabled = enabled
                    )
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




