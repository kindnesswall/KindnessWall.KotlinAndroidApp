package com.farshidabz.kindnesswall.view.addproduct

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.recyclerview.widget.SimpleItemAnimator
import com.farshidabz.kindnesswall.BaseActivity
import com.farshidabz.kindnesswall.R
import com.farshidabz.kindnesswall.data.model.CategoryModel
import com.farshidabz.kindnesswall.data.model.CityModel
import com.farshidabz.kindnesswall.data.model.CustomResult
import com.farshidabz.kindnesswall.data.model.RegionModel
import com.farshidabz.kindnesswall.databinding.ActivitySubmitGiftBinding
import com.farshidabz.kindnesswall.utils.startMultiSelectingImagePicker
import com.farshidabz.kindnesswall.view.category.CategoryActivity
import com.farshidabz.kindnesswall.view.citychooser.CityChooserActivity
import com.farshidabz.kindnesswall.view.giftdetail.GiftDetailActivity
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import org.koin.android.viewmodel.ext.android.viewModel

class SubmitGiftActivity : BaseActivity() {

    lateinit var binding: ActivitySubmitGiftBinding

    private val viewModel: SubmitGiftViewModel by viewModel()
    private lateinit var adapter: SelectedImagesAdapter

    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, SubmitGiftActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_submit_gift)

        configureViews(savedInstanceState)
        configureViewModel()
    }

    private fun configureViewModel() {
        viewModel.title.observe(this) { checkSubmitButtonEnabling() }
        viewModel.price.observe(this) { checkSubmitButtonEnabling() }
        viewModel.description.observe(this) { checkSubmitButtonEnabling() }

        viewModel.categoryId.observe(this) { checkSubmitButtonEnabling() }
        viewModel.provinceId.observe(this) { checkSubmitButtonEnabling() }
        viewModel.cityId.observe(this) { checkSubmitButtonEnabling() }

        viewModel.uploadImagesLiveData.observe(this) {
            viewModel.uploadedImagesAddress.add(it.address)
            viewModel.imagesToUpload.removeAt(0)
            if (viewModel.imagesToUpload.isEmpty()) {
                registerGift()
            } else {
                viewModel.uploadImages(this, this)
            }
        }
    }

    override fun configureViews(savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.backImageView.setOnClickListener { onBackPressed() }
        binding.addNewPhotoContainer.setOnClickListener { pickImage() }

        binding.chooseCategoryTextView.setOnClickListener {
            CategoryActivity.startActivityForResult(this)
        }

        binding.chooseCityTextView.setOnClickListener {
            CityChooserActivity.startActivityForResult(this, true)
        }

        binding.submitButton.setOnClickListener { viewModel.uploadImages(this, this) }

        initRecyclerView()
    }

    private fun registerGift() {
        viewModel.submitGift().observe(this) {
            when (it.status) {
                CustomResult.Status.LOADING -> showProgressDialog()
                CustomResult.Status.ERROR -> {
                    dismissProgressDialog()
                    showToastMessage("")
                }
                CustomResult.Status.SUCCESS -> {
                    dismissProgressDialog()
                    it.data?.let { gift ->
                        GiftDetailActivity.start(this, gift)
                        finish()
                    }
                }
            }
        }
    }

    private fun pickImage() {
        startMultiSelectingImagePicker(this)
    }

    private fun initRecyclerView() {
        adapter = SelectedImagesAdapter().apply {
            onClickCallback = { position, url ->
                viewModel.selectedImages.removeAt(position)
                viewModel.imagesToUpload.removeAt(position)
                showImages()
            }
        }

        adapter.setHasStableIds(true)

        binding.selectedImagesRecyclerView.adapter = adapter

        val animator = binding.selectedImagesRecyclerView.itemAnimator

        if (animator is SimpleItemAnimator) {
            animator.supportsChangeAnimations = false
        }
    }

    private fun showImages() {
        if (::adapter.isInitialized) {
            adapter.setItems(viewModel.selectedImages)
        }

        checkSubmitButtonEnabling()
    }

    private fun checkSubmitButtonEnabling() {
        binding.submitButton.isEnabled =
            !(viewModel.description.value.isNullOrEmpty() or
                    viewModel.title.value.isNullOrEmpty() or
                    viewModel.price.value.isNullOrEmpty() or
                    viewModel.selectedImages.isEmpty())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == Activity.RESULT_OK && data != null) {
            data.getParcelableArrayListExtra<Image>(Config.EXTRA_IMAGES)?.let {
                viewModel.selectedImages.addAll(it.map { image -> image.path })
                viewModel.imagesToUpload.addAll(it.map { image -> image.path })
                showImages()
            }
        } else if (requestCode == CityChooserActivity.CITY_CHOOSER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val region = data?.getSerializableExtra("region")
                val cityModel = data?.getSerializableExtra("city")
                if (region != null) {
                    (region as? RegionModel)?.let {
                        viewModel.provinceId.value = it.id
                        binding.chooseCityTextView.text = it.name
                    }

                } else if (cityModel != null) {
                    (cityModel as? CityModel)?.let {
                        viewModel.provinceId.value = it.provinceId
                        viewModel.cityId.value = it.id

                        binding.chooseCityTextView.text = it.name
                    }
                }
            }
        } else if (requestCode == CategoryActivity.CATEGORY_CHOOSER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val categoryModels =
                    data?.getSerializableExtra("selectedCategories") as? ArrayList<CategoryModel>

                categoryModels?.let {
                    viewModel.categoryId.value = it[0].id
                    binding.chooseCategoryTextView.text = it[0].title
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}
