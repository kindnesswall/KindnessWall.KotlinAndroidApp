package com.farshidabz.kindnesswall.view.authentication

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.farshidabz.kindnesswall.BaseActivity
import com.farshidabz.kindnesswall.R
import com.farshidabz.kindnesswall.databinding.ActivityAuthenticationBinding
import com.farshidabz.kindnesswall.view.main.MainActivity
import org.koin.android.viewmodel.ext.android.viewModel

class AuthenticationActivity : BaseActivity(), AuthenticationInteractor {
    private val viewModel by viewModel<AuthenticationViewModel>()

    lateinit var binding: ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_authentication)

        configureViewModel()
        configureViews(savedInstanceState)
    }

    override fun configureViewModel() {
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
        val action =
            InsertVerificationNumberFragmentDirections
                .actionInsertVerificationNumberFragmentToInsertUserNameFragment()

        view.findNavController().navigate(action)
    }

    override fun onAuthenticationComplete(view: View) {
        MainActivity.start(this)
        finish()
    }
}
