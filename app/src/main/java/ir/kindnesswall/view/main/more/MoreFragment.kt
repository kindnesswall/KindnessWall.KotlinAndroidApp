package ir.kindnesswall.view.main.more

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.android.material.composethemeadapter.MdcTheme
import ir.kindnesswall.BaseFragment
import ir.kindnesswall.KindnessApplication
import ir.kindnesswall.R
import ir.kindnesswall.data.local.AppPref
import ir.kindnesswall.data.local.UserInfoPref
import ir.kindnesswall.utils.extentions.runOrStartAuth
import ir.kindnesswall.utils.isAppAvailable
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
    var shPref: SharedPreferences? = null
    val keyName = "nameKey"
    var sEdite: SharedPreferences.Editor? = null
    var numview: Boolean = true

    private val viewModel: SubmitGiftViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(inflater.context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        }.also {

            MainActivity.liveData.observe(viewLifecycleOwner) {
                shPref = context!!.getSharedPreferences(UserInfoPref.MyPref, Context.MODE_PRIVATE)
                sEdite = shPref!!.edit()
                if (UserInfoPref.bearerToken.isNotEmpty()) {
                    // val numberstatus = NumberStatus(viewModel, binding.moreNone, binding.moreCharity, binding.moreAll)
                    // numberstatus.getShowNumberStatus(binding.root.context)
                }
            }
            // binding.moreCharity.setOnClickListener {
            //     sEdite!!.putString(keyName, "charity")
            //     sEdite!!.apply()
            //     viewModel.setPhoneVisibility("charity").observe(this) {
            //         when (it.status) {
            //             CustomResult.Status.LOADING -> {
            //                 Log.i("4566456456465465", "LOADING")
            //             }
            //             CustomResult.Status.ERROR -> {
            //                 Log.i("4566456456465465", "ERROR")
            //             }
            //             CustomResult.Status.SUCCESS -> {
            //                 Log.i("4566456456465465", "SUCCESS")
            //             }
            //         }
            //     }
            // }
            // binding.moreAll.setOnClickListener {
            //     sEdite!!.putString(keyName, "all")
            //     sEdite!!.apply()
            //     viewModel.setPhoneVisibility("all").observe(this) {
            //         when (it.status) {
            //             CustomResult.Status.LOADING -> {
            //                 Log.i("4566456456465465", "LOADING")
            //             }
            //             CustomResult.Status.ERROR -> {
            //                 Log.i("4566456456465465", "ERROR")
            //             }
            //             CustomResult.Status.SUCCESS -> {
            //                 Log.i("4566456456465465", "SUCCESS")
            //             }
            //         }
            //     }
            // }
            // binding.moreNone.setOnClickListener {
            //     sEdite!!.putString(keyName, "none")
            //     sEdite!!.apply()
            //     viewModel.setPhoneVisibility("none").observe(this) {
            //         when (it.status) {
            //             CustomResult.Status.LOADING -> {
            //                 Log.i("4566456456465465", "LOADING")
            //             }
            //             CustomResult.Status.ERROR -> {
            //                 Log.i("4566456456465465", "ERROR")
            //             }
            //             CustomResult.Status.SUCCESS -> {
            //                 Log.i("4566456456465465", "SUCCESS")
            //             }
            //         }
            //     }
            // }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        view as ComposeView
        view.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        view.setContent {
            MdcTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(title = { Text(text = stringResource(R.string.more_items)) })
                    }
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {

                        // if (UserInfoPref.isGuestUser.not())
                        Header(
                            name = UserInfoPref.name,
                            phoneNumber = UserInfoPref.getPersianPhoneNumber()
                        )

                        Item(
                            textResId = R.string.my_profile,
                            imgResId = R.drawable.ic_profile_placeholder_gary
                        ) {
                            UserProfileActivity.start(
                                requireContext(),
                                UserInfoPref.getUser()
                            )
                        }

                        Item(
                            textResId = R.string.show_number,
                            imgResId = R.drawable.ic_baseline_phone_iphone_24
                        ) {}

                        Item(
                            textResId = R.string.check_submitted_gifts,
                            imgResId = R.drawable.ic_check_and_review
                        ) {
                            ReviewGiftsActivity.start(requireContext())
                        }

                        Item(
                            textResId = R.string.bookmarks,
                            imgResId = R.drawable.ic_bookmark_gray
                        ) {}

                        Item(
                            textResId = R.string.setting,
                            imgResId = R.drawable.ic_settings_gray
                        ) {}

                        Item(
                            textResId = R.string.blocked_users,
                            imgResId = R.drawable.ic_block_gray
                        ) { BlockListActivity.start(requireContext()) }

                        Item(
                            textResId = R.string.help_seekers_label,
                            imgResId = R.drawable.ic_block_gray
                        ) { }

                        Item(
                            textResId = R.string.about,
                            imgResId = R.drawable.ic_info_gray
                        ) { AboutUsActivity.start(requireContext()) }

                        Item(
                            textResId = R.string.pros_and_cons,
                            imgResId = R.drawable.ic_pros_and_cons
                        ) { openTelegram() }

                        Item(
                            textResId = R.string.report_bug,
                            imgResId = R.drawable.ic_bug
                        ) { openTelegram() }

                        Item(
                            textResId = R.string.contact_us,
                            imgResId = R.drawable.ic_cotact_us
                        ) { openTelegram() }

                        // if (UserInfoPref.isGuestUser)
                        Item(textResId = R.string.login, imgResId = R.drawable.ic_exit) { openAuth() }
                        // else
                        Item(textResId = R.string.logout, imgResId = R.drawable.ic_exit) { openAuth() }
                    }
                }
            }
        }
    }

    override fun configureViews() {
        // binding.showNumber.setOnClickListener {
        //     if (numview.equals(true)) {
        //         numview = false
        //         binding.numViewGroup.visibility = View.VISIBLE
        //     } else {
        //         numview = true
        //         binding.numViewGroup.visibility = View.GONE
        //     }
        // }
        var shPref: SharedPreferences =
            view?.context!!.getSharedPreferences(UserInfoPref.MyPref, Context.MODE_PRIVATE);
        val keyName = "nameKey"
        // if (shPref.contains(keyName)) {
        //     when (shPref.getString(keyName, null)) {
        //         "none" -> {
        //             binding.moreNone.isChecked = true
        //         }
        //         "charity" -> {
        //             binding.moreCharity.isChecked = true
        //         }
        //         "all" -> {
        //             binding.moreAll.isChecked = true
        //         }
        //     }
        // }
    }

    private fun openAuth() {
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
}

@Composable
private fun Header(name: String, phoneNumber: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(8.dp))
        Image(
            painter = painterResource(R.drawable.ic_profile_placeholder_gary),
            contentDescription = "Avatar",
            modifier = Modifier.size(72.dp)
        )

        Text(
            name.takeUnless { it.isNotBlank() } ?: stringResource(R.string.user_name_place_holder),
        )

        Text(phoneNumber)
    }
}

@Composable
fun Item(textResId: Int, imgResId: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(imgResId),
            contentDescription = null,
            colorFilter = ColorFilter.tint(color = colorResource(R.color.secondaryTextColor)),
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(textResId),
            modifier = Modifier.weight(1f)
        )
    }
}