package ir.kindnesswall.view.giftdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import ir.kindnesswall.BaseActivity
import ir.kindnesswall.R
import ir.kindnesswall.data.local.UserInfoPref
import ir.kindnesswall.data.local.dao.catalog.GiftModel
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
        fun start(context: Context, giftModel: GiftModel) {
            context.startActivity(Intent(context, GiftDetailActivity::class.java).apply {
                putExtra("giftModel", giftModel)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_gift_detail)

        configureViewModel()
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

        if (UserInfoPref.bearerToken.isEmpty() or UserInfoPref.isCharity) {
            binding.requestButton.visibility = View.VISIBLE
        } else {
            binding.requestButton.visibility = View.GONE
        }

        setupPhotoSlider()
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
        if (UserInfoPref.bearerToken.isEmpty()) {
            AuthenticationActivity.start(this)
        } else {
            // todo get chatId
            ChatActivity.start(this, 3)
        }
    }

    override fun onShareClicked() {
        shareString(this, "Share kindnessWall message", "subject")
    }

    override fun onBookmarkClicked() {

    }
}
