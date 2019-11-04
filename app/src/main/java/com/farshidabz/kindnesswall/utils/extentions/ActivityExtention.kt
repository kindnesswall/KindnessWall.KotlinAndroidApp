package com.farshidabz.kindnesswall.utils.extentions

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager


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

fun Activity.hideKeyboard(){
    val inputMethodManager =
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    if (inputMethodManager.isAcceptingText)
        inputMethodManager.hideSoftInputFromWindow(
            currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
}