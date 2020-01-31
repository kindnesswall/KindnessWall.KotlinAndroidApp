package com.farshidabz.kindnesswall.view.giftdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.farshidabz.kindnesswall.BaseActivity
import com.farshidabz.kindnesswall.R
import com.farshidabz.kindnesswall.data.local.dao.catalog.GiftModel
import com.farshidabz.kindnesswall.databinding.ActivityGiftDetailBinding
import com.farshidabz.kindnesswall.utils.shareString
import com.farshidabz.kindnesswall.view.gallery.GalleryActivity
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

    }

    override fun onShareClicked() {
        shareString(this, "Share kindnessWall message", "subject")
    }

    override fun onBookmarkClicked() {

    }
}
