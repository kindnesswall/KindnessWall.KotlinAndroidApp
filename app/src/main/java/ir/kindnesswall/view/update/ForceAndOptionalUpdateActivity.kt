package ir.kindnesswall.view.update

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import ir.kindnesswall.R
import ir.kindnesswall.data.local.AppPref
import ir.kindnesswall.data.local.UserInfoPref
import ir.kindnesswall.databinding.ActivityForceAndOptionalUpdateBinding
import ir.kindnesswall.utils.updateApp
import ir.kindnesswall.view.main.MainActivity
import ir.kindnesswall.view.onbording.OnBoardingActivity

class ForceAndOptionalUpdateActivity : AppCompatActivity() {

    lateinit var binding: ActivityForceAndOptionalUpdateBinding

    var isForceUpdate = false
    var updateLink = ""

    companion object {
        fun start(context: Context, isForceUpdate: Boolean, updateLink: String) {
            context.startActivity(
                Intent(
                    context,
                    ForceAndOptionalUpdateActivity::class.java
                ).putExtra("isForceUpdate", isForceUpdate).putExtra("updateLink", updateLink)
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_force_and_optional_update)

        isForceUpdate = intent.getBooleanExtra("isForceUpdate", false)
        updateLink = intent.getStringExtra("updateLink")

        binding.isForeUpdate = isForceUpdate

        binding.closeImageView.setOnClickListener { gotoNextActivity() }
        binding.updateButton.setOnClickListener { updateApp(this) }
    }

    private fun gotoNextActivity() {
        if (AppPref.isOnBoardingShown or UserInfoPref.bearerToken.isNotEmpty()) {
            MainActivity.start(this)
            finish()
        } else {
            AppPref.isOnBoardingShown = true
            OnBoardingActivity.start(this)
            finish()
        }
    }
}
