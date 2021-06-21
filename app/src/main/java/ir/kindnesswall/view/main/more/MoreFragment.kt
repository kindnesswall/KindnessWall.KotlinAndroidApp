package ir.kindnesswall.view.main.more

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import ir.kindnesswall.BaseFragment
import ir.kindnesswall.KindnessApplication
import ir.kindnesswall.R
import ir.kindnesswall.data.local.AppPref
import ir.kindnesswall.data.local.UserInfoPref
import ir.kindnesswall.data.repository.UserRepo
import ir.kindnesswall.databinding.FragmentMoreBinding
import ir.kindnesswall.utils.extentions.runOrStartAuth
import ir.kindnesswall.utils.isAppAvailable
import ir.kindnesswall.view.authentication.AuthenticationActivity
import ir.kindnesswall.view.main.charity.addcharity.SubmitCharityActivity
import ir.kindnesswall.view.main.more.aboutus.AboutUsActivity
import ir.kindnesswall.view.main.more.hami.HamiActivity
import ir.kindnesswall.view.main.more.userlist.UserListActivity
import ir.kindnesswall.view.main.reviewcharity.ReviewCharityActivity
import ir.kindnesswall.view.profile.UserProfileActivity
import ir.kindnesswall.view.profile.blocklist.BlockListActivity
import ir.kindnesswall.view.reviewgift.ReviewGiftsActivity
import kotlinx.android.synthetic.main.fragment_more.*
import kotlinx.android.synthetic.main.fragment_more.view.*
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
    var numview : Boolean =true
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
        binding.lifecycleOwner = viewLifecycleOwner

        binding.myProfileContainer.setOnClickListener { context?.let { UserProfileActivity.start(it, UserInfoPref.getUser()) } }
        binding.submitCharityContainer.setOnClickListener { context?.let { SubmitCharityActivity.start(it) } }
        binding.users.setOnClickListener { context?.let { UserListActivity.start(it) } }
        binding.reviewGiftsContainer.setOnClickListener { context?.let { ReviewGiftsActivity.start(it) } }
        binding.reviewCharityContainer.setOnClickListener { context?.let { ReviewCharityActivity.start(it) } }

        binding.none1.setOnClickListener {
            Log.i("5446546546545","test")
        }
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

        binding.showNumber.setOnClickListener {
        if (numview.equals(true)){
            numview = false
            binding.numViewGroup.visibility = View.VISIBLE
        }else{
            numview = true
            binding.numViewGroup.visibility = View.GONE

        }
        }
        binding.contactUs.setOnClickListener { openTelegram() }
        binding.bugReport.setOnClickListener { openTelegram() }
        binding.suggestions.setOnClickListener { openTelegram() }

        binding.hami.setOnClickListener { HamiActivity.start(requireContext()) }

        var shPref: SharedPreferences = view?.context!!.getSharedPreferences(UserInfoPref.MyPref, Context.MODE_PRIVATE);
        val keyName = "nameKey"
        if (shPref.contains(keyName)){
            when(shPref.getString(keyName, null)){
                "none"->{
                    none1.isChecked = true
                }
                "charity"->{
                    charity1.isChecked= true
                }
                "all"->{
                    all1.isChecked= true
                }
            }
        }


        binding.logInLogOut.setOnClickListener {
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
    }

    private fun openTelegram() {
        context?.let {
            val packageName = "org.telegram.messenger"
            if (isAppAvailable(it, packageName)) {
                val telegramIntent = Intent(Intent.ACTION_VIEW)
                telegramIntent.data = Uri.parse("http://telegram.me/Kindness_Wall_Admin")
                startActivity(telegramIntent)
            } else {
                showToastMessage(getString(R.string.install_telegram))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.userInfo = UserInfoPref
    }



}