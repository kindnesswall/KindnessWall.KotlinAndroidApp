package ir.kindnesswall.view.main.addproduct

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ir.kindnesswall.BaseActivity
import ir.kindnesswall.R
import ir.kindnesswall.data.db.dao.catalog.GiftModel
import ir.kindnesswall.data.model.*
import ir.kindnesswall.data.repositories.gift.PhoneVisibility
import ir.kindnesswall.databinding.ActivitySubmitGiftBinding
import ir.kindnesswall.domain.common.CustomResult
import ir.kindnesswall.utils.widgets.NoInternetDialogFragment
import ir.kindnesswall.view.category.CategoryActivity
import ir.kindnesswall.view.citychooser.CityChooserActivity
import ir.kindnesswall.view.giftdetail.GiftDetailActivity
import ir.kindnesswall.view.main.MainActivity
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class SubmitGiftActivity : BaseActivity() {
    lateinit var binding: ActivitySubmitGiftBinding
    private val viewModel: SubmitGiftViewModel by viewModel()
    private lateinit var adapter: SelectedImagesAdapter

    companion object {
        fun start(context: Context, giftModel: GiftModel? = null) {
            context.startActivity(
                Intent(
                    context,
                    SubmitGiftActivity::class.java
                ).putExtra("giftModel", giftModel)
            )
        }
    }

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_submit_gift)

        viewModel.editableGiftModel = intent?.getParcelableExtra("giftModel")

        if (viewModel.editableGiftModel != null) {
            viewModel.isNew = false
        }

        configureViews(savedInstanceState)
        configureViewModel()

        if (viewModel.isNew) {
            binding.titleTextView.text = getString(R.string.submit_gift)
            getDraftedModel()
        } else {
            binding.titleTextView.text = getString(R.string.edit_gift)
            fillWithEditableGift()
        }

        viewModel.phoneVisibilityLiveData.observe(this) {
            when (it) {
                PhoneVisibility.None -> binding.submitNone.isChecked = true
                PhoneVisibility.JustCharities -> binding.submitCharity.isChecked = true
                PhoneVisibility.All -> binding.submitAll.isChecked = true
                null -> {
                }
            }
        }
    }


    private fun configureViewModel() {
        viewModel.title.observe(this) {
            checkSubmitButtonEnabling()

            if (it.length < 5) {
                binding.titleMessageTextView.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.onBoardingFirstColor
                    )
                )
            } else {
                binding.titleMessageTextView.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.secondaryTextColor
                    )
                )
            }
        }

        viewModel.price.observe(this) {
            checkSubmitButtonEnabling()
            if (it.length < 4) {
                binding.priceMessageTextView.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.onBoardingFirstColor
                    )
                )
            } else {
                binding.priceMessageTextView.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.secondaryTextColor
                    )
                )
            }
        }

        viewModel.description.observe(this) {
            checkSubmitButtonEnabling()
            if (it.length < 5) {
                binding.descriptionMessageTextView.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.onBoardingFirstColor
                    )
                )
            } else {
                binding.descriptionMessageTextView.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.secondaryTextColor
                    )
                )
            }
        }

        viewModel.categoryId.observe(this) { checkSubmitButtonEnabling() }
        viewModel.provinceId.observe(this) { checkSubmitButtonEnabling() }
        viewModel.regionId.observe(this) { checkSubmitButtonEnabling() }
        viewModel.cityId.observe(this) { checkSubmitButtonEnabling() }

        viewModel.uploadImagesLiveData.observe(this) {
            if (it.isFailed) {
                dismissProgressDialog()
                showToastMessage(getString(R.string.please_try_again))
                return@observe
            }

            viewModel.applyUploadedLocalImage(it.address)

            if (viewModel.images.all { image -> image is GiftImage.OnlineImage }) {
                registerGift()
            } else {
                viewModel.uploadImages(this, this)
            }
        }

        viewModel.openCameraLiveData.observe(this) { uriEvent ->
            uriEvent?.ifNotHandled { cameraPickerContract.launch(it) }
        }
    }

    override fun configureViews(savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.backImageView.setOnClickListener { onBackPressed() }
        binding.addNewPhotoContainer.setOnClickListener { pickImage() }

        binding.chooseCategoryTextView.setOnClickListener {
            CategoryActivity.startActivityForResult(
                this,
                false,
                arrayListOf<CategoryModel>().apply {
                    add(CategoryModel().apply {
                        title = viewModel.categoryName.value
                        id = viewModel.categoryId.value ?: 0
                        isSelected = true
                    })
                })
        }

        binding.chooseCityTextView.setOnClickListener {
            CityChooserActivity.startActivityForResult(this, true)
        }
        binding.submitNone.setOnClickListener {
            viewModel.setPhoneVisibility(PhoneVisibility.None)
        }
        binding.submitCharity.setOnClickListener {
            viewModel.setPhoneVisibility(PhoneVisibility.JustCharities)
        }
        binding.submitAll.setOnClickListener {
            viewModel.setPhoneVisibility(PhoneVisibility.All)
        }
        binding.submitButton.setOnClickListener {
            if (viewModel.editableGiftModel == null && viewModel.isNew) {
                showProgressDialog()
                viewModel.uploadImages(this, this)
            } else {
                registerGift()
            }
        }

        initRecyclerView()

        binding.clearAll.setOnClickListener { clearAll() }
    }

    override fun onBackPressed() {
        if (viewModel.isNew) {
            viewModel.backupData {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }

    private fun fillWithEditableGift() {
        viewModel.editableGiftModel?.let {
            viewModel.isNew = false
            binding.giftTitleEditText.setText(it.title)
            viewModel.title.value = it.title

            binding.giftDescEditText.setText(it.description)
            viewModel.description.value = it.description

            if (it.price != null && it.price!!.toDouble() > 0) {
                binding.giftPriceEditText.setText(it.price.toString())
                viewModel.price.value = it.price.toString()
            }

            if (!it.categoryTitle.isNullOrEmpty()) {
                binding.chooseCategoryTextView.text = it.categoryTitle
                viewModel.categoryName.value = it.categoryTitle
            }

            if (it.cityName.isNullOrEmpty() && !it.provinceName.isNullOrEmpty()) {
                binding.chooseCityTextView.text = it.provinceName
            } else if (!it.cityName.isNullOrEmpty()) {
                binding.chooseCityTextView.text = it.cityName
            }

            viewModel.categoryId.value = it.categoryId
            viewModel.provinceName.value = it.provinceName
            viewModel.provinceId.value = it.provinceId
            viewModel.cityId.value = it.cityId
            viewModel.cityName.value = it.cityName
            viewModel.isNew = false

            it.giftImages.takeUnless { it.isNullOrEmpty() }
                ?.map { GiftImage.OnlineImage(it) }
                ?.let { viewModel.fillByOnlineImages(it) }

            if (viewModel.images.isNotEmpty()) {
                showImages()
            }

            if (viewModel.categoryId.value == 0) {
                binding.chooseCategoryTextView.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.onBoardingFirstColor
                    )
                )

                binding.chooseCategoryTextView.setBackgroundResource(R.drawable.selected_filter_stroke)

            } else {
                binding.chooseCategoryTextView.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorPrimary
                    )
                )

                binding.chooseCategoryTextView.setBackgroundResource(R.drawable.profile_filter_stroke)
            }

            if (viewModel.cityId.value == 0 && viewModel.provinceId.value == 0) {
                binding.chooseCityTextView.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.onBoardingFirstColor
                    )
                )

                binding.chooseCityTextView.setBackgroundResource(R.drawable.selected_filter_stroke)

            } else {
                binding.chooseCityTextView.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorPrimary
                    )
                )

                binding.chooseCityTextView.setBackgroundResource(R.drawable.profile_filter_stroke)
            }
        }
    }

    private fun getDraftedModel() {
        viewModel.getBackUpData().observe(this) { model ->
            model?.let {
                if (it.isEmpty()) return@observe

                binding.giftTitleEditText.setText(it.title)
                binding.giftDescEditText.setText(it.description)

                if (it.price.toDouble() > 0) {
                    binding.giftPriceEditText.setText(it.price.toString())
                }

                if (!it.categoryName.isNullOrEmpty()) {
                    binding.chooseCategoryTextView.text = it.categoryName
                }

                if (!it.cityName.isNullOrEmpty() && !it.provinceName.isNullOrEmpty()) {
                    binding.chooseCityTextView.text = it.provinceName
                } else if (!it.cityName.isNullOrEmpty()) {
                    binding.chooseCityTextView.text = it.cityName
                }

                viewModel.categoryId.value = it.categoryId
                viewModel.provinceName.value = it.provinceName
                viewModel.provinceId.value = it.provinceId
                viewModel.cityId.value = it.cityId
                viewModel.cityName.value = it.cityName
                viewModel.isNew = it.isNew

                if (viewModel.categoryId.value == 0) {
                    binding.chooseCategoryTextView.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.onBoardingFirstColor
                        )
                    )

                    binding.chooseCategoryTextView.setBackgroundResource(R.drawable.selected_filter_stroke)

                } else {
                    binding.chooseCategoryTextView.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.colorPrimary
                        )
                    )

                    binding.chooseCategoryTextView.setBackgroundResource(R.drawable.profile_filter_stroke)
                }

                if (viewModel.cityId.value == 0 && viewModel.provinceId.value == 0) {
                    binding.chooseCityTextView.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.onBoardingFirstColor
                        )
                    )

                    binding.chooseCityTextView.setBackgroundResource(R.drawable.selected_filter_stroke)

                } else {
                    binding.chooseCityTextView.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.colorPrimary
                        )
                    )

                    binding.chooseCityTextView.setBackgroundResource(R.drawable.profile_filter_stroke)
                }

                viewModel.fillByLocalImages(it.giftImages.map { GiftImage.LocalImage(it.toUri()) })

                if (viewModel.images.isNotEmpty()) {
                    showImages()
                }
            }
        }
    }

    private fun clearAll() {
        binding.giftTitleEditText.setText("")
        binding.giftDescEditText.setText("")
        binding.giftPriceEditText.setText("")

        binding.titleMessageTextView.setTextColor(
            ContextCompat.getColor(this, R.color.secondaryTextColor)
        )
        binding.priceMessageTextView.setTextColor(
            ContextCompat.getColor(this, R.color.secondaryTextColor)
        )
        binding.descriptionMessageTextView.setTextColor(
            ContextCompat.getColor(this, R.color.secondaryTextColor)
        )

        binding.chooseCategoryTextView.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.onBoardingFirstColor
            )
        )

        binding.chooseCityTextView.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.onBoardingFirstColor
            )
        )

        binding.chooseCategoryTextView.setBackgroundResource(R.drawable.selected_filter_stroke)
        binding.chooseCityTextView.setBackgroundResource(R.drawable.selected_filter_stroke)

        binding.chooseCategoryTextView.text = getString(R.string.choose_category)
        binding.chooseCityTextView.text = getString(R.string.choose_city)

        adapter.clear()

        viewModel.clearData()
    }

    private fun registerGift() {
        if (viewModel.isNew) {
            submitGift()
        } else {
            showPromptDialog(
                messageToShow = getString(R.string.edit_gift_warning_message),
                onPositiveClickCallback = { submitGift() })
        }
    }

    private fun submitGift() {
        viewModel.submitGift().observe(this) {
            when (it.status) {
                CustomResult.Status.LOADING -> showProgressDialog()
                CustomResult.Status.ERROR -> {
                    dismissProgressDialog()
                    if (it.errorMessage?.message!!.contains("Unable to resolve host")) {
                        NoInternetDialogFragment().display(supportFragmentManager) {
                            submitGift()
                        }
                    } else {
                        showToastMessage(getString(R.string.please_try_again))
                    }
                }
                CustomResult.Status.SUCCESS -> {
                    dismissProgressDialog()
                    showPromptDialog(
                        messageToShow = getString(R.string.gift_submitted_successfully),
                        showNegativeButton = false,
                        positiveButtonText = getString(R.string.ok),
                        onPositiveClickCallback = { response ->
                            it.data?.let { gift ->
                                viewModel.removeBackupData()
                                GiftDetailActivity.start(this, gift)
                                finish()
                            }
                        }
                    )
                }
            }
        }
    }

    private fun pickImage() {
        MaterialAlertDialogBuilder(this)
            .setItems(
                listOf(R.string.from_gallery, R.string.by_camera).map(::getString).toTypedArray()
            ) { _, which ->
                when (which) {
                    0 -> {
                        imagePickerContract.launch("image/*")
                    }
                    1 -> {
                        viewModel.attemptOpenCamera(this)
                    }
                    else -> {
                    }
                }
            }
            .show()
    }

    private fun initRecyclerView() {
        adapter = SelectedImagesAdapter().apply {
            onClickCallback = { position ->
                viewModel.deleteImage(position)
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
            adapter.setItems(viewModel.images)
        }

        checkSubmitButtonEnabling()
    }

    private fun checkSubmitButtonEnabling() {
        val condition =
            (!viewModel.description.value.isNullOrEmpty() && viewModel.description.value!!.length >= 5) &&
                    (!viewModel.title.value.isNullOrEmpty() && viewModel.title.value!!.length >= 5) &&
                    (!viewModel.price.value.isNullOrEmpty() && viewModel.price.value!!.toFloat() >= 1000) &&
                    (viewModel.provinceId.value ?: 0 > 0) &&
                    (viewModel.cityId.value ?: 0 > 0) &&
                    (viewModel.categoryId.value ?: 0 > 0) &&
                    viewModel.images.isNotEmpty()

        if (viewModel.hasRegion) {
            if (viewModel.regionId.value ?: 0 > 0) {
                binding.submitButton.isEnabled = condition && true
                return
            } else {
                binding.submitButton.isEnabled = false
                return
            }
        }

        binding.submitButton.isEnabled = condition
    }

    private val imagePickerContract =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris: List<Uri?>? ->
            Timber.d("image-picker result=$uris ")
            if (uris.isNullOrEmpty()) return@registerForActivityResult

            val filteredUris = uris.filterNotNull().map { GiftImage.LocalImage(it) }
            viewModel.addLocalImages(filteredUris)

            showImages()
        }

    private val cameraPickerContract =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { wasSuccessfully: Boolean ->
            Timber.d("camera result=$wasSuccessfully ")
            if (wasSuccessfully.not()) return@registerForActivityResult

            viewModel.submitPendingImages()

            showImages()
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CityChooserActivity.CITY_CHOOSER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val region = data?.getSerializableExtra("region")
                val cityModel = data?.getSerializableExtra("city")
                if (region != null) {
                    (region as? RegionModel)?.let {
                        viewModel.hasRegion = true
                        viewModel.provinceId.value = it.province_id
                        viewModel.cityId.value = it.city_id
                        viewModel.regionId.value = it.id
                        viewModel.regionName.value = it.name

                        binding.chooseCityTextView.text = it.name

                        binding.chooseCityTextView.setTextColor(
                            ContextCompat.getColor(
                                this,
                                R.color.colorPrimary
                            )
                        )
                        binding.chooseCityTextView.setBackgroundResource(R.drawable.profile_filter_stroke)
                    }

                } else if (cityModel != null) {
                    (cityModel as? CityModel)?.let {
                        viewModel.hasRegion = false
                        viewModel.provinceId.value = it.provinceId
                        viewModel.cityName.value = it.name
                        viewModel.cityId.value = it.id

                        binding.chooseCityTextView.text = it.name

                        binding.chooseCityTextView.setTextColor(
                            ContextCompat.getColor(
                                this,
                                R.color.colorPrimary
                            )
                        )
                        binding.chooseCityTextView.setBackgroundResource(R.drawable.profile_filter_stroke)
                    }
                }
            }
        } else if (requestCode == CategoryActivity.CATEGORY_CHOOSER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val categoryModels =
                    data?.getSerializableExtra("selectedCategories") as? ArrayList<CategoryModel>

                categoryModels?.let {
                    viewModel.categoryId.value = it[0].id
                    viewModel.categoryName.value = it[0].title
                    binding.chooseCategoryTextView.text = it[0].title

                    binding.chooseCategoryTextView.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.colorPrimary
                        )
                    )
                    binding.chooseCategoryTextView.setBackgroundResource(R.drawable.profile_filter_stroke)
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStop() {
        super.onStop()
        MainActivity.liveData.value = "getPhoneNumberValue"
    }
}
