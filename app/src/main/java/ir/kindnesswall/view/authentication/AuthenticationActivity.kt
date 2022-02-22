package ir.kindnesswall.view.authentication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import ir.kindnesswall.BaseActivity
import ir.kindnesswall.R
import ir.kindnesswall.data.local.UserInfoPref
import ir.kindnesswall.databinding.ActivityAuthenticationBinding
import ir.kindnesswall.view.main.MainActivity
import org.koin.android.viewmodel.ext.android.viewModel

class AuthenticationActivity : BaseActivity(), AuthenticationInteractor {
    private val viewModel by viewModel<AuthenticationViewModel>()

    lateinit var binding: ActivityAuthenticationBinding

    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, AuthenticationActivity::class.java))
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

    override fun onStop() {
        super.onStop()
        MainActivity.liveData.value = "getPhoneNumberValue"
    }
}
