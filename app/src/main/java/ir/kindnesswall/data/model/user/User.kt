package ir.kindnesswall.data.model.user

import android.content.Context
import ir.kindnesswall.R

data class User(
    var id: Long = 0,
    var name: String? = null,
    var phoneNumber: String? = null,
    var image: String? = null,
    var isCharity: Boolean? = false,
    var charityName: String? = null
) {
    fun getTitle(context: Context): String {
        if (name.isNullOrEmpty()) {
            return if (isCharity == true) {
                context.getString(R.string.tabbar_charity)
            } else {
                context.getString(R.string.username)
            }
        }

        return name!!
    }
}