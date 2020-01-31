package com.farshidabz.kindnesswall.view.gallery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.farshidabz.kindnesswall.BaseActivity
import com.farshidabz.kindnesswall.R
import com.farshidabz.kindnesswall.databinding.ActivityGalleryBinding
import com.farshidabz.kindnesswall.utils.helper.getDeviceWidth
import org.koin.android.viewmodel.ext.android.viewModel


/**
 * Created by farshid.abazari since 01/17/19
 *
 * Usage: Gallery activity
 *
 * How to call: just call startActivityForResult and pass images url as array list of strings
 */

class GalleryActivity : BaseActivity(), OnGalleryButtonClickListener {

    val viewModel: GalleryViewModel by viewModel()

    private lateinit var binding: ActivityGalleryBinding

    companion object {
        @JvmStatic
        fun start(context: Context, images: ArrayList<String>, currentImageIndex: Int) {
            val intent = Intent(context, GalleryActivity::class.java)
            intent.putExtra("images", images)
            intent.putExtra("currentImageIndex", currentImageIndex)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gallery)

        readIntent()
        configureViewModel()
        configureViews(savedInstanceState)
    }

    private fun readIntent() {
        intent?.let {
            viewModel.images = it.getSerializableExtra("images") as ArrayList<String>
            viewModel.currentImageIndex = it.getIntExtra("currentImageIndex", 0)
        }
    }

    private fun configureViewModel() {
        viewModel.onGalleryButtonClickListener = this
    }

    override fun configureViews(savedInstanceState: Bundle?) {
        setupPhotoSlider()
    }

    private fun setupPhotoSlider() {
        binding.viewModel = viewModel

        binding.photoSlider.layoutParams.height = getDeviceWidth(this)
        binding.photoSlider.initialize(zoomable = true, showIndicator = false)
            .show(viewModel.images)

        binding.photoSlider.scrollToPosition(viewModel.currentImageIndex)

        binding.photoSlider.photoSliderRecyclerView.setItemViewCacheSize(viewModel.images.size)
        binding.photoSliderIndicator.attachToRecyclerView(binding.photoSlider.photoSliderRecyclerView)
    }

    override fun onCloseClicked() {
        onBackPressed()
    }
}
