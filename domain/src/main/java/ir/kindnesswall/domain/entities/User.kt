package ir.kindnesswall.domain.entities

import android.content.Context
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
        "خیریه"
      } else {
        "نام کاربری"
      }
    }

    return name!!
  }
}
