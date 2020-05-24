package ir.kindnesswall.view.splash

import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.observe
import ir.kindnesswall.BaseActivity
import ir.kindnesswall.R
import ir.kindnesswall.data.local.AppPref
import ir.kindnesswall.data.local.UserInfoPref
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.model.RequestChatModel
import ir.kindnesswall.view.main.MainActivity
import ir.kindnesswall.view.main.conversation.chat.ChatActivity
import ir.kindnesswall.view.onbording.OnBoardingActivity
import org.koin.android.viewmodel.ext.android.viewModel

class SplashActivity : BaseActivity() {

    private val viewModel: SplashViewModel by viewModel()

    private var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        viewModel.isStartFromNotification = intent.getBooleanExtra("isStartFromNotification", false)
        viewModel.requestChatModel =
            intent.getSerializableExtra("requestChatModel") as? RequestChatModel
    }

    override fun onResume() {
        super.onResume()

        handler = Handler()
        handler?.postDelayed({ getUserProfile() }, 2000)
    }

    override fun onStop() {
        super.onStop()

        handler?.removeCallbacksAndMessages(null)
        handler = null
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
                        showToastMessage("")
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
