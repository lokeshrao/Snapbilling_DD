package com.snapbizz.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snapbizz.common.config.SnapThemeConfig
import com.snapbizz.core.utils.Dimens
import com.snapbizz.core.utils.Dimens.borderWidth_2
import com.snapbizz.core.utils.Dimens.cornerMedium
import com.snapbizz.core.utils.Dimens.cornerSmall
import com.snapbizz.core.utils.Dimens.iconSizeExtraSmall
import com.snapbizz.core.utils.Dimens.inputFieldHeightSmall
import com.snapbizz.core.utils.Dimens.paddingMedium
import com.snapbizz.core.utils.Dimens.paddingSmall

object SnapButtonStyle {
    val defaultCornerRadius = cornerSmall
    val defaultHeight = inputFieldHeightSmall
    val horizontalPadding = paddingMedium
    val verticalPadding = paddingSmall
    val iconSize = iconSizeExtraSmall
    val textFontSize = SnapTextStyle.Default.fontSize
    val borderWidth = Dimens.borderWidth
    val strokeWidth = borderWidth_2
    val deBounceTimeMs = 3000L
    val textStyle = SnapTextStyle.Default
    val defaultBackgroundColor = SnapThemeConfig.Primary
    val disabledBackgroundColor = Color.Gray
    val defaultTextColor = SnapThemeConfig.OnPrimary
    val defaultBorderColor = SnapThemeConfig.Primary
    val disabledTextColor = SnapThemeConfig.OnPrimary.copy(alpha = 0.4f)
    val outLinedButtonBackground = Color.Transparent
}

object SnapEditTextStyle {
    val DefaultCornerRadius = 12.dp
    val backGroundColor = SnapThemeConfig.ContainerBg
    val textColor = SnapThemeConfig.Text
    val hintColor = SnapThemeConfig.Hint
    val cornerRadius = cornerSmall
    val focusBorderColor = SnapThemeConfig.Primary
    val unfocusedBorderColor = Color.Gray
    val cursorColor = SnapThemeConfig.Primary
    val selectionHandleColor = SnapThemeConfig.Primary
    val selectionBackgroundColor = SnapThemeConfig.Primary.copy(alpha = 0.3f)
    val keyboardType = KeyboardType.Text
    val labelPadding = paddingSmall
    val fieldHeight = inputFieldHeightSmall
    val horizontalPadding = Dimens.paddingSmallMedium
    val iconSize = Dimens.iconSizeSmall
    val iconPaddingEnd = paddingSmall
    val textFontSize = SnapTextStyle.Default.fontSize
    val hintFontSize = SnapTextStyle.Default.fontSize
    val boxBorder = Dimens.borderWidth
    val textStyle = SnapTextStyle.Default
    val labelColor =  SnapThemeConfig.Text
}

object SnapTextStyle {
    val Default = TextStyle(
        fontSize = 14.sp,
        color = Color.Black,
        textAlign = TextAlign.Left,
        fontWeight = FontWeight.Normal
    )

    val Medium = TextStyle(
        fontSize = 16.sp,
        color = Color.Black,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold
    )

    val Bold = TextStyle(
        fontSize = 18.sp,
        color = Color.Black,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold
    )

    val Large = TextStyle(
        fontSize = 22.sp,
        color = Color.Black,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Medium
    )
}
