package ir.kindnesswall.utils.extentions

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
        Patterns.PHONE.matcher(this).matches() && this.length > 9
    } else false
}

/**
 * A method for validate email
 */
fun String.isValidEmail(): Boolean {
    return if (!TextUtils.isEmpty(this)) {
        Patterns.EMAIL_ADDRESS.matcher(this).matches() && this.length > 3
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
        isNumeric() && this.length == 4
    } else false
}

/**
 * A method for validate a string is only number or not
 */
fun String.isNumeric(): Boolean = this.all(Char::isDigit)

/**
 * A method to format country code to
 */
fun String.zeroFormatCountryCode(): String {
    return "00$this"
}

fun String.plusFormatCountryCode(): String {
    return "+$this"
}

fun String.colorString(string: String, color: Int): CharSequence {
    var index: Int = this.indexOf(string)

    val spannedString = SpannableStringBuilder(this)
    spannedString.setSpan(
        ForegroundColorSpan(color),
        index,
        index + string.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    return spannedString
}

fun String.boldString(string: String): CharSequence {
    val index: Int = this.indexOf(string)

    val spannedString = SpannableStringBuilder(this)
    spannedString.setSpan(
        android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
        index,
        index + string.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    return spannedString
}

fun String.toBold(): CharSequence {
    val index: Int = this.indexOf(this)

    val spannedString = SpannableStringBuilder(this)
    spannedString.setSpan(
        android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
        index,
        index + this.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    return spannedString
}

fun String.addUnderLineHtml(): String {
    return "<u>$this</u>"
}

fun String.boldAndColorString(string: String, color: Int = Color.BLACK): CharSequence {
    val index: Int = this.indexOf(string)

    val spannedString = SpannableStringBuilder(this)
    spannedString.setSpan(
        android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
        index,
        index + string.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    spannedString.setSpan(
        ForegroundColorSpan(color),
        index,
        index + string.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    return spannedString
}

fun String.boldAndColorStringHtml(string: String = this, color: Int = Color.BLACK): String {
    val index: Int = this.indexOf(string)

    val startTag = "<b><font color=\"" + String.format("#%06X", 0xFFFFFF and color) + "\">"
    val endTag = "</font></b>"

    val formattedText = this.replace(string, startTag + string + endTag)
    return formattedText
}

fun String.isUnicode(): Boolean = this.any { it.code > 255 }

fun String.intOrString(): Any {
    val v = toIntOrNull()
    return when (v) {
        null -> this
        else -> v
    }
}

/**
 * This method converts a number string to persian equivalent.
 *
 * @return Persian number string
 * @throws NumberFormatException occurs when the "numberStr" is invalid.
 */
@Throws(NumberFormatException::class)
fun String.persianizeNumberString(): String {
    var persianNumberStr = ""
    for (element in this) {
        val numberUnicode = element.toInt()
        val persianUnicode: Int
        persianUnicode = when (numberUnicode) {
            in 48..57 -> numberUnicode + 1728   // The digit character is Latin
            in 1632..1641 -> numberUnicode + 144    // The digit is Arabic
            in 1776..1785 -> numberUnicode  // The digit character is Persian
            '.'.toInt() -> '/'.toInt()
            else -> throw NumberFormatException("\"numberStr\" has an invalid digit character") // The digit character is invalid
        }
        persianNumberStr += persianUnicode.toChar()
    }
    return persianNumberStr
}

/**
 * This method gets a string and converts all digits in it to Persian equivalent.
 *
 * @return the processed string that don't have any non-persian digit character.
 */
fun String.persianizeDigitsInString(): String {
    var persianizedStr = ""
    for (element in this) {
        val unicode = element.toInt()
        val persianizedUnicode: Int
        if (unicode in 48..57) {
            // The character is Latin digit
            persianizedUnicode = unicode + 1728
        } else if (unicode in 1632..1641) {
            // The character is Arabic digit
            persianizedUnicode = unicode + 144
        } else {
            persianizedUnicode = unicode
        }
        persianizedStr += persianizedUnicode.toChar()
    }
    return persianizedStr
}
