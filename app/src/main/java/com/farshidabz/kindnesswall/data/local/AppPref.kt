package com.farshidabz.kindnesswall.data.local

import com.chibatching.kotpref.KotprefModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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

    private var recentSearch by stringPref("")

    fun setRecentSearch(searches: ArrayList<String>) {
        val type = object : TypeToken<List<String>>() {}.type

        if (searches.size > 20) {
            searches.removeAt(0)
        }

        recentSearch = Gson().toJson(searches, type).reversed()
    }

    fun addRecentSearch(search: String) {
        val type = object : TypeToken<List<String>>() {}.type

        if (recentSearch == null || recentSearch.isEmpty()) {
            val searches = arrayListOf(search)
            recentSearch = Gson().toJson(searches, type)
        } else {
            val searches = getRecentSearch()!!
            for (item in searches) {
                if (item == search)
                    return
            }

            setRecentSearch(getRecentSearch()!!.apply { add(search) })
        }
    }

    fun getRecentSearch(): ArrayList<String>? {
        if (recentSearch.isNullOrEmpty()) {
            return null
        }

        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(recentSearch, type)
    }
}