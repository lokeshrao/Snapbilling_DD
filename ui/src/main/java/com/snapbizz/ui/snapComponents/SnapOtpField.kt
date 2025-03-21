package com.ban.otptextfield

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snapbizz.common.config.SnapThemeConfig
import com.snapbizz.ui.snapComponents.SnapText

@Composable
fun SnapOtpField(
    modifier: Modifier = Modifier,
    label: String = "Enter OTP",
    otpText: String,
    otpCount: Int = 6,
    onOtpTextChange: (String, Boolean) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }  // Track focus state

    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        // Label
        SnapText(
            textAlign = TextAlign.Start,
            text = label,
            fontSize = 14.sp,
            color = SnapThemeConfig.Text,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp) // Space between label and OTP boxes
        )

        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused }, // Update focus state
            value = TextFieldValue(otpText, selection = TextRange(otpText.length)),
            onValueChange = {
                if (it.text.length <= otpCount) {
                    onOtpTextChange.invoke(it.text, it.text.length == otpCount)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            decorationBox = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    repeat(otpCount) { index ->
                        CharView(
                            index = index,
                            text = otpText,
                            isFocused = isFocused && (index == otpText.length || (otpText.length == otpCount && index == otpCount - 1))
                        )
                    }
                }
            }
        )
    }
}

@Composable
private fun CharView(
    index: Int,
    text: String,
    isFocused: Boolean
) {
    val char = text.getOrNull(index)?.toString() ?: ""  // Show character if exists, otherwise empty

    Box(
        modifier = Modifier
            .size(50.dp)  // Square box
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(
                1.dp, if (isFocused) SnapThemeConfig.Primary else Color.Gray, RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center 
    ) {
        SnapText(
            text = char,
            fontSize = 20.sp,
            color = SnapThemeConfig.Text,
            textAlign = TextAlign.Center
        )
    }
}
