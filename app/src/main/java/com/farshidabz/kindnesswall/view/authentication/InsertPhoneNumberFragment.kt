package com.farshidabz.kindnesswall.view.authentication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        authenticationInteractor = context as AuthenticationActivity
    }

    override fun configureViewModel() {
    }

    override fun configureViews() {
        binding.sendPhoneNumberTextView.setOnClickListener {
            viewModel.registerUser().observeWithMessage(activity, viewLifecycleOwner, Observer {
                if (!it.isFailed) {
                    authenticationInteractor?.onPhoneNumberSent(binding.sendPhoneNumberTextView)
                }
            })
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }
}
