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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import com.snapbizz.ui.theme.SnapEditTextStyle
import com.snapbizz.ui.theme.SnapEditTextStyle.backGroundColor
import com.snapbizz.ui.theme.SnapEditTextStyle.boxBorder
import com.snapbizz.ui.theme.SnapEditTextStyle.cornerRadius
import com.snapbizz.ui.theme.SnapEditTextStyle.cursorColor
import com.snapbizz.ui.theme.SnapEditTextStyle.fieldHeight
import com.snapbizz.ui.theme.SnapEditTextStyle.focusBorderColor
import com.snapbizz.ui.theme.SnapEditTextStyle.hintColor
import com.snapbizz.ui.theme.SnapEditTextStyle.hintFontSize
import com.snapbizz.ui.theme.SnapEditTextStyle.horizontalPadding
import com.snapbizz.ui.theme.SnapEditTextStyle.iconPaddingEnd
import com.snapbizz.ui.theme.SnapEditTextStyle.iconSize
import com.snapbizz.ui.theme.SnapEditTextStyle.labelColor
import com.snapbizz.ui.theme.SnapEditTextStyle.labelPadding
import com.snapbizz.ui.theme.SnapEditTextStyle.selectionBackgroundColor
import com.snapbizz.ui.theme.SnapEditTextStyle.selectionHandleColor
import com.snapbizz.ui.theme.SnapEditTextStyle.textStyle
import com.snapbizz.ui.theme.SnapEditTextStyle.unfocusedBorderColor

@Composable
fun SnapEditText(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: ((String) -> Unit)? = null,
    label: String? = null,
    hint: String = "",
    leadingIconResId: Int? = null,
    trailingIconResId: Int? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    keyboardType: KeyboardType = SnapEditTextStyle.keyboardType
) {
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
                color = labelColor,
                modifier = Modifier.padding(vertical =  labelPadding)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(fieldHeight)
                .clip(RoundedCornerShape(cornerRadius))
                .background(backGroundColor)
                .border(boxBorder,
                    if (isFocused) focusBorderColor else unfocusedBorderColor,
                    RoundedCornerShape(cornerRadius)
                )
                .padding(horizontal = horizontalPadding)
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
                            .size(iconSize)
                            .padding(end = iconPaddingEnd)
                    )
                }

                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        SnapText(
                            text = hint,
                            color = hintColor,
                            fontSize = hintFontSize,
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterStart)
                        )
                    }

                    CompositionLocalProvider(LocalTextSelectionColors provides customSelectionColors) {
                        BasicTextField(
                            value = value,
                            onValueChange = { onValueChange?.invoke(it) },
                            textStyle = textStyle,
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = VisualTransformation.None,
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
                            .size(iconSize)
                            .clickable { onTrailingIconClick?.invoke() }
                    )
                }
            }
        }
    }
}

