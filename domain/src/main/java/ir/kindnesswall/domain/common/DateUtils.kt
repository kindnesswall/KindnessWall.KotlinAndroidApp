package ir.kindnesswall.domain.common

import java.text.SimpleDateFormat
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
fun Date.toSimpleFormat() =
  SimpleDateFormat("yyyy-MM-dd", Locale(AppPref.currentLocale)).format(this)

fun Date.toSimpleDotFormat() =
  SimpleDateFormat("dd.MM.yyyy", Locale(AppPref.currentLocale)).format(this)

fun Date.toSimpleSlashFormat() =
  SimpleDateFormat("dd/MM/yyyy", Locale(AppPref.currentLocale)).format(this)


fun Date?.getHoursOfDay(): Int {
  if (this == null) return 0
  val calendar: Calendar = Calendar.getInstance()
  calendar.timeInMillis = this.time
  return calendar.get(Calendar.HOUR_OF_DAY)
}

fun Date?.removeTime() {
  if (this == null) return

  val calendar = Calendar.getInstance().apply {
    timeInMillis = this@removeTime.time
  }

  calendar.set(Calendar.HOUR_OF_DAY, 0)
  calendar.set(Calendar.MINUTE, 0)
  calendar.set(Calendar.SECOND, 0)
  calendar.set(Calendar.MILLISECOND, 0)

  this.time = calendar.timeInMillis
}

fun Date.getJustDate(): Date {
  val calendar = Calendar.getInstance()
  calendar.timeInMillis = this.time
  calendar.set(Calendar.HOUR_OF_DAY, 0)
  calendar.set(Calendar.MINUTE, 0)
  calendar.set(Calendar.SECOND, 0)
  calendar.set(Calendar.MILLISECOND, 0)

  return Date().apply { time = calendar.timeInMillis }
}

fun Date?.toIsoFormat(): String {
  this?.let {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.UK)
    return sdf.format(it)
  }

  return ""
}

fun Date?.isToday(): Boolean {
  if (this == null) return false

  val calendar = Calendar.getInstance().apply {
    timeInMillis = this@isToday.time
  }

  val targetCalendar = Calendar.getInstance()

  val day = calendar[Calendar.DAY_OF_MONTH]
  val targetDay = targetCalendar[Calendar.DAY_OF_MONTH]

  val month = calendar[Calendar.MONTH]
  val targetMonth = targetCalendar[Calendar.MONTH]

  val year = calendar[Calendar.YEAR]
  val targetYear = targetCalendar[Calendar.YEAR]

  return day == targetDay && month == targetMonth && year == targetYear
}

fun Date?.getHourAndMinute(): String {
  if (this == null) {
    return ""
  }

  return SimpleDateFormat("HH:mm", Locale(AppPref.currentLocale)).format(this).toString()
}
