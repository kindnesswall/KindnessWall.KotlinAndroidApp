package com.farshidabz.kindnesswall.view.charity.charitydetail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.farshidabz.kindnesswall.BaseActivity
import com.farshidabz.kindnesswall.R
import com.farshidabz.kindnesswall.data.local.dao.charity.CharityModel
import com.farshidabz.kindnesswall.data.model.CustomResult
import com.farshidabz.kindnesswall.databinding.ActivityCharityDetailBinding
import com.farshidabz.kindnesswall.utils.StaticContentViewer
import com.farshidabz.kindnesswall.utils.shareString
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

        binding.informationBottomSheet.item = viewModel.charityModel
        binding.informationBottomSheet.viewModel = viewModel

        viewModel.charityViewListener = this
    }

    override fun onBackButtonClicked() {
        onBackPressed()
    }

    override fun onRequestClicked() {

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
