package com.zj.composerefreshlayout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@Composable
fun ClassicRefreshHeader(state: SwipeRefreshState, refreshTriggerDistance: Dp) {
    Text(
        text = "正在刷新",
        color = Color.White,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h5,
        modifier = Modifier
            .offset {
                IntOffset(0, -150)
            }
            .fillMaxWidth()
            .clipToBounds()
            .height(50.dp)
            .background(Color.Blue)
    )
}