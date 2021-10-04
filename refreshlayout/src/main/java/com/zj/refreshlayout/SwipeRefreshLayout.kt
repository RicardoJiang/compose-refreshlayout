package com.zj.refreshlayout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.zIndex
import com.zj.refreshlayout.header.ClassicRefreshHeader

@Composable
fun SwipeRefreshLayout(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    swipeStyle: SwipeRefreshStyle = SwipeRefreshStyle.Translate,
    swipeEnabled: Boolean = true,
    refreshTriggerRate: Float = 1f, //刷新生效高度与refreshHeader高度的比例
    maxDragRate: Float = 2.5f, //最大刷新距离与refreshHeader高度的比例
    indicator: @Composable (state: SwipeRefreshState) -> Unit = {
        ClassicRefreshHeader(it)
    },
    content: @Composable () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val updatedOnRefresh = rememberUpdatedState(onRefresh)
    var indicatorHeight by remember {
        mutableStateOf(1)
    }
    val refreshTrigger = indicatorHeight * refreshTriggerRate
    val maxDrag = indicatorHeight * maxDragRate
    val state = rememberSwipeRefreshState(isRefreshing, refreshTrigger, maxDrag)
    LaunchedEffect(state.isSwipeInProgress, state.isRefreshing) {
        // If there's no swipe currently in progress, animate to the correct resting position
        if (!state.isSwipeInProgress) {
            if (state.isRefreshing) {
                state.animateOffsetTo(refreshTrigger)
            } else {
                state.animateOffsetTo(0f)
            }
        }
    }

    // Our nested scroll connection, which updates our state.
    val nestedScrollConnection = remember(state, coroutineScope) {
        SwipeRefreshNestedScrollConnection(state, coroutineScope) {
            // On refresh, re-dispatch to the update onRefresh block
            updatedOnRefresh.value.invoke()
        }
    }.apply {
        this.enabled = swipeEnabled
        this.refreshTrigger = refreshTrigger
    }

    Box(
        modifier
            .nestedScroll(connection = nestedScrollConnection)
    ) {
        Box(modifier = Modifier
            .onGloballyPositioned {
                indicatorHeight = it.size.height
            }
            .offset {
                getHeaderOffset(swipeStyle, state, indicatorHeight)
            }
            .zIndex(getHeaderZIndex(swipeStyle))
        ) {
            indicator(state)
        }
        Box(modifier = Modifier.offset {
            getContentOffset(swipeStyle, state)
        }) {
            content()
        }
    }
}

private fun getHeaderZIndex(style: SwipeRefreshStyle): Float {
    return if (style == SwipeRefreshStyle.FixedFront || style == SwipeRefreshStyle.FixedContent) {
        1f
    } else {
        0f
    }
}

private fun getHeaderOffset(
    style: SwipeRefreshStyle,
    state: SwipeRefreshState,
    indicatorHeight: Int
): IntOffset {
    return when (style) {
        SwipeRefreshStyle.Translate -> {
            IntOffset(0, state.indicatorOffset.toInt() - indicatorHeight)
        }
        SwipeRefreshStyle.FixedBehind, SwipeRefreshStyle.FixedFront -> {
            IntOffset(0, 0)
        }
        else -> {
            IntOffset(0, state.indicatorOffset.toInt() - indicatorHeight)
        }
    }
}

private fun getContentOffset(
    style: SwipeRefreshStyle,
    state: SwipeRefreshState
): IntOffset {
    return when (style) {
        SwipeRefreshStyle.Translate -> {
            IntOffset(0, state.indicatorOffset.toInt())
        }
        SwipeRefreshStyle.FixedBehind -> {
            IntOffset(0, state.indicatorOffset.toInt())
        }
        else -> {
            IntOffset(0, 0)
        }
    }
}