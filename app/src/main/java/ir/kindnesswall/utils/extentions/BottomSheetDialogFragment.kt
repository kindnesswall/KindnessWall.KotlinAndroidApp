package ir.kindnesswall.utils.extentions

import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

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
fun BottomSheetDialogFragment.setBackgroundTransparent() {
    context?.let { context -> (view?.parent as View).setBackgroundColor(ContextCompat.getColor(context,android.R.color.transparent)) }
}