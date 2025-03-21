package com.snapbizz.ui.snapComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.snapbizz.ui.R


@Composable
fun AxisBanner(
    modifier: Modifier = Modifier,
    backgroundImage: Int = R.drawable.banner,
    overlayImage: Int = R.drawable.axis_logo,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Image(
            painter = painterResource(id = backgroundImage),
            contentDescription = "Header Background",
            modifier = Modifier.fillMaxWidth().height(50.dp),
            contentScale = ContentScale.FillBounds
        )

        Image(
            painter = painterResource(id = overlayImage),
            contentDescription = "Overlay Image",
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(80.dp).padding(start = 10.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAxisBanner() {
    AxisBanner(
        modifier = Modifier.padding(16.dp)
    )
}
