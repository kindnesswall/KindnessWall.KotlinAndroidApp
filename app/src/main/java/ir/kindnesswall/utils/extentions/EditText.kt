package ir.kindnesswall.utils.extentions

import android.content.Context
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


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

/**
 * a function to showing keyboard programmatically
 */
fun EditText.showKeyboard() {
    this.requestFocus()
    val keyboard =
        this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    keyboard.showSoftInput(this, 0)
}

fun EditText.hideKeyboard() {
    this.requestFocus()
    val keyboard =
        this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    keyboard.showSoftInput(this, 0)
}

fun EditText.onDone(callback: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            callback.invoke()
            true
        }
        false
    }
}