package ir.kindnesswall.view.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import ir.kindnesswall.BaseActivity
import ir.kindnesswall.R
import ir.kindnesswall.annotation.Filter
import ir.kindnesswall.data.local.UserInfoPref
import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.model.user.User
import ir.kindnesswall.databinding.ActivityMyProfileBinding
import ir.kindnesswall.utils.OnItemClickListener
import ir.kindnesswall.utils.imageloader.GlideApp
import ir.kindnesswall.utils.imageloader.circleCropTransform
import ir.kindnesswall.utils.imageloader.loadImage
import ir.kindnesswall.utils.startSingleModeImagePicker
import ir.kindnesswall.view.giftdetail.GiftDetailActivity
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File
import java.util.*


class UserProfileActivity : BaseActivity(), OnItemClickListener {
    lateinit var binding: ActivityMyProfileBinding

    private val viewModel: MyProfileViewModel by viewModel()

    companion object {
        @JvmStatic
        fun start(context: Context, user: User) {
            context.startActivity(
                Intent(context, UserProfileActivity::class.java).putExtra("user", user)
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_profile)

        configureViews(savedInstanceState)

        viewModel.user = intent.getSerializableExtra("user") as User

        binding.myProfileInfo = UserInfoPref
        binding.user = viewModel.user
        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        getGiftList()

        viewModel.newImageUrlLiveData.observe(this) {
            dismissProgressDialog()

            if (it.isFailed) {
                showToastMessage(getString(R.string.please_try_again))
                return@observe
            }

            saveChanges()
        }
    }

    override fun configureViews(savedInstanceState: Bundle?) {
        binding.backImageView.setOnClickListener { onBackPressed() }
        binding.editImageView.setOnClickListener { showEditInfoLayout() }

        binding.userPhoneNumberText.setOnClickListener { callUser() }

        binding.cancelChangesTextView.setOnClickListener {
            revertAllChanges()
            binding.editProfileContainer.visibility = View.GONE
        }

        binding.saveChangesTextView.setOnClickListener {
            if (viewModel.selectedImageFile == null && viewModel.newUserName.isEmpty()) {
                revertAllChanges()
                binding.editProfileContainer.visibility = View.GONE
                return@setOnClickListener
            }

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

        binding.acceptedFilter.setOnClickListener {
            deselectAllFilters()
            selectFilter(binding.acceptedFilter)
            viewModel.currentFilter = Filter.ACCEPTED
            getGiftList()
        }

        binding.rejectedFilter.setOnClickListener {
            deselectAllFilters()
            selectFilter(binding.rejectedFilter)
            viewModel.currentFilter = Filter.REJECTED
            getGiftList()
        }

        initRecyclerView()
    }

    private fun callUser() {
        if (viewModel.user.id == UserInfoPref.id) {
            return
        }

        val intent = Intent(
            Intent.ACTION_DIAL,
            Uri.fromParts("tel", viewModel.user?.phoneNumber ?: "", null)
        )

        startActivity(intent)
    }

    private fun pickImage() {
        startSingleModeImagePicker(this)
    }

    private fun initRecyclerView() {
        binding.userActivityList.apply {
            adapter = UserGiftsAdapter(this@UserProfileActivity)
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
                checkEmptyState()
            }

            CustomResult.Status.LOADING -> {
            }

            CustomResult.Status.ERROR -> {
                dismissProgressDialog()
                showToastMessage("")
                checkEmptyState()
            }
        }
    }

    private fun showList(data: List<GiftModel>?) {
        (binding.userActivityList.adapter as UserGiftsAdapter).submitList(null)

        if (!data.isNullOrEmpty()) {
            binding.emptyPageTextView.visibility = View.GONE
            (binding.userActivityList.adapter as UserGiftsAdapter).submitList(data)
        }
    }

    private fun checkEmptyState() {
        if ((binding.userActivityList.adapter as UserGiftsAdapter).itemCount == 0) {
            binding.emptyPageTextView.visibility = View.VISIBLE

            when (viewModel.currentFilter) {
                Filter.DONATED -> {
                    if (viewModel.user.id == UserInfoPref.id) {
                        binding.emptyPageTextView.text =
                            getString(R.string.donated_gift_empty_message)
                    } else {
                        binding.emptyPageTextView.text =
                            getString(R.string.donated_gift_others_empty_message)
                    }
                }

                Filter.RECEIVED -> {
                    if (viewModel.user.id == UserInfoPref.id) {
                        binding.emptyPageTextView.text =
                            getString(R.string.received_gift_empty_message)
                    } else {
                        binding.emptyPageTextView.text =
                            getString(R.string.received_gift_others_empty_message)
                    }
                }

                Filter.REGISTERED -> {
                    if (viewModel.user.id == UserInfoPref.id) {
                        binding.emptyPageTextView.text =
                            getString(R.string.registered_gift_empty_message)
                    } else {
                        binding.emptyPageTextView.text =
                            getString(R.string.registered_gift_others_empty_message)
                    }
                }

                Filter.ACCEPTED -> {
                    if (viewModel.user.id == UserInfoPref.id) {
                        binding.emptyPageTextView.text =
                            getString(R.string.registered_gift_empty_message)
                    } else {
                        binding.emptyPageTextView.text =
                            getString(R.string.accepted_gift_others_empty_message)
                    }
                }

                Filter.REJECTED -> {
                    if (viewModel.user.id == UserInfoPref.id) {
                        binding.emptyPageTextView.text =
                            getString(R.string.registered_gift_empty_message)
                    } else {
                        binding.emptyPageTextView.text =
                            getString(R.string.rejected_gift_others_empty_message)
                    }
                }
            }
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
        binding.acceptedFilter.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        binding.rejectedFilter.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))

        binding.registeredFilter.setBackgroundResource(R.drawable.profile_filter_stroke)
        binding.donatedFilter.setBackgroundResource(R.drawable.profile_filter_stroke)
        binding.receivedFilter.setBackgroundResource(R.drawable.profile_filter_stroke)
        binding.acceptedFilter.setBackgroundResource(R.drawable.profile_filter_stroke)
        binding.rejectedFilter.setBackgroundResource(R.drawable.profile_filter_stroke)
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
        loadImage(
            UserInfoPref.image,
            binding.userNewImageView,
            placeHolderId = R.drawable.ic_profile_placeholder_gary,
            options = circleCropTransform()
        )
    }

    override fun onItemClicked(position: Int, obj: Any?) {
        GiftDetailActivity.start(this, obj as GiftModel)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == Activity.RESULT_OK && data != null) {
            data.getParcelableArrayListExtra<Image>(Config.EXTRA_IMAGES)?.let {
                val images: ArrayList<Image> = it
                val path = images[0].path

                GlideApp.with(this).load(path).circleCrop().into(binding.userNewImageView)

                viewModel.selectedImagePath = path
                viewModel.selectedImageFile = File(path)
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}
