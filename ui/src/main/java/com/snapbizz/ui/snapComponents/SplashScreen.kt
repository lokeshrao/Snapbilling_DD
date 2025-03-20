package com.snapbizz.ui.snapComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.snapbizz.ui.R

@Composable
fun SplashScreen() {

        Image(
            painter = painterResource(R.drawable.splash),
            contentDescription = "Splash Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

}
