package ir.kindnesswall.utils

import android.view.View


/**
 * Created by farshid.abazari since 2019-11-26
 *
 * Usage:
 *
 * Useful parameter:
 *
 */

interface OnItemClickListener : View.OnClickListener{
    fun onItemClicked(position: Int, obj: Any? = null)

    override fun onClick(p0: View?) {
    }
}