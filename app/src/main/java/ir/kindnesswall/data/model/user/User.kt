package ir.kindnesswall.data.model.user

import android.content.Context
import ir.kindnesswall.R
import java.io.Serializable

data class User(
        var id: Long = 0,
        var name: String? = null,
        var phoneNumber: String? = null,
        var image: String? = null,
        var isCharity: Boolean? = false,
        var isAdmin: Boolean? = false,
        var charityName: String? = null
) : Serializable {
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

    fun getNameWithType(context: Context): String {
        var userName = ""
        name?.let { userName = it }
        isCharity?.takeIf { it }?.also {
            userName = "$userName (${context.getString(R.string.tabbar_charity)})"
        }

        isAdmin?.takeIf { it }?.also {
            userName = "$userName (${context.getString(R.string.admin)})"
        }
        return userName
    }
}