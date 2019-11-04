package com.farshidabz.kindnesswall.utils.extentions

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.util.Patterns

/**
 * a function for checking a string which is all of characters is a valid phone number or not
 */
/**
 * A method for validate mobile number
 */
fun String.isValidMobileNumber(): Boolean {
    return if (!TextUtils.isEmpty(this)) {
        Patterns.PHONE.matcher(this).matches()&& this.length>9
    } else false
}

/**
 * A method for validate email
 */
fun String.isValidEmail(): Boolean {
    return if (!TextUtils.isEmpty(this)) {
        Patterns.EMAIL_ADDRESS.matcher(this).matches()&& this.length>3
    } else false
}

/**
 * A method for validate a string is a valid name or not
 */
fun String.isValidName(): Boolean {
    return if (!TextUtils.isEmpty(this)) {
        this.isNotEmpty()
    } else false
}

/**
 * A method for validate verification code
 */
fun String.isValidVerificationCode(): Boolean {
    return if (!TextUtils.isEmpty(this)) {
        isNumeric() && this.length==4
    } else false
}

/**
 * A method for validate a string is only number or not
 */
fun String.isNumeric(): Boolean {
    var i = length
    while (--i >= 0) {
        if (!Character.isDigit(this[i])) {
            return false
        }
    }
    return true
}

/**
 * A method to format country code to
 */
fun String.zeroFormatCountryCode(): String {
    return "00$this"
}
fun String.plusFormatCountryCode(): String{
    return "+$this"
}

fun String.colorString(string:String,color:Int):CharSequence
{
    var index:Int = this.indexOf(string)

    val spannedString = SpannableStringBuilder(this)
    spannedString.setSpan(
        ForegroundColorSpan(color),
        index,
        index+string.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

    return spannedString
}

fun String.boldString(string:String):CharSequence
{
    val index:Int = this.indexOf(string)

    val spannedString = SpannableStringBuilder(this)
    spannedString.setSpan(
        android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
        index,
        index+string.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

    return spannedString
}

fun String.toBold():CharSequence
{
    val index:Int = this.indexOf(this)

    val spannedString = SpannableStringBuilder(this)
    spannedString.setSpan(
        android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
        index,
        index+this.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

    return spannedString
}

fun String.addUnderLineHtml():String
{
    return "<u>$this</u>"
}

fun String.boldAndColorString(string:String,color: Int = Color.BLACK):CharSequence
{
    val index:Int = this.indexOf(string)

    val spannedString = SpannableStringBuilder(this)
    spannedString.setSpan(
        android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
        index,
        index+string.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    spannedString.setSpan(
        ForegroundColorSpan(color),
        index,
        index+string.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

    return spannedString
}

fun String.boldAndColorStringHtml(string:String=this,color: Int = Color.BLACK):String
{
    val index:Int = this.indexOf(string)

    val startTag = "<b><font color=\""+ String.format("#%06X", 0xFFFFFF and color)+ "\">"
    val endTag = "</font></b>"

    val formattedText = this.replace(string,startTag+string+endTag)
    return formattedText
}

fun String.isUniCode(): Boolean {
    for (c in this.toCharArray()) {
        if (c.toInt() > 255) return true
    }
    return false
}

fun String.intOrString(): Any {
    val v = toIntOrNull()
    return when(v) {
        null -> this
        else -> v
    }
}