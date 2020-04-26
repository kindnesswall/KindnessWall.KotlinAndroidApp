package ir.kindnesswall.utils.extentions

import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager

/**
 * Created by Farshid Abazari since 25/10/19
 *
 * Usage:
 *
 * How to call:
 *
 * Useful parameter:
 *
 */

fun NestedScrollView.betterSmoothScrollToPosition(targetPosition: Int) {

    val maxScroll = 10
    when (this) {
        is LinearLayoutManager -> {
            val topItem = findFirstVisibleItemPosition()
            val distance = topItem - targetPosition
            val anchorItem = when {
                distance > maxScroll -> targetPosition + maxScroll
                distance < -maxScroll -> targetPosition - maxScroll
                else -> topItem
            }
            if (anchorItem != topItem) scrollToPosition(anchorItem)
            post {
                smoothScrollTo(0,targetPosition)
            }
        }
        else -> smoothScrollTo(0,targetPosition)
        }
}