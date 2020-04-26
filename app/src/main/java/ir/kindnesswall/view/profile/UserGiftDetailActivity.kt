package ir.kindnesswall.view.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import ir.kindnesswall.BaseActivity
import ir.kindnesswall.R
import ir.kindnesswall.data.local.UserInfoPref
import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.databinding.ActivityUserGiftDetailBinding
import ir.kindnesswall.utils.shareString
import ir.kindnesswall.view.gallery.GalleryActivity
import ir.kindnesswall.view.giftdetail.GiftDetailViewModel
import ir.kindnesswall.view.giftdetail.GiftViewListener
import org.koin.android.viewmodel.ext.android.viewModel

class UserGiftDetailActivity : BaseActivity(), GiftViewListener {
    private val viewModel: GiftDetailViewModel by viewModel()
    lateinit var binding: ActivityUserGiftDetailBinding

    companion object {
        @JvmStatic
        fun start(context: Context, giftModel: GiftModel) {
            context.startActivity(Intent(context, UserGiftDetailActivity::class.java).apply {
                putExtra("giftModel", giftModel)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_gift_detail)

        configureViewModel()
        configureViews(savedInstanceState)
    }

    private fun configureViewModel() {
        viewModel.giftModel = intent?.getSerializableExtra("giftModel") as GiftModel

        if (viewModel.giftModel == null) {
            finish()
        }
    }

    override fun configureViews(savedInstanceState: Bundle?) {
        viewModel.giftViewListener = this
        binding.item = viewModel.giftModel
        binding.viewModel = viewModel

        if (viewModel.giftModel?.userId == UserInfoPref.userId) {
            binding.requestButton.visibility = View.GONE
        }

        setupPhotoSlider()
        setSituationText()
    }

    private fun setSituationText() {
        binding.situationTextView.text = ""
        viewModel.giftModel?.let {
            if (it.isRejected) {
                binding.situationTextView.text = getString(R.string.rejected)
                binding.situationTextView.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.rejectTextColor
                    )
                )
            } else if (!it.isRejected && it.isReviewed) {
                binding.situationTextView.text = getString(R.string.accepted)
                binding.situationTextView.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorPrimary
                    )
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
    }

    override fun onShareClicked() {
        shareString(this, "Share kindnessWall message", "subject")
    }

    override fun onBookmarkClicked() {
    }
}
