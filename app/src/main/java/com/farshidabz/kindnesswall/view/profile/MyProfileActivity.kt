package com.farshidabz.kindnesswall.view.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import com.farshidabz.kindnesswall.BaseActivity
import com.farshidabz.kindnesswall.R
import com.farshidabz.kindnesswall.annotation.Filter
import com.farshidabz.kindnesswall.data.local.UserInfoPref
import com.farshidabz.kindnesswall.data.local.dao.catalog.GiftModel
import com.farshidabz.kindnesswall.data.model.CustomResult
import com.farshidabz.kindnesswall.databinding.ActivityMyProfileBinding
import com.farshidabz.kindnesswall.utils.OnItemClickListener
import com.farshidabz.kindnesswall.utils.imageloader.circleCropTransform
import com.farshidabz.kindnesswall.utils.imageloader.loadImage
import com.github.dhaval2404.imagepicker.ImagePicker
import org.koin.android.viewmodel.ext.android.viewModel

class MyProfileActivity : BaseActivity(), OnItemClickListener {
    lateinit var binding: ActivityMyProfileBinding

    private val viewModel: MyProfileViewModel by viewModel()

    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, MyProfileActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_profile)

        configureViews(savedInstanceState)

        binding.userInfo = UserInfoPref
        binding.viewModel = viewModel

        getGiftList()

        viewModel.newImageUrlLiveData.observe(this) {
            dismissProgressDialog()
            saveChanges()
        }
    }

    override fun configureViews(savedInstanceState: Bundle?) {
        binding.backImageView.setOnClickListener { onBackPressed() }
        binding.editImageView.setOnClickListener { showEditInfoLayout() }

        binding.cancelChangesTextView.setOnClickListener { revertAllChanges() }

        binding.saveChangesTextView.setOnClickListener {
            if (viewModel.selectedImageFile != null) {
                showProgressDialog()
                viewModel.uploadImage(context = this, lifecycleOwner = this)
            } else {
                saveChanges()
            }
        }

        binding.userNewImageView.setOnClickListener { pickImage() }
        binding.addNewPhotoImageView.setOnClickListener { pickImage() }

        binding.registeredFilter.setOnClickListener {
            deselectAllFilters()
            selectFilter(binding.registeredFilter)
            viewModel.currentFilter = Filter.REGISTERED
            getGiftList()
        }

        binding.donatedFilter.setOnClickListener {
            deselectAllFilters()
            selectFilter(binding.donatedFilter)
            viewModel.currentFilter = Filter.DONATED
            getGiftList()
        }

        binding.receivedFilter.setOnClickListener {
            deselectAllFilters()
            selectFilter(binding.receivedFilter)
            viewModel.currentFilter = Filter.RECEIVED
            getGiftList()
        }

        initRecyclerView()
    }

    private fun pickImage() {
        ImagePicker.with(this)
            .crop(1f, 1f)
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start { resultCode, data ->
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        data?.let {
                            it.data?.let { uri -> binding.userNewImageView.setImageURI(uri) }

                            viewModel.selectedImageFile = ImagePicker.getFile(it)
                            viewModel.selectedImagePath = ImagePicker.getFilePath(it).toString()
                        }
                    }

                    ImagePicker.RESULT_ERROR -> {
                        Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                    }

                    else -> {
                        Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun initRecyclerView() {
        binding.userActivityList.apply {
            adapter = UserGiftsAdapter(this@MyProfileActivity)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun getGiftList() {
        viewModel.getGifts().observe(this) { onDataReceived(it) }
    }

    private fun onDataReceived(it: CustomResult<List<GiftModel>>) {
        when (it.status) {
            CustomResult.Status.SUCCESS -> {
                dismissProgressDialog()
                showList(it.data)
            }

            CustomResult.Status.LOADING -> {
                showProgressDialog()
            }

            CustomResult.Status.ERROR -> {
                dismissProgressDialog()
                showToastMessage("")
            }
        }
    }

    private fun showList(data: List<GiftModel>?) {
        if (!data.isNullOrEmpty()) {
            (binding.userActivityList.adapter as UserGiftsAdapter).submitList(data)
        }
    }

    private fun selectFilter(view: TextView) {
        view.setTextColor(ContextCompat.getColor(this, R.color.white))
        view.setBackgroundResource(R.drawable.selected_profile_filter)
    }

    private fun deselectAllFilters() {
        binding.registeredFilter.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        binding.donatedFilter.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        binding.receivedFilter.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))

        binding.registeredFilter.setBackgroundResource(R.drawable.profile_filter_stroke)
        binding.donatedFilter.setBackgroundResource(R.drawable.profile_filter_stroke)
        binding.receivedFilter.setBackgroundResource(R.drawable.profile_filter_stroke)
    }

    private fun showEditInfoLayout() {
        binding.userNameEditText.setText(UserInfoPref.name)
        loadImage(
            UserInfoPref.image,
            binding.userNewImageView,
            placeHolderId = R.drawable.ic_profile_placeholder_gary,
            options = circleCropTransform()
        )

        binding.editProfileContainer.visibility = View.VISIBLE
        binding.editImageView.visibility = View.VISIBLE
    }

    private fun saveChanges() {
        viewModel.updateUserProfile().observe(this) {
            when (it.status) {
                CustomResult.Status.LOADING -> {
                    showProgressDialog()
                }
                CustomResult.Status.ERROR -> {
                    dismissProgressDialog()
                }

                CustomResult.Status.SUCCESS -> {
                    loadImage(
                        viewModel.newImageUrlLiveData.value?.address,
                        binding.userImageView,
                        placeHolderId = R.drawable.ic_profile_placeholder_gary,
                        options = circleCropTransform()
                    )
                    binding.userNameText.text = viewModel.newUserName

                    binding.editProfileContainer.visibility = View.GONE

                    dismissProgressDialog()
                }
            }
        }
    }

    private fun revertAllChanges() {
        binding.userNameEditText.setText(UserInfoPref.name)
        loadImage(UserInfoPref.image, binding.userImageView)
    }

    override fun onItemClicked(position: Int, obj: Any?) {
        UserGiftDetailActivity.start(this, obj as GiftModel)
    }
}
