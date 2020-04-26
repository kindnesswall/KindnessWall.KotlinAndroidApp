package ir.kindnesswall.view.main.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import ir.kindnesswall.BaseFragment
import ir.kindnesswall.R
import ir.kindnesswall.data.local.AppPref
import ir.kindnesswall.data.local.UserInfoPref
import ir.kindnesswall.databinding.FragmentMoreBinding
import ir.kindnesswall.view.main.more.aboutus.AboutUsActivity
import ir.kindnesswall.view.profile.MyProfileActivity
import ir.kindnesswall.view.profile.blocklist.BlockListActivity


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

class MoreFragment : BaseFragment() {
    lateinit var binding: FragmentMoreBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_more, container, false)
        return binding.root
    }

    override fun configureViews() {
        binding.profileTextView.setOnClickListener { context?.let { MyProfileActivity.start(it) } }
        binding.aboutUs.setOnClickListener { context?.let { AboutUsActivity.start(it) } }
        binding.blockedUsers.setOnClickListener { context?.let { BlockListActivity.start(it) } }

        binding.logOut.setOnClickListener {
            showPromptDialog(
                getString(R.string.logout_message),
                positiveButtonText = getString(R.string.yes),
                negativeButtonText = getString(R.string.no),
                onPositiveClickCallback = {
                    UserInfoPref.clear()
                    AppPref.clear()
                    activity?.recreate()
                })
        }
    }

    override fun onResume() {
        super.onResume()
        binding.userInfo = UserInfoPref
    }
}