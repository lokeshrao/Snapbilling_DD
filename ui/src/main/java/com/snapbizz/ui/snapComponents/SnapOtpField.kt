package com.snapbizz.ui.snapComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snapbizz.common.config.SnapThemeConfig

@Composable
fun SnapOtpField(
    modifier: Modifier = Modifier,
    otpLength: Int = 6,
    otpValue: String,
    onOtpChange: (String) -> Unit,
    label: String = "Enter OTP"
) {
    val focusManager = LocalFocusManager.current
    val otpArray = remember { mutableStateListOf(*Array(otpLength) { otpValue }) }
    val textFields = remember { List(otpLength) { FocusRequester() } }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        SnapText(
            text = label,
            fontSize = 14.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(bottom = 8.dp, start = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (index in 0 until otpLength) {
                BasicTextField(
                    value = otpArray[index],
                    onValueChange = { newText ->
                        if (newText.length <= 1) {
                            otpArray[index] = newText
                            onOtpChange(otpArray.joinToString(""))
                            if (newText.isNotEmpty() && index < otpLength - 1) {
                                textFields[index + 1].requestFocus()
                            }
                        }
                    },
                    textStyle = TextStyle(fontSize = 18 .sp, color = SnapThemeConfig.Text, textAlign = TextAlign.Center),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = if (index == otpLength - 1) ImeAction.Done else ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            if (index < otpLength - 1) {
                                textFields[index + 1].requestFocus()
                            }
                        },
                        onDone = { focusManager.clearFocus() }
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .padding(2.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .focusRequester(textFields[index])
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.key == Key.Backspace && otpArray[index].isEmpty() && index > 0) {
                                otpArray[index - 1] = ""
                                textFields[index - 1].requestFocus()
                                onOtpChange(otpArray.joinToString(""))
                            }
                            false
                        },
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            innerTextField()
                        }
                    }
                )
            }
        }
    }

    // Focus on the first field when the UI loads
    LaunchedEffect(Unit) {
        textFields.first().requestFocus()
    }
}
