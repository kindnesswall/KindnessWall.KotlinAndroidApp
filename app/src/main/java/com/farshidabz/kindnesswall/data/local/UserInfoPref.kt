package com.farshidabz.kindnesswall.data.local

import com.chibatching.kotpref.KotprefModel
import com.farshidabz.kindnesswall.annotation.Gender
import com.farshidabz.kindnesswall.data.model.user.User

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

    var id by stringPref("")
    var aboutMe by stringPref("")
    var bearerToken by stringPref("")
    var firstName by stringPref("")
    var lastName by stringPref("")
    var fullName by stringPref("")
    var birthday by stringPref("")
    var age by stringPref("")
    var rate by floatPref(0f)
    var rateCount by intPref(0)
    var gender by stringPref(Gender.MALE)
    var email by stringPref("mahmoud.daryaban@gmail.com")
    var balance by floatPref(0.0f)
    var mobileNumber by stringPref("")

    var firebaseAuthenticationToken by stringPref("")

    fun setUser(user: User?) {
        if (user == null) {
            return
        }

        id = user.id

        firstName = user.firstName
        lastName = user.lastName
        fullName = user.fullName
        birthday = user.birthday?.time.toString()
        aboutMe = user.about
        mobileNumber = user.mobileNumber

        user.gender?.let {
            gender = it
        }

        rate = user.rating.toFloat()
        rateCount = user.rateCount
        age = user.getAge()
    }
}