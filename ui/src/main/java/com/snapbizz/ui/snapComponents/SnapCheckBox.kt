package com.snapbizz.ui.snapComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snapbizz.common.config.SnapThemeConfig
import com.snapbizz.ui.R


@Composable
fun SnapCheckBox(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    checkedColor: Color = SnapThemeConfig.Primary,
    uncheckedColor: Color = SnapThemeConfig.Text,
    textColor: Color = SnapThemeConfig.Text
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable { onCheckedChange(!isChecked) }) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .border(
                    1.dp, if (isChecked) checkedColor else uncheckedColor, RoundedCornerShape(4.dp)
                )
                .background(
                    if (isChecked) checkedColor else Color.Transparent, RoundedCornerShape(4.dp)
                )
                .clickable { onCheckedChange(!isChecked) },
            contentAlignment = Alignment.Center,
        ) {
            if (isChecked) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_check_24),
                    contentDescription = "Checked",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        label?.let {
            SnapText(
                text = it,
                color = textColor,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically),
                textAlign = TextAlign.Start,
            )
        }
    }
}
