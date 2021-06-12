package ir.kindnesswall.view.splash

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.observe
import ir.kindnesswall.BaseActivity
import ir.kindnesswall.R
import ir.kindnesswall.data.local.AppPref
import ir.kindnesswall.data.local.UserInfoPref
import ir.kindnesswall.data.model.ChatModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.utils.helper.runIfAuthenticated
import ir.kindnesswall.utils.widgets.NoInternetDialogFragment
import ir.kindnesswall.view.main.MainActivity
import ir.kindnesswall.view.main.conversation.chat.ChatActivity
import ir.kindnesswall.view.onbording.OnBoardingActivity
import ir.kindnesswall.view.update.ForceAndOptionalUpdateActivity
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
        viewModel.getSetting().observe(this) {
            if (it.status == CustomResult.Status.SUCCESS) {
                it.data?.let { data ->
              Toast.makeText(this,"jnklnkl",Toast.LENGTH_LONG).show()

                }
            } else if (it.status == CustomResult.Status.ERROR) {
                if (it.errorMessage?.message!!.contains("Unable to resolve host")) {
                    NoInternetDialogFragment().display(supportFragmentManager) {
                    }
                }
            }
        }
        checkUpdate()

        runIfAuthenticated {
            viewModel.getUserProfile()
        }
    }

    private fun checkUpdate() {
        viewModel.getVersion().observe(this) {
            if (it.status == CustomResult.Status.SUCCESS) {
                it.data?.let { data ->
                    if (ir.kindnesswall.BuildConfig.VERSION_CODE < data.minVersion) {
                        ForceAndOptionalUpdateActivity.start(this, true)
                    } else if (ir.kindnesswall.BuildConfig.VERSION_CODE >= data.minVersion &&
                        ir.kindnesswall.BuildConfig.VERSION_CODE < data.currentVersion
                    ) {
                        ForceAndOptionalUpdateActivity.start(this, false)
                    } else {
                        gotoNextActivity()
                    }
                }
            } else if (it.status == CustomResult.Status.ERROR) {
                if (it.errorMessage?.message!!.contains("Unable to resolve host")) {
                    NoInternetDialogFragment().display(supportFragmentManager) {
                        checkUpdate()
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
