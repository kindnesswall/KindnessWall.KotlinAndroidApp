package ir.kindnesswall.view.giftdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import ir.kindnesswall.BaseActivity
import ir.kindnesswall.R
import ir.kindnesswall.data.local.UserInfoPref
import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.databinding.ActivityGiftDetailBinding
import ir.kindnesswall.utils.shareString
import ir.kindnesswall.view.authentication.AuthenticationActivity
import ir.kindnesswall.view.gallery.GalleryActivity
import ir.kindnesswall.view.main.conversation.chat.ChatActivity
import org.koin.android.viewmodel.ext.android.viewModel

class GiftDetailActivity : BaseActivity(), GiftViewListener {

    lateinit var binding: ActivityGiftDetailBinding

    val viewModel: GiftDetailViewModel by viewModel()

    companion object {
        @JvmStatic
        fun start(context: Context, giftModel: GiftModel, isMyGift: Boolean = false) {
            context.startActivity(Intent(context, GiftDetailActivity::class.java).apply {
                putExtra("giftModel", giftModel)
                putExtra("isMyGift", isMyGift)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_gift_detail)
        viewModel.isMyGift = intent?.getBooleanExtra("isMyGift", false) ?: false

        viewModel.giftModel = intent?.getSerializableExtra("giftModel") as GiftModel
        if (viewModel.giftModel == null) {
            finish()
        }

        configureViews(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        if (UserInfoPref.bearerToken.isEmpty() or UserInfoPref.isCharity) {
            binding.requestButton.visibility = View.VISIBLE
        } else {
            binding.requestButton.visibility = View.VISIBLE
        }
    }

    override fun configureViews(savedInstanceState: Bundle?) {
        viewModel.giftViewListener = this
        binding.item = viewModel.giftModel
        binding.viewModel = viewModel

        if (UserInfoPref.bearerToken.isEmpty() or UserInfoPref.isCharity) {
            binding.requestButton.visibility = View.VISIBLE
        } else {
            binding.requestButton.visibility = View.GONE
        }

        setupPhotoSlider()

        if (viewModel.isMyGift) {
            binding.situationTextView.visibility = View.VISIBLE
            binding.situationText.visibility = View.VISIBLE
            binding.secondDivider.visibility = View.VISIBLE
            setSituationText()
        } else {
            binding.situationTextView.visibility = View.GONE
            binding.situationText.visibility = View.GONE
            binding.secondDivider.visibility = View.GONE
        }
    }

    private fun setupPhotoSlider() {
        binding.photoSlider
            .initialize(
                zoomable = false,
                showIndicator = true,
                clickListener = {
                    gotoGalleryActivity()
                },
                pageChangeCallback = { selectedImageIndex ->
                    viewModel.selectedImageIndex = selectedImageIndex
                })
            .show(viewModel.giftModel?.giftImages)

    }

    private fun setSituationText() {
        binding.situationTextView.text = ""
        viewModel.giftModel?.let {
            when {
                it.isRejected -> {
                    binding.situationTextView.text = getString(R.string.rejected)
                    binding.situationTextView.setTextColor(
                        ContextCompat.getColor(this, R.color.rejectTextColor)
                    )
                }
                it.isReviewed -> {
                    binding.situationTextView.text = getString(R.string.accepted)
                    binding.situationTextView.setTextColor(
                        ContextCompat.getColor(this, R.color.colorPrimary)
                    )
                }
                else -> {
                    binding.situationTextView.text = getString(R.string.registered)
                    binding.situationTextView.setTextColor(
                        ContextCompat.getColor(this, R.color.colorPrimary)
                    )
                }
            }
        }
    }

    private fun gotoGalleryActivity() {
        GalleryActivity.start(
            this,
            viewModel.giftModel?.giftImages as ArrayList<String>,
            viewModel.selectedImageIndex
        )
    }

    override fun onBackButtonClicked() {
        onBackPressed()
    }

    override fun onRequestClicked() {
        if (UserInfoPref.bearerToken.isEmpty()) {
            AuthenticationActivity.start(this)
        } else {
            if (viewModel.isMyGift) {
                return
            }

            viewModel.requestGift().observe(this) {
                when (it.status) {
                    CustomResult.Status.SUCCESS -> {
                        it.data?.let { data ->
                            ChatActivity.start(this, data)
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
}
