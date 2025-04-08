package com.snapbizz.ui.snapComponents

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snapbizz.common.config.SnapThemeConfig
import com.snapbizz.ui.R
import kotlinx.coroutines.launch

@Composable
fun SnapScaffoldWithDrawer(
    drawerItems: List<String>,
    bottomItems: List<BottomBarItem>,
    onDrawerItemClick: (String) -> Unit,
    onBottomItemClick: (String) -> Unit,
    bottomSelectedField: String,
    content: @Composable (PaddingValues) -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    var selectedItem  by rememberSaveable{ mutableStateOf(bottomSelectedField)}

    LaunchedEffect(Unit) {
        if(bottomItems.any { x-> x.label == bottomSelectedField }){
            onBottomItemClick(bottomSelectedField)
        }
    }
    ModalDrawer(
        drawerContent = {
            Column(modifier = Modifier.padding(16.dp)) {
                drawerItems.forEach { item ->
                    SnapText(
                        text = item,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                onDrawerItemClick(item)
                                scope.launch { scaffoldState.drawerState.close() }
                            }
                    )
                }
            }
        },
        drawerState = scaffoldState.drawerState
    ) {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    title = { Text("Home") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { scaffoldState.drawerState.open() }
                        }) {
                            Image(painter = painterResource(R.drawable.baseline_check_24), contentDescription = "")
                        }
                    }
                )
            },
            bottomBar = {
                CustomBottomAppBar(bottomItems,selectedItem){newSelected->
                    onBottomItemClick(newSelected).also { selectedItem=newSelected }

                }
            },
            content = content
        )
    }
}
data class BottomBarItem(
    val iconRes: Int,
    val label: String
)
@Composable
fun CustomBottomAppBar(
    items: List<BottomBarItem>,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    val selectedIndex = items.indexOfFirst { it.label == selectedItem }
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val itemWidth = screenWidth / items.size
    val circleSize = 42.dp
    val curveDepth = 30.dp

    val density = LocalDensity.current
    val targetOffset = if (selectedIndex != -1)
        (itemWidth * selectedIndex) + (itemWidth / 2) - (circleSize / 2)
    else 0.dp
    val animatedXOffset by animateDpAsState(targetValue = targetOffset, label = "circleXOffset")

    val itemWidthPx = with(density) { itemWidth.toPx() }
    val curveCenterTarget = if (selectedIndex != -1)
        itemWidthPx * selectedIndex + itemWidthPx / 2
    else -itemWidthPx

    val animatedCurveCenter by animateFloatAsState(
        targetValue = curveCenterTarget,
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "curveCenter"
    )
    val scale = remember { Animatable(0f) }
    val yOffset = remember { Animatable(10f) }

    LaunchedEffect(selectedItem) {
        scale.snapTo(0.8f)
        yOffset.snapTo(10f)
        launch {
            scale.animateTo(
                1f,
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
            )
        }
        launch {
            yOffset.animateTo(
                0f,
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
            )
        }
    }


    Box(modifier = Modifier.fillMaxWidth().height(60.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val curveDepthPx = curveDepth.toPx()
            val itemWidthPxCanvas = size.width / items.size
            val curveWidthPx = itemWidthPxCanvas * 0.8f
            val cutoutCenterX = animatedCurveCenter

            val startX = cutoutCenterX - (curveWidthPx / 2)
            val endX = cutoutCenterX + (curveWidthPx / 2)

            drawPath(
                path = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(startX, 0f)

                    // Curve path
                    cubicTo(
                        startX + curveWidthPx * 0.25f, 0f,
                        startX + curveWidthPx * 0.25f, curveDepthPx,
                        cutoutCenterX, curveDepthPx
                    )
                    cubicTo(
                        endX - curveWidthPx * 0.25f, curveDepthPx,
                        endX - curveWidthPx * 0.25f, 0f,
                        endX, 0f
                    )

                    lineTo(size.width, 0f)
                    lineTo(size.width, size.height)
                    lineTo(0f, size.height)
                    close()
                },
                color = Color.White
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = item.label == selectedItem

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                        .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onItemSelected(item.label) },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = item.iconRes),
                            contentDescription = item.label,
                            modifier = Modifier
                                .size(22.dp)
                                .padding(bottom = 2.dp)
                        )
                        Text(
                            text = item.label,
                            fontSize = 11.sp,
                            color = if (isSelected) SnapThemeConfig.Primary else Color.Gray
                        )
                    }
                }
            }
        }

        if (selectedIndex != -1) {
            Box(
                modifier = Modifier
                    .offset(x = animatedXOffset, y = (-24).dp + with(density) { yOffset.value.dp })
                    .graphicsLayer {
                        scaleX = scale.value
                        scaleY = scale.value
                    }
                    .size(circleSize)
                    .align(Alignment.TopStart)
                    .clip(CircleShape)
                    .background(Color(0xFF9B004F))
                    .clickable { onItemSelected(items[selectedIndex].label) },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = items[selectedIndex].iconRes),
                    contentDescription = items[selectedIndex].label,
                    modifier = Modifier.size(22.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
        }
    }
}





