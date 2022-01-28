package ir.kindnesswall.view.main.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ir.kindnesswall.BaseFragment
import ir.kindnesswall.KindnessApplication
import ir.kindnesswall.R
import ir.kindnesswall.data.local.AppPref
import ir.kindnesswall.data.local.UserInfoPref
import ir.kindnesswall.data.model.PhoneVisibility
import ir.kindnesswall.databinding.FragmentMoreBinding
import ir.kindnesswall.utils.extentions.runOrStartAuth
import ir.kindnesswall.utils.openSupportForm
import ir.kindnesswall.view.main.MainActivity
import ir.kindnesswall.view.main.addproduct.SubmitGiftViewModel
import ir.kindnesswall.view.main.more.aboutus.AboutUsActivity
import ir.kindnesswall.view.profile.UserProfileActivity
import ir.kindnesswall.view.profile.blocklist.BlockListActivity
import ir.kindnesswall.view.reviewgift.ReviewGiftsActivity
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * Created by farshid.abazari since 2019-11-07
 *
 * Usage:
 *
 * How to call:
 *
 * Useful parameter:
 *
 */

class MoreFragment() : BaseFragment() {
    lateinit var binding: FragmentMoreBinding

    private val viewModel: SubmitGiftViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_more, container, false)
        MainActivity.liveData.observe(binding.root.context as LifecycleOwner, Observer {
            if (UserInfoPref.bearerToken.isNotEmpty()) {
                viewModel.refreshPhoneVisibility()
            }
        })
        if (UserInfoPref.bearerToken.isNotEmpty()) {
            // TODO because of its mediator livedata (cold observer)
            viewModel.phoneVisibilityLiveData.observe(viewLifecycleOwner) {}
        }

        return binding.root
    }

    override fun configureViews() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.myProfileContainer.setOnClickListener {
            context?.let {
                UserProfileActivity.start(
                    it,
                    UserInfoPref.getUser()
                )
            }
        }
        binding.reviewGiftsContainer.setOnClickListener {
            context?.let {
                ReviewGiftsActivity.start(
                    it
                )
            }
        }
        binding.aboutUs.setOnClickListener { context?.let { AboutUsActivity.start(it) } }
        binding.blockedUsers.setOnClickListener { context?.let { BlockListActivity.start(it) } }
        binding.showNumber.setOnClickListener { openPhoneVisibilityDialog() }
        binding.contactUs.setOnClickListener { openSupportForm(requireContext()) }
        binding.bugReport.setOnClickListener { openSupportForm(requireContext()) }
        binding.suggestions.setOnClickListener { openSupportForm(requireContext()) }
        binding.logInLogOut.setOnClickListener { startAuthOrLogoutPrompt() }
    }

    private fun startAuthOrLogoutPrompt() {
        context?.runOrStartAuth {
            showPromptDialog(
                getString(R.string.logout_message),
                positiveButtonText = getString(R.string.yes),
                negativeButtonText = getString(R.string.no),
                onPositiveClickCallback = {
                    UserInfoPref.clear()
                    AppPref.clear()
                    KindnessApplication.instance.clearContactList()
                    activity?.recreate()
                })
        }
    }

    override fun onResume() {
        super.onResume()
        binding.userInfo = UserInfoPref
    }

    private fun openPhoneVisibilityDialog() {
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.show_number)
            .setSingleChoiceItems(
                arrayOf(
                    getString(R.string.none),
                    getString(R.string.charity),
                    getString(R.string.all),
                ),
                when (viewModel.phoneVisibilityLiveData.value) {
                    PhoneVisibility.None -> 0
                    PhoneVisibility.JustCharities -> 1
                    PhoneVisibility.All -> 2
                    null -> -1
                }
            ) { dialog, which ->
                val item = when (which) {
                    0 -> PhoneVisibility.None
                    1 -> PhoneVisibility.JustCharities
                    2 -> PhoneVisibility.All
                    else -> error("unknown item. $which")
                }
                viewModel.setPhoneVisibility(item)
                dialog.cancel()
            }
            .show()
    }
}