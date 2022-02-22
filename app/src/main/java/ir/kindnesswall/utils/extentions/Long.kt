package ir.kindnesswall.utils.extentions

import java.util.*


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

fun Long?.inSameDay(date: Long?): Boolean {
    if (this == null) return false
    if (date == null) return false

    return Calendar.getInstance().apply {
        timeInMillis = this@inSameDay
    }.get(Calendar.DAY_OF_MONTH) == Calendar.getInstance().apply {
        timeInMillis = date
    }.get(Calendar.DAY_OF_MONTH)

}

fun Long.getHourOfDay(): String {
    return Calendar.getInstance().apply {
        timeInMillis = this@getHourOfDay
    }.get(Calendar.HOUR_OF_DAY).toString()
}

fun Long.getDayOfMonth(): String {
    return Calendar.getInstance().apply {
        timeInMillis = this@getDayOfMonth
    }.get(Calendar.DAY_OF_MONTH).toString()
}