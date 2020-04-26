package ir.kindnesswall.data.local

import com.chibatching.kotpref.KotprefModel
import ir.kindnesswall.data.model.user.User

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
object UserInfoPref : KotprefModel() {

    var fireBaseToken by stringPref("")

    var id by longPref(0)
    var userId by longPref(0)

    var name by stringPref("")
    var phoneNumber by stringPref("")
    var image by stringPref("")

    var isCharity by booleanPref(false)

    var bearerToken by stringPref("")

    fun setUser(user: User?) {
        if (user == null) {
            return
        }

        id = user.id

        name = user.name ?: ""
        phoneNumber = user.phoneNumber ?: ""
        image = user.image ?: ""

        isCharity = user.isCharity ?: false
    }
}