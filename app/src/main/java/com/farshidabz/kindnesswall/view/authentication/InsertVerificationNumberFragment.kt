package com.farshidabz.kindnesswall.view.authentication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.farshidabz.kindnesswall.BaseFragment
import com.farshidabz.kindnesswall.R
import com.farshidabz.kindnesswall.databinding.FragmentInsertVerificationNumberBinding
import org.koin.android.viewmodel.ext.android.sharedViewModel

class InsertVerificationNumberFragment : BaseFragment() {
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

    override fun configureViewModel() {
    }

    override fun configureViews() {
        binding.sendVersificationTextView.setOnClickListener {
            authenticationInteractor?.onVerificationSent(binding.sendVersificationTextView)
        }
    }
}
