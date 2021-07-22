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
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import ir.kindnesswall.BaseFragment
import ir.kindnesswall.KindnessApplication
import ir.kindnesswall.R
import ir.kindnesswall.data.local.AppPref
import ir.kindnesswall.data.local.UserInfoPref
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.repository.UserRepo
import ir.kindnesswall.databinding.FragmentMoreBinding
import ir.kindnesswall.utils.NumberStatus
import ir.kindnesswall.utils.extentions.runOrStartAuth
import ir.kindnesswall.utils.isAppAvailable
import ir.kindnesswall.view.main.MainActivity
import ir.kindnesswall.view.main.addproduct.SubmitGiftViewModel
import ir.kindnesswall.view.main.more.aboutus.AboutUsActivity
import ir.kindnesswall.view.profile.UserProfileActivity
import ir.kindnesswall.view.profile.blocklist.BlockListActivity
import ir.kindnesswall.view.reviewgift.ReviewGiftsActivity
import kotlinx.android.synthetic.main.activity_submit_gift.*
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
    var shPref: SharedPreferences? = null
    val keyName = "nameKey"
    var sEdite: SharedPreferences.Editor? = null
    var numview: Boolean = true
    lateinit var binding: FragmentMoreBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_more, container, false)
        val viewModel: SubmitGiftViewModel by viewModel()
        MainActivity.liveData.observe(binding.root.context as LifecycleOwner, Observer {
            shPref= context!!.getSharedPreferences(UserInfoPref.MyPref, Context.MODE_PRIVATE)
            sEdite =shPref!!.edit()
            val numberstatus = NumberStatus(viewModel ,more_none , more_charity , more_all )
            numberstatus.getShowNumberStatus(binding.root.context)
        })

        binding.moreCharity.setOnClickListener {
            sEdite!!.putString(keyName, "charity")
            sEdite!!.apply()
            viewModel.setPhoneVisibility("charity").observe(this) {
                when (it.status) {
                    CustomResult.Status.LOADING -> {
                        Log.i("4566456456465465", "LOADING")
                    }
                    CustomResult.Status.ERROR -> {
                        Log.i("4566456456465465", "ERROR")
                    }
                    CustomResult.Status.SUCCESS -> {
                        Log.i("4566456456465465", "SUCCESS")
                    }
                }
            }
        }
        binding.moreAll.setOnClickListener {
            sEdite!!.putString(keyName, "all")
            sEdite!!.apply()
            viewModel.setPhoneVisibility("all").observe(this) {
                when (it.status) {
                    CustomResult.Status.LOADING -> {
                        Log.i("4566456456465465", "LOADING")
                    }
                    CustomResult.Status.ERROR -> {
                        Log.i("4566456456465465", "ERROR")
                    }
                    CustomResult.Status.SUCCESS -> {
                        Log.i("4566456456465465", "SUCCESS")
                    }
                }
            }
        }
        binding.moreNone.setOnClickListener {
            sEdite!!.putString(keyName, "none")
            sEdite!!.apply()
            viewModel.setPhoneVisibility("none").observe(this) {
                when (it.status) {
                    CustomResult.Status.LOADING -> {
                        Log.i("4566456456465465", "LOADING")
                    }
                    CustomResult.Status.ERROR -> {
                        Log.i("4566456456465465", "ERROR")
                    }
                    CustomResult.Status.SUCCESS -> {
                        Log.i("4566456456465465", "SUCCESS")
                    }
                }
            }
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

        binding.showNumber.setOnClickListener {
            if (numview.equals(true)) {
                numview = false
                binding.numViewGroup.visibility = View.VISIBLE
            } else {
                numview = true
                binding.numViewGroup.visibility = View.GONE

            }
        }
        binding.contactUs.setOnClickListener { openTelegram() }
        binding.bugReport.setOnClickListener { openTelegram() }
        binding.suggestions.setOnClickListener { openTelegram() }
        var shPref: SharedPreferences =
            view?.context!!.getSharedPreferences(UserInfoPref.MyPref, Context.MODE_PRIVATE);
        val keyName = "nameKey"
        if (shPref.contains(keyName)) {
            when (shPref.getString(keyName, null)) {
                "none" -> {
                    more_none.isChecked = true
                }
                "charity" -> {
                    more_charity.isChecked = true
                }
                "all" -> {
                    more_all.isChecked = true
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