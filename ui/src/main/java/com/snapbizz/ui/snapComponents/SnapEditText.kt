package com.snapbizz.ui.snapComponents

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SnapEditText(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
    backgroundColor: Color = MaterialTheme.colors.surface,
    textColor: Color = MaterialTheme.colors.onSurface,
    hintColor: Color = Color.Gray,
    cornerRadius: Int = 12,
    leadingIconResId: Int? = null,
    trailingIconResId: Int? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    isPassword: Boolean = false
) {
    var textState by remember { mutableStateOf(value) }

    OutlinedTextField(
        value = textState,
        onValueChange = {
            textState = it
            onValueChange(it)
        },
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(cornerRadius.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = backgroundColor,
            textColor = textColor,
            placeholderColor = hintColor,
            focusedBorderColor = MaterialTheme.colors.primary,
            unfocusedBorderColor = Color.Gray
        ),
        placeholder = { Text(hint, color = hintColor, fontSize = 14.sp) },
        leadingIcon = leadingIconResId?.let {
            {
                Icon(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    tint = textColor
                )
            }
        },
        trailingIcon = trailingIconResId?.let {
            {
                IconButton(onClick = { onTrailingIconClick?.invoke() }) {
                    Icon(
                        painter = painterResource(id = it),
                        contentDescription = null,
                        tint = textColor
                    )
                }
            }
        },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
    )
}
