package com.farshidabz.kindnesswall.data.local

import com.chibatching.kotpref.KotprefModel

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
object AppPref : KotprefModel() {
    var currentLocale by stringPref("fa")
    var isAppInForeground by booleanPref(false)
}