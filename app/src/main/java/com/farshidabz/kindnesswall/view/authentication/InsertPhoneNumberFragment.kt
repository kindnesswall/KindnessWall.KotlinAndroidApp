package com.farshidabz.kindnesswall.view.authentication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import com.farshidabz.kindnesswall.BaseFragment
import com.farshidabz.kindnesswall.R
import com.farshidabz.kindnesswall.databinding.FragmentInsertPhoneNumberBinding
import org.koin.android.viewmodel.ext.android.sharedViewModel

class InsertPhoneNumberFragment : BaseFragment() {
    private val viewModel by sharedViewModel<AuthenticationViewModel>()
    private var authenticationInteractor: AuthenticationInteractor? = null

    lateinit var binding: FragmentInsertPhoneNumberBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_insert_phone_number,
            container,
            false
        )

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        authenticationInteractor = context as AuthenticationActivity
    }

    override fun configureViews() {
        binding.phoneNumberEditText.doOnTextChanged { text, start, count, after ->
            onPhoneNumberChanged(text)
        }

        binding.sendPhoneNumberTextView.setOnClickListener {
            viewModel.phoneNumber.value = "+98${binding.phoneNumberEditText.text}"
            authenticationInteractor?.onPhoneNumberSent(binding.sendPhoneNumberTextView)
        }

        binding.skipAuthenticationTextView.setOnClickListener { activity?.finish() }

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun onPhoneNumberChanged(text: CharSequence?) {
        if (text.isNullOrEmpty()) {
            binding.sendPhoneNumberTextView.isEnabled = false
        } else {
            if (text.startsWith("0")) {
                binding.sendPhoneNumberTextView.isEnabled = false
            } else if (!text.startsWith("9")) {
                binding.sendPhoneNumberTextView.isEnabled = false
            } else binding.sendPhoneNumberTextView.isEnabled = text.length >= 10
        }
    }
}
