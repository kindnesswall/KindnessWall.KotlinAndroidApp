package ir.kindnesswall

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import ir.kindnesswall.data.local.AppPref
import ir.kindnesswall.utils.LocaleHelper


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
abstract class BaseActivity : AppCompatActivity() {


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(
            LocaleHelper.onAttach(
                newBase,
                AppPref.currentLocale
            )
        )
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    abstract fun configureViews(savedInstanceState: Bundle?)

    fun showToastMessage(message: String) {
        if (message.isNotEmpty()) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }

    private var promptDialog: android.app.AlertDialog? = null

    fun showPromptDialog(
        title: String? = null,
        messageToShow: String? = null,
        positiveButtonText: String = getString(R.string.yes),
        negativeButtonText: String = getString(R.string.no),
        positiveButtonTextColor: Int = ContextCompat.getColor(this, R.color.primaryTextColor),
        negativeButtonTextColor: Int = ContextCompat.getColor(this, R.color.secondaryTextColor),
        showPositiveButton: Boolean = true,
        showNegativeButton: Boolean = true,
        cancelable: Boolean = true,
        canceledOnTouchOutside: Boolean = true,
        showCheckBox: Boolean = false,
        onPositiveClickCallback: ((Boolean) -> Unit)? = null,
        onNegativeClickCallback: (() -> Unit)? = null,
        onDismissCallback: (() -> Unit)? = null,
        checkBoxMessage: String? = null
    ) {
        var checkBoxChecked = false

        if (promptDialog == null) {
//            var checkBoxView: View? = null
//            if (showCheckBox) {
//                checkBoxView = View.inflate(this, R.layout.prompt_dialog_with_check_box, null)
//                val checkBox = checkBoxView.findViewById(R.id.alertDialogCheckBox) as CheckBox
//                checkBox.text =
//                    checkBoxMessage ?: getString(R.string.request_reject_popup__dont_show_again_box)
//
//                checkBox.setOnCheckedChangeListener { _, b ->
//                    checkBoxChecked = b
//                }
//            }

            promptDialog = android.app.AlertDialog.Builder(this).apply {
                this.setMessage(messageToShow)
                this.setTitle(title)
                if (showPositiveButton)
                    this.setPositiveButton(positiveButtonText) { dialog, _ ->
                        onPositiveClickCallback?.invoke(checkBoxChecked)
                        dialog.dismiss()
                    }
                if (showNegativeButton)
                    this.setNegativeButton(negativeButtonText) { dialog, _ ->
                        if (onNegativeClickCallback == null) {
                            dialog.dismiss()
                            promptDialog = null
                        } else {
                            onNegativeClickCallback.invoke()
                        }
                    }
                this.setOnDismissListener {
                    promptDialog = null
                    onDismissCallback?.invoke()
                }
                this.setOnCancelListener {
                    promptDialog = null
                    onDismissCallback?.invoke()
                }
            }.also {
                if (showCheckBox) {
//                    it.setView(checkBoxView)
                }
            }.create()

            promptDialog?.show()
            promptDialog?.setCancelable(cancelable)
            promptDialog?.setCanceledOnTouchOutside(canceledOnTouchOutside)
            if (showNegativeButton)
                promptDialog?.getButton(android.app.AlertDialog.BUTTON_NEGATIVE)?.setTextColor(
                    negativeButtonTextColor
                )
            if (showPositiveButton)
                promptDialog?.getButton(android.app.AlertDialog.BUTTON_POSITIVE)?.setTextColor(
                    positiveButtonTextColor
                )
        }
    }

    fun dismissPromptDialog() {
        promptDialog?.dismiss()
        promptDialog = null
    }
    
    fun showProgressDialog() {

    }

    fun dismissProgressDialog() {

    }
}