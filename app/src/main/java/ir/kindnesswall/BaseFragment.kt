package ir.kindnesswall

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment


/**
 * Created by farshid.abazari since 2019-10-31
 *
 * Usage:
 *
 * How to call:
 *
 * Useful parameter:
 *
 */

@SuppressLint("Registered")
abstract class BaseFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureViews()
    }

    abstract fun configureViews()

    fun showToastMessage(message: String) {
        if (message.isNotEmpty()) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    fun showPromptDialog(
        message: String? = null,
        positiveButtonText: String,
        negativeButtonText: String,
        cancelable: Boolean = true,
        positiveButtonTextColor: Int? = null,
        negativeButtonTextColor: Int? = null,
        onPositiveClickCallback: ((Boolean) -> Unit)? = null,
        onNegativeClickCallback: (() -> Unit)? = null,
        title: String? = null,
        checkBoxMessage: String? = null,
        showCheckBox: Boolean = false
    ) {

        var positiveButtonTextColorValue: Int = positiveButtonTextColor ?: 0
        var negativeButtonTextColorValue: Int = negativeButtonTextColor ?: 0

        if (positiveButtonTextColor == null)
            context?.let {
                positiveButtonTextColorValue = ContextCompat.getColor(it, R.color.primaryTextColor)
            }

        if (negativeButtonTextColor == null)
            context?.let {
                negativeButtonTextColorValue =
                    ContextCompat.getColor(it, R.color.secondaryTextColor)
            }

        if (activity is BaseActivity?)
            (activity as BaseActivity?)?.showPromptDialog(
                title = title,
                messageToShow = message,
                positiveButtonText = positiveButtonText,
                negativeButtonText = negativeButtonText,
                cancelable = cancelable,
                showCheckBox = showCheckBox,
                checkBoxMessage = checkBoxMessage,
                positiveButtonTextColor = positiveButtonTextColorValue,
                negativeButtonTextColor = negativeButtonTextColorValue,
                onPositiveClickCallback = onPositiveClickCallback,
                onNegativeClickCallback = onNegativeClickCallback
            )
    }

    fun dismissPromptDialog() {
        if (activity is BaseActivity?)
            (activity as BaseActivity?)?.dismissPromptDialog()
    }

    fun showProgressDialog() {

    }

    fun dismissProgressDialog() {

    }
}