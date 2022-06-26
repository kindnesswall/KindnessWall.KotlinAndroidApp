package ir.kindnesswall.utils

import com.chibatching.kotpref.KotprefModel
import ir.kindnesswall.domain.entities.User

/**
 * Created by Farshid since 25/10/19
 *
 * Usage: User information shared pref holder
 *
 * How to call: just get or set each variable you want
 * @sample {UserInfoPref.setUser(@User)}
 * @sample {UserInfoPref.id = "id", var id = UserInfoPref.id}
 *
 * */
object UserInfoPref : KotprefModel(

) {
  var fireBaseToken by stringPref("")

  var id by longPref(0)
  var userId by longPref(0)

  var name by stringPref("")
  var charityName by stringPref("")
  var phoneNumber by stringPref("")
  var image by stringPref("")

  var isCharity by booleanPref(false)
  var isAdmin by booleanPref(false)

  var bearerToken by stringPref("")

  fun setUser(user: User?) {
    if (user == null) {
      return
    }

    id = user.id
    userId = user.id

    name = user.name ?: ""
    charityName = user.charityName ?: ""

    phoneNumber = user.phoneNumber ?: ""
    image = user.image ?: ""

    isCharity = user.isCharity ?: false

    isAdmin = user.isAdmin ?: false
  }

  fun getPersianPhoneNumber() =
    phoneNumber.replace("0", "۰")
      .replace("1", "۱")
      .replace("2", "۲")
      .replace("3", "۳")
      .replace("4", "۴")
      .replace("5", "۵")
      .replace("6", "۶")
      .replace("7", "۷")
      .replace("8", "۸")
      .replace("9", "۹")


  fun getUser() = User(userId, name, phoneNumber, image, isCharity, isAdmin, charityName)
}