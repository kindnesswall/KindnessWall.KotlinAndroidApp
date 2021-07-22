package ir.kindnesswall.view.authentication

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.google.firebase.iid.FirebaseInstanceId
import ir.kindnesswall.BaseFragment
import ir.kindnesswall.R
import ir.kindnesswall.data.local.AppPref
import ir.kindnesswall.data.local.UserInfoPref
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.databinding.FragmentInsertVerificationNumberBinding
import ir.kindnesswall.utils.widgets.NoInternetDialogFragment
import ir.kindnesswall.view.giftdetail.GiftDetailActivity
import org.koin.android.viewmodel.ext.android.sharedViewModel

class InsertVerificationNumberFragment : BaseFragment() {
    companion object {
        var login: Boolean? = null
        var isAdmin: Boolean =false
    }

    private var couldResendCode: Boolean = false

    private val viewModel by sharedViewModel<AuthenticationViewModel>()
    private var authenticationInteractor: AuthenticationInteractor? = null

    lateinit var binding: FragmentInsertVerificationNumberBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_insert_verification_number,
                container,
                false
            )

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        authenticationInteractor = context as AuthenticationActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerUser()
    }

    override fun configureViews() {
        binding.sendVersificationTextView.setOnClickListener {
            loginUser()
        }

        binding.verificationCodeEditText.doOnTextChanged { text, _, _, _ ->
            binding.errorPhoneNumberTextView.visibility = View.GONE
            when {
                text.isNullOrEmpty() -> binding.sendVersificationTextView.isEnabled = false
                text.length < 5 -> binding.sendVersificationTextView.isEnabled = false
                else -> binding.sendVersificationTextView.isEnabled = true
            }
        }

        binding.sendVerificationCodeAgainTextView.setOnClickListener {
            if (couldResendCode) {
                registerUser()
                binding.sendVerificationCodeAgainTextView.visibility = View.GONE
            }
        }

        binding.skipAuthenticationTextView.setOnClickListener { activity?.finish() }
    }

    private fun registerUser() {
        viewModel.registerUser().observe(viewLifecycleOwner) {
            when (it.status) {
                CustomResult.Status.SUCCESS -> {
                    dismissProgressDialog()
                    startTimer()
                }
                CustomResult.Status.LOADING -> {
                    showProgressDialog()
                }
                CustomResult.Status.ERROR -> {
                    if (it.errorMessage?.message!!.contains("Unable to resolve host")) {
                        NoInternetDialogFragment().display(childFragmentManager) {
                            registerUser()
                        }
                    } else {
                        showToastMessage(getString(R.string.please_try_again))
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun startTimer() {
        couldResendCode = false
        binding.countDownNumberTextView.text = "60:00"

        object : CountDownTimer(1000 * 60, 1000) {
            override fun onFinish() {
                couldResendCode = true
                binding.sendVerificationCodeAgainTextView.visibility = View.VISIBLE
            }

            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                binding.countDownNumberTextView.text = "00:${millisUntilFinished / 1000}"
            }
        }.start()
    }

    private fun loginUser() {
        login = false
        viewModel.loginUser(binding.verificationCodeEditText.text.toString())
            .observe(viewLifecycleOwner) {
                when (it.status) {
                    CustomResult.Status.SUCCESS -> {
                        UserInfoPref.bearerToken = it.data?.token?.token ?: ""
                        UserInfoPref.id = it.data?.token?.id ?: 0
                        UserInfoPref.userId = it.data?.token?.userID ?: 0
                        UserInfoPref.isAdmin = it.data?.isAdmin ?: false
                        UserInfoPref.isCharity = it.data?.isCharity ?: false
                        getUserProfile()
                        isAdmin = it.data?.isAdmin!!
                        if (GiftDetailActivity.LoginFlag.equals("GiftDetailActivity")) {
                            login = true
                        }
                    }
                    CustomResult.Status.LOADING -> {
                        showProgressDialog()
                    }
                    CustomResult.Status.ERROR -> {
                        binding.errorPhoneNumberTextView.visibility = View.VISIBLE
                        login = false
                    }
                }
            }
    }

    private fun getUserProfile() {
        viewModel.getUserProfile().observe(viewLifecycleOwner) {
            when (it.status) {
                CustomResult.Status.SUCCESS -> {
                    dismissProgressDialog()
                    FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { result ->
                        if (result.isSuccessful) {
                            val token = result.result?.token.toString()
                            if (token.isNotEmpty()) {
                                UserInfoPref.fireBaseToken = token
                                AppPref.shouldUpdatedFireBaseToken = true
                                authenticationInteractor?.onVerificationSent(binding.sendVersificationTextView)
                            } else {
                                authenticationInteractor?.onVerificationSent(binding.sendVersificationTextView)
                            }
                        } else {
                            authenticationInteractor?.onVerificationSent(binding.sendVersificationTextView)
                        }
                    }.addOnFailureListener {
                        authenticationInteractor?.onVerificationSent(binding.sendVersificationTextView)
                    }
                }

                CustomResult.Status.LOADING -> {
                    showProgressDialog()
                }

                CustomResult.Status.ERROR -> {
                    showToastMessage("")
                }
            }
        }
    }
}
