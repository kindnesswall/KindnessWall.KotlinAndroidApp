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
import ir.kindnesswall.view.main.addproduct.SubmitGiftActivity
import ir.kindnesswall.view.main.conversation.chat.ChatActivity
import org.koin.android.viewmodel.ext.android.viewModel

class GiftDetailActivity : BaseActivity(), GiftViewListener {

    lateinit var binding: ActivityGiftDetailBinding

    val viewModel: GiftDetailViewModel by viewModel()

    companion object {
        fun start(context: Context, giftModel: GiftModel) {
            context.startActivity(Intent(context, GiftDetailActivity::class.java).apply {
                putExtra("giftModel", giftModel)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_gift_detail)

        viewModel.giftModel = intent?.getParcelableExtra("giftModel") as GiftModel
        if (viewModel.giftModel == null) {
            finish()
        }

        viewModel.isMyGift = viewModel.giftModel?.userId == UserInfoPref.userId

        configureViews(savedInstanceState)
    }

    override fun configureViews(savedInstanceState: Bundle?) {
        viewModel.giftViewListener = this
        binding.item = viewModel.giftModel
        binding.viewModel = viewModel

        setupPhotoSlider()

        if (viewModel.isMyGift) {
            binding.situationTextView.visibility = View.VISIBLE
            binding.situationText.visibility = View.VISIBLE
            binding.secondDivider.visibility = View.VISIBLE
            binding.editGiftImageView.visibility = View.VISIBLE
            setSituationText()
        } else {
            binding.situationTextView.visibility = View.GONE
            binding.situationText.visibility = View.GONE
            binding.secondDivider.visibility = View.GONE
            binding.editGiftImageView.visibility = View.GONE
        }

        viewModel.giftModel?.let {
            if (it.isDeleted && UserInfoPref.isAdmin) {
                binding.situationTextView.text = getString(R.string.deleted)
                binding.situationTextView.setTextColor(
                    ContextCompat.getColor(this, R.color.rejectTextColor)
                )
            } else if (it.donatedToUserId == UserInfoPref.userId.toInt()) {
                binding.situationTextView.visibility = View.VISIBLE
                binding.situationText.visibility = View.VISIBLE
                binding.secondDivider.visibility = View.VISIBLE

                binding.situationTextView.text = getString(R.string.gift_received)
                binding.situationTextView.setTextColor(
                    ContextCompat.getColor(this, R.color.colorPrimary)
                )
            }
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
            if (!it.isReviewed) {
                binding.situationTextView.text = getString(R.string.situation_in_review_queue)
                binding.situationTextView.setTextColor(
                    ContextCompat.getColor(this, R.color.situation_yellow_color)
                )

                return
            }

            if (it.isReviewed && !it.isRejected && it.donatedToUserId != null && it.donatedToUserId!! > 0) {
                binding.situationTextView.text = getString(R.string.filter_profile_donate)
                binding.situationTextView.setTextColor(
                    ContextCompat.getColor(this, R.color.colorPrimary)
                )

                return
            }

            if (it.isReviewed && it.isRejected) {
                binding.situationTextView.text =
                    getString(R.string.rejected, it.rejectReason.toString())
                binding.situationTextView.setTextColor(
                    ContextCompat.getColor(this, R.color.rejectTextColor)
                )

                return
            }

            if (!it.isRejected) {
                binding.situationTextView.text = getString(R.string.situation_confirm)
                binding.situationTextView.setTextColor(
                    ContextCompat.getColor(this, R.color.colorPrimary)
                )
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

    override fun onEditButtonClicked() {
        SubmitGiftActivity.start(this, viewModel.giftModel)
    }

    override fun onRequestClicked() {
        if (UserInfoPref.bearerToken.isEmpty()) {
            AuthenticationActivity.start(this)
        } else {
            if (viewModel.isMyGift) {
                return
            }

            if (!UserInfoPref.isCharity && !UserInfoPref.isAdmin) {
                showPromptDialog(
                    messageToShow = getString(R.string.request_for_users_error_message),
                    positiveButtonText = getString(R.string.ok),
                    showNegativeButton = false
                )

                return
            }

            viewModel.requestGift().observe(this) {
                when (it.status) {
                    CustomResult.Status.SUCCESS -> {
                        it.data?.let { data ->
                            ChatActivity.start(this, data, false, isStartFromNotification = false)
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
