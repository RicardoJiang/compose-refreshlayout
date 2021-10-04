package com.zj.composerefreshlayout.customheader

import android.view.animation.AccelerateDecelerateInterpolator
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.zj.refreshlayout.SwipeRefreshState
import kotlinx.coroutines.delay

@Composable
fun BallRefreshHeader(state: SwipeRefreshState) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp), contentAlignment = Alignment.Center
    ) {
        val radius = LocalDensity.current.run {
            8.dp.toPx()
        }
        val mCircleSpacing = LocalDensity.current.run {
            8.dp.toPx()
        }
        val mInterpolator = AccelerateDecelerateInterpolator()
        val ballColor = if (state.isRefreshing) Color(0xff33aaff) else Color(0xffeeeeee)
        val mStartTime = if (state.isRefreshing) System.currentTimeMillis() else 0
        var now by remember {
            mutableStateOf(0L)
        }
        var mScale by remember {
            mutableStateOf(1f)
        }
        LaunchedEffect(key1 = state.isRefreshing) {
            if (state.isRefreshing) {
                while (true) {
                    now = System.currentTimeMillis()
                    delay(60)
                }
            }
        }
        Canvas(modifier = Modifier.wrapContentSize()) {
            for (i in 0 until 3) {
                val time: Long = now - mStartTime - 120 * (i + 1)
                var percent: Float = if (time > 0) time % 750 / 750f else 0f
                percent = mInterpolator.getInterpolation(percent)
                mScale = if (state.isRefreshing) {
                    if (percent < 0.5f) {
                        1 - percent * 2 * 0.7f
                    } else {
                        percent * 2 * 0.7f - 0.4f
                    }
                } else {
                    1f
                }
                scale(mScale, Offset(x = (i - 1) * (mCircleSpacing + radius * 2), y = 0f)) {
                    drawCircle(
                        ballColor,
                        radius,
                        center = Offset(x = (i - 1) * (mCircleSpacing + radius * 2), y = 0f),
                        1f
                    )
                }
            }
        }
    }
}