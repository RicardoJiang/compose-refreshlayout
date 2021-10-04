package com.zj.refreshlayout

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.MutatorMutex
import androidx.compose.runtime.*
import java.lang.Math.*
import kotlin.math.pow

@Composable
internal fun rememberSwipeRefreshState(
    isRefreshing: Boolean,
    refreshTrigger: Float,
    maxDrag: Float
): SwipeRefreshState {
    return remember {
        SwipeRefreshState(
            isRefreshing = isRefreshing,
            refreshTrigger = refreshTrigger,
            maxDrag = maxDrag
        )
    }.apply {
        this.isRefreshing = isRefreshing
        this.refreshTrigger = refreshTrigger
        this.maxDrag = maxDrag
    }
}


@Stable
class SwipeRefreshState(
    isRefreshing: Boolean,
    refreshTrigger: Float,
    maxDrag: Float
) {
    private val _indicatorOffset = Animatable(0f)
    private val mutatorMutex = MutatorMutex()

    /**
     * 最大下拉距离
     */
    var maxDrag: Float by mutableStateOf(maxDrag)
        internal set

    /**
     * 刷新生效距离
     */
    var refreshTrigger: Float by mutableStateOf(refreshTrigger)
        internal set

    /**
     * Whether this [SwipeRefreshState] is currently refreshing or not.
     */
    var isRefreshing: Boolean by mutableStateOf(isRefreshing)
        internal set

    /**
     * Whether a swipe/drag is currently in progress.
     */
    var isSwipeInProgress: Boolean by mutableStateOf(false)
        internal set

    var headerState: RefreshHeaderState by mutableStateOf(RefreshHeaderState.PullDownToRefresh)
        internal set

    /**
     * The current offset for the indicator, in pixels.
     */
    val indicatorOffset: Float get() = _indicatorOffset.value

    internal suspend fun animateOffsetTo(offset: Float) {
        mutatorMutex.mutate {
            _indicatorOffset.animateTo(offset)
            updateHeaderState()
        }
    }

    /**
     * Dispatch scroll delta in pixels from touch events.
     */
    internal suspend fun dispatchScrollDelta(delta: Float) {
        mutatorMutex.mutate(MutatePriority.UserInput) {
            val slingShotOffset = getSlingShotOffset(_indicatorOffset.value + delta, maxDrag)
            _indicatorOffset.snapTo(slingShotOffset)
            updateHeaderState()
        }
    }

    private fun updateHeaderState() {
        headerState = if (isRefreshing) {
            RefreshHeaderState.Refreshing
        } else if (isSwipeInProgress) {
            if (indicatorOffset > refreshTrigger) {
                RefreshHeaderState.ReleaseToRefresh
            } else {
                RefreshHeaderState.PullDownToRefresh
            }
        } else {
            RefreshHeaderState.PullDownToRefresh
        }
    }

    private fun getSlingShotOffset(offsetY: Float, maxOffsetY: Float): Float {
        val offsetPercent = min(1f, offsetY / maxOffsetY)
        val extraOffset = abs(offsetY) - maxOffsetY

        // Can accommodate custom start and slingshot distance here
        val slingshotDistance = maxOffsetY
        val tensionSlingshotPercent = max(
            0f, min(extraOffset, slingshotDistance * 2) / slingshotDistance
        )
        val tensionPercent = (
                (tensionSlingshotPercent / 4) -
                        (tensionSlingshotPercent / 4).pow(2)
                ) * 2
        val extraMove = slingshotDistance * tensionPercent * 2
        return ((slingshotDistance * offsetPercent) + extraMove)
    }
}

enum class RefreshHeaderState {
    PullDownToRefresh,
    Refreshing,
    ReleaseToRefresh
}