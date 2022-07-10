package ir.kindnesswall.view.test

import androidx.compose.runtime.Composable
import ir.kindnesswall.R

typealias ComposableFun = @Composable () -> Unit

sealed class TabItem(var icon: Int, var title: String, var screen: ComposableFun) {
    object Booked : TabItem(R.drawable.ic_hourglass_top, "Booked", { BookedScreen() })
    object Received : TabItem(R.drawable.ic_received, "Received", { ReceivedScreen() })
}