package ir.kindnesswall.view.test

import androidx.compose.runtime.Composable
import ir.kindnesswall.R

typealias ComposableFun = @Composable () -> Unit

sealed class TabItem(var icon: Int, var title: String, var screen: ComposableFun) {
    object Booked : TabItem(R.drawable.ic_hourglass_top, "در انتظار جمع آوری", { BookedScreen() })
    object Received : TabItem(R.drawable.ic_received, "دریافت شده", { ReceivedScreen() })
}