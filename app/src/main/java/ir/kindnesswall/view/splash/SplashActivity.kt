package ir.kindnesswall.view.splash

import android.os.Bundle
import androidx.lifecycle.observe
import ir.kindnesswall.BaseActivity
import ir.kindnesswall.R
import ir.kindnesswall.data.local.AppPref
import ir.kindnesswall.data.local.UserInfoPref
import ir.kindnesswall.data.model.ChatModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.view.main.MainActivity
import ir.kindnesswall.view.main.conversation.chat.ChatActivity
import ir.kindnesswall.view.onbording.OnBoardingActivity
import org.koin.android.viewmodel.ext.android.viewModel

class SplashActivity : BaseActivity() {

    private val viewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        viewModel.isStartFromNotification = intent.getBooleanExtra("isStartFromNotification", false)
        viewModel.requestChatModel =
            intent.getSerializableExtra("requestChatModel") as? ChatModel
    }

    override fun onResume() {
        super.onResume()

        getUserProfile()

//        viewModel.getVersion().observe(this) {
//            if (it.status == CustomResult.Status.SUCCESS) {
//                todo check force update
//                 if there is an update -> goto update activity else get user profile
//                getUserProfile()
//            }
//        }
    }

    override fun configureViews(savedInstanceState: Bundle?) {
    }

    private fun getUserProfile() {
        if (UserInfoPref.bearerToken.isEmpty()) {
            gotoNextActivity()
        } else {
            viewModel.getUserProfile().observe(this@SplashActivity) {
                when (it.status) {
                    CustomResult.Status.SUCCESS -> gotoNextActivity()

                    CustomResult.Status.ERROR -> {
                        gotoNextActivity()
                    }

                    CustomResult.Status.LOADING -> {
                    }
                }
            }
        }
    }

    private fun gotoNextActivity() {
        if (AppPref.isOnBoardingShown or UserInfoPref.bearerToken.isNotEmpty()) {
            if (viewModel.requestChatModel != null && viewModel.isStartFromNotification) {
                ChatActivity.start(
                    this, viewModel.requestChatModel!!,
                    isCharity = true,
                    isStartFromNotification = true
                )
            } else {
                MainActivity.start(this)
            }
            finish()
        } else {
            AppPref.isOnBoardingShown = true
            OnBoardingActivity.start(this)
            finish()
        }
    }
}
