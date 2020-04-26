package ir.kindnesswall.data.local

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

    var shouldUpdatedFireBaseToken by booleanPref(false)

    var currentLocale by stringPref("fa")
    var isAppInForeground by booleanPref(false)
    var countryId by intPref(103)

    private var recentSearch by stringPref("")

    var isOnBoardingShown by booleanPref(false)

    fun setRecentSearch(searches: ArrayList<String>) {
        val type = object : TypeToken<List<String>>() {}.type

        if (searches.size > 20) {
            searches.removeAt(0)
        }

        recentSearch = Gson().toJson(searches, type)
    }

    fun addRecentSearch(search: String) {
        val type = object : TypeToken<List<String>>() {}.type

        if (recentSearch.isNullOrEmpty()) {
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
        val result: List<String> = Gson().fromJson(recentSearch, type)

        if (result.isNullOrEmpty()) {
            return null
        }

        if (result.size == 1) {
            return ArrayList<String>().apply { add(result[0]) }
        }

        return result.reversed() as ArrayList<String>
    }
}