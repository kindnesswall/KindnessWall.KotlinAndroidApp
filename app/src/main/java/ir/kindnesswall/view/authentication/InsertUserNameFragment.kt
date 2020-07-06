package ir.kindnesswall.view.authentication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import ir.kindnesswall.BaseFragment
import ir.kindnesswall.R
import ir.kindnesswall.data.local.UserInfoPref
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.databinding.FragmentInsertUsernameBinding
import ir.kindnesswall.utils.widgets.NoInternetDialogFragment
import org.koin.android.viewmodel.ext.android.sharedViewModel

class InsertUserNameFragment : BaseFragment() {

    private val viewModel by sharedViewModel<AuthenticationViewModel>()
    private var authenticationInteractor: AuthenticationInteractor? = null

    lateinit var binding: FragmentInsertUsernameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_insert_username, container, false)

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        authenticationInteractor = context as AuthenticationActivity
    }

    override fun configureViews() {
        binding.sendUsernameTextView.setOnClickListener {
            if (binding.userNameEditText.text.isEmpty()) {
                showToastMessage(getString(R.string.insert_user_name))
            } else {
                updateUserProfile()
            }
        }

        binding.skipAuthenticationTextView.setOnClickListener { activity?.finish() }
    }

    private fun updateUserProfile() {
        viewModel.updateUserProfile(
            binding.userNameEditText.text.toString(),
            UserInfoPref.image
        ).observe(viewLifecycleOwner) {
            when (it.status) {
                CustomResult.Status.LOADING -> {
                    showProgressDialog()
                }

                CustomResult.Status.SUCCESS -> {
                    dismissProgressDialog()
                    authenticationInteractor?.onAuthenticationComplete(binding.sendUsernameTextView)
                }
                else -> {
                    if (it.errorMessage?.message!!.contains("Unable to resolve host")) {
                        NoInternetDialogFragment().display(childFragmentManager) {
                            updateUserProfile()
                        }
                    } else {
                        showToastMessage(getString(R.string.please_try_again))
                    }
                }
            }
        }
    }
}
