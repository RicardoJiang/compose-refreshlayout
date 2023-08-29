package com.zj.refreshlayout

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

internal class SwipeRefreshNestedScrollConnection(
    private val state: SwipeRefreshState,
    private val coroutineScope: CoroutineScope,
    private val onRefresh: () -> Unit,
    private val onLoadMore: () -> Unit,
) : NestedScrollConnection {
    private val dragMultiplier = 0.5f

    var enabledRefresh: Boolean = false
    var enabledLoadMore: Boolean = false
    var refreshTrigger: Float = 0f

    override fun onPreScroll(
        available: Offset,
        source: NestedScrollSource
    ): Offset = when {
        // If swiping isn't enabled, return zero
        !enabledRefresh -> Offset.Zero
        // If we're refreshing, return zero
        state.isRefreshing -> Offset.Zero
        // If the user is swiping up, handle it
        source == NestedScrollSource.Drag && available.y < 0 -> onDragRefresh(available)
        else -> Offset.Zero
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset = when {
        // If swiping isn't enabled, return zero
        !enabledRefresh -> Offset.Zero
        // If we're refreshing, return zero
        state.isRefreshing -> Offset.Zero
        // If the user is swiping down and there's y remaining, handle it
        source == NestedScrollSource.Drag && available.y > 0 -> onDragRefresh(available)
        source == NestedScrollSource.Drag && available.y < 0 -> onDragLoadMore(available)
        else -> Offset.Zero
    }

    private fun onDragRefresh(available: Offset): Offset {
        state.isSwipeInProgress = true

        val newOffset = (available.y * dragMultiplier + state.indicatorOffset).coerceAtLeast(0f)
        val dragConsumed = newOffset - state.indicatorOffset

        return if (dragConsumed.absoluteValue >= 0.5f) {
            coroutineScope.launch {
                state.dispatchScrollDelta(dragConsumed)
            }
            // Return the consumed Y
            Offset(x = 0f, y = dragConsumed / dragMultiplier)
        } else {
            Offset.Zero
        }
    }

    private fun onDragLoadMore(available: Offset): Offset {
        if (enabledLoadMore && !state.isLoadingMore) {
            onLoadMore()
        }
        return Offset.Zero
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        // If we're dragging, not currently refreshing and scrolled
        // past the trigger point, refresh!
        if (!state.isRefreshing && state.indicatorOffset >= refreshTrigger) {
            onRefresh()
        }

        // Reset the drag in progress state
        state.isSwipeInProgress = false

        // Don't consume any velocity, to allow the scrolling layout to fling
        return Velocity.Zero
    }
}