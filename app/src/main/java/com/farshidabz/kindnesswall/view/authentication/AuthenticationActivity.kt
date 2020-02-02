package com.farshidabz.kindnesswall.view.authentication

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.farshidabz.kindnesswall.BaseActivity
import com.farshidabz.kindnesswall.R
import com.farshidabz.kindnesswall.data.local.UserInfoPref
import com.farshidabz.kindnesswall.databinding.ActivityAuthenticationBinding
import org.koin.android.viewmodel.ext.android.viewModel

class AuthenticationActivity : BaseActivity(), AuthenticationInteractor {
    private val viewModel by viewModel<AuthenticationViewModel>()

    lateinit var binding: ActivityAuthenticationBinding

    companion object {
        @JvmStatic
        fun start(context: Context) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_authentication)

        configureViews(savedInstanceState)
    }

    override fun configureViews(savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this
    }

    override fun onPhoneNumberSent(view: View) {
        val action =
            InsertPhoneNumberFragmentDirections
                .actionInsertPhoneNumberFragmentToInsertVerificationNumberFragment()

        view.findNavController().navigate(action)
    }

    override fun onVerificationSent(view: View) {
        if (UserInfoPref.name.isEmpty()) {
            val action =
                InsertVerificationNumberFragmentDirections
                    .actionInsertVerificationNumberFragmentToInsertUserNameFragment()

            view.findNavController().navigate(action)
        } else {
            finish()
        }
    }

    override fun onAuthenticationComplete(view: View) {
        finish()
    }
}
