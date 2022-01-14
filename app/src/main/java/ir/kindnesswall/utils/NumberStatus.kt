package ir.kindnesswall.utils

import android.content.Context
import android.widget.RadioButton
import androidx.lifecycle.LifecycleOwner
import ir.kindnesswall.data.local.UserPreferences
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.model.PhoneVisibility
import ir.kindnesswall.view.main.addproduct.SubmitGiftViewModel

class NumberStatus(
    val viewModel: SubmitGiftViewModel,
    val none: RadioButton,
    val charity: RadioButton,
    val all: RadioButton
) {
    var context: Context? = null
    fun getShowNumberStatus(context: Context) {
        this.context = context
        val localPhoneVisibilityStatus = UserPreferences.phoneVisibilityStatus
        if (localPhoneVisibilityStatus != null) {
            fromSharedPreferences(localPhoneVisibilityStatus)
        } else {
            fromServer()
        }
    }

    private fun fromSharedPreferences(localPhoneVisibilityStatus: PhoneVisibility) {
        when (localPhoneVisibilityStatus) {
            PhoneVisibility.None -> {
                none.isChecked = true
            }
            PhoneVisibility.JustCharities -> {
                charity.isChecked = true
            }
            PhoneVisibility.All -> {
                all.isChecked = true
            }
        }
    }

    private fun fromServer() {
        viewModel.getPhoneVisibility().observe(context as LifecycleOwner) {
            when (it.status) {
                CustomResult.Status.LOADING -> {
                }
                CustomResult.Status.SUCCESS -> {
                    val visibility = it.data ?: error("null visibility must not be comes here")
                    when (visibility) {
                        PhoneVisibility.None -> none.isChecked = true
                        PhoneVisibility.JustCharities -> charity.isChecked = true
                        PhoneVisibility.All -> all.isChecked = true
                    }
                    UserPreferences.phoneVisibilityStatus = visibility
                }
                CustomResult.Status.ERROR -> {
                }
            }
        }
    }
}