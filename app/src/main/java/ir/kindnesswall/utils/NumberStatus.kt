package ir.kindnesswall.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.observe
import ir.kindnesswall.data.local.UserInfoPref
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.view.main.addproduct.SubmitGiftViewModel
import kotlinx.android.synthetic.main.activity_submit_gift.*
import org.koin.android.viewmodel.ext.android.viewModel

class NumberStatus(
    val viewModel: SubmitGiftViewModel,
    val none: RadioButton,
    val charity: RadioButton,
    val all: RadioButton
) {
    var shPref: SharedPreferences? = null
    val keyName = "nameKey"
    var sEdite: SharedPreferences.Editor? = null
    var context: Context? = null
    fun getShowNumberStatus(context: Context) {
        this.context = context
        shPref = context.getSharedPreferences(UserInfoPref.MyPref, Context.MODE_PRIVATE)
        sEdite = shPref!!.edit()
        if (shPref!!.contains(keyName)) {
            fromSharedPreferences()
        } else {
            fromServer()
        }
    }

    private fun fromSharedPreferences() {
        when (shPref!!.getString(keyName, null)) {
            "none" -> {
                none.isChecked = true
            }
            "charity" -> {
                charity.isChecked = true
            }
            "all" -> {
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
                    if (it.data?.setting == null) {
                        none.isChecked = true
                        editSharedPreferences("none")
                    } else {
                        when (it.data!!.setting) {
                            "none" -> {
                                none.isChecked = true
                            }
                            "charity" -> {
                                charity.isChecked = true
                            }
                            "all" -> {
                                all.isChecked = true
                            }
                        }
                        editSharedPreferences(it.data!!.setting.toString())

                    }


                }
                CustomResult.Status.ERROR -> {

                }
            }
        }
    }
    fun editSharedPreferences(value:String){
        sEdite!!.putString(keyName, value)
        sEdite!!.apply()
    }
}