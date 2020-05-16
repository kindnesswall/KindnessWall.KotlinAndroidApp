package ir.kindnesswall.view.main.charity.charitydetail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.google.android.material.bottomsheet.BottomSheetBehavior
import ir.kindnesswall.BaseActivity
import ir.kindnesswall.R
import ir.kindnesswall.data.local.UserInfoPref
import ir.kindnesswall.data.local.dao.charity.CharityModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.databinding.ActivityCharityDetailBinding
import ir.kindnesswall.utils.StaticContentViewer
import ir.kindnesswall.utils.shareString
import ir.kindnesswall.view.authentication.AuthenticationActivity
import ir.kindnesswall.view.main.conversation.chat.ChatActivity
import org.koin.android.viewmodel.ext.android.viewModel


class CharityDetailActivity : BaseActivity(), CharityViewListener {

    lateinit var binding: ActivityCharityDetailBinding

    private val viewModel: CharityViewModel by viewModel()

    companion object {
        @JvmStatic
        fun start(context: Context, charityModel: CharityModel) {
            context.startActivity(Intent(context, CharityDetailActivity::class.java).apply {
                putExtra("charityModel", charityModel)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_charity_detail)

        viewModel.charityModel = intent?.getSerializableExtra("charityModel") as CharityModel

        if (viewModel.charityModel == null) {
            finish()
        }

        configureViews(savedInstanceState)

        viewModel.getUserInformation().observe(this) {
            if (it.status == CustomResult.Status.SUCCESS) {
                binding.otherUser = it.data
            }
        }
    }

    override fun configureViews(savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.item = viewModel.charityModel

        viewModel.charityViewListener = this

        initBottomSheet()
    }

    private fun initBottomSheet() {
        binding.informationBottomSheet.item = viewModel.charityModel
        binding.informationBottomSheet.viewModel = viewModel

        val sheetBehavior =
            BottomSheetBehavior.from(binding.informationBottomSheet.charityContentBottomSheet)

        if (UserInfoPref.bearerToken.isNotEmpty() && UserInfoPref.isCharity) {
            binding.informationBottomSheet.startChatButton.visibility = View.VISIBLE
        } else {
            binding.informationBottomSheet.startChatButton.visibility = View.VISIBLE
        }

        binding.informationBottomSheet.toolbar.setOnClickListener {
            if (sheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            } else {
                sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    override fun onBackButtonClicked() {
        onBackPressed()
    }

    override fun onStartChatClicked() {
        if (UserInfoPref.bearerToken.isEmpty()) {
            AuthenticationActivity.start(this)
        } else {
            viewModel.getChatId().observe(this) {
                when (it.status) {
                    CustomResult.Status.SUCCESS -> {
                        it.data?.let { data ->
                            ChatActivity.start(this, data, true)
                        }
                    }
                }
            }
        }
    }

    override fun onShareClicked() {
        shareString(this, "Share kindnessWall message", "subject")
    }

    override fun onBookmarkClicked() {
    }

    override fun onCallClicked() {
        val intent = Intent(
            Intent.ACTION_DIAL,
            Uri.fromParts("tel", viewModel.charityModel?.telephoneNumber ?: "", null)
        )

        startActivity(intent)
    }

    override fun onTelegramClicked() {
        StaticContentViewer.show(this, viewModel.charityModel?.telegram)
    }

    override fun onInstagramClicked() {
        StaticContentViewer.show(this, viewModel.charityModel?.instagram)
    }

    override fun onEmailClicked() {
        val emailIntent = Intent(
            Intent.ACTION_SENDTO,
            Uri.fromParts("mailto", viewModel.charityModel?.email, null)
        )

        startActivity(Intent.createChooser(emailIntent, "Send email..."))
    }

    override fun onWebsiteClicked() {
        StaticContentViewer.show(this, viewModel.charityModel?.website)
    }
}
