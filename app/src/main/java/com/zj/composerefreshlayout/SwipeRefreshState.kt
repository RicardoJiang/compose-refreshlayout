package com.zj.composerefreshlayout

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.MutatorMutex
import androidx.compose.runtime.*

/**
 * Creates a [SwipeRefreshState] that is remembered across compositions.
 *
 * Changes to [isRefreshing] will result in the [SwipeRefreshState] being updated.
 *
 * @param isRefreshing the value for [SwipeRefreshState.isRefreshing]
 */
@Composable
fun rememberSwipeRefreshState(
    isRefreshing: Boolean,
    refreshTrigger: Float
): SwipeRefreshState {
    return remember {
        SwipeRefreshState(
            isRefreshing = isRefreshing,
            refreshTrigger = refreshTrigger
        )
    }.apply {
        this.isRefreshing = isRefreshing
        this.refreshTrigger = refreshTrigger
    }
}


/**
 * A state object that can be hoisted to control and observe changes for [SwipeRefresh].
 *
 * In most cases, this will be created via [rememberSwipeRefreshState].
 *
 * @param isRefreshing the initial value for [SwipeRefreshState.isRefreshing]
 */
@Stable
class SwipeRefreshState(
    isRefreshing: Boolean,
    refreshTrigger: Float
) {
    private val _indicatorOffset = Animatable(0f)
    private val mutatorMutex = MutatorMutex()
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
            _indicatorOffset.snapTo(_indicatorOffset.value + delta)
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
}

enum class RefreshHeaderState {
    PullDownToRefresh,
    Refreshing,
    ReleaseToRefresh
}