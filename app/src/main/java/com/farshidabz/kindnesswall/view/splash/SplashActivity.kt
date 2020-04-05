package com.farshidabz.kindnesswall.view.splash

import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.observe
import com.farshidabz.kindnesswall.BaseActivity
import com.farshidabz.kindnesswall.R
import com.farshidabz.kindnesswall.data.local.AppPref
import com.farshidabz.kindnesswall.data.local.UserInfoPref
import com.farshidabz.kindnesswall.data.model.CustomResult
import com.farshidabz.kindnesswall.view.main.MainActivity
import com.farshidabz.kindnesswall.view.onbording.OnBoardingActivity
import org.koin.android.viewmodel.ext.android.viewModel

class SplashActivity : BaseActivity() {

    private val viewModel: SplashViewModel by viewModel()

    private var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
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
        if (AppPref.isOnBoardingShown) {
            MainActivity.start(this)
            finish()
        } else {
            AppPref.isOnBoardingShown = true
            OnBoardingActivity.start(this)
            finish()
        }
    }
}
