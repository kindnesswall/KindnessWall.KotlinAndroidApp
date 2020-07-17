package ir.kindnesswall.view.main.charity.addcharity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.recyclerview.widget.SimpleItemAnimator
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import ir.kindnesswall.BaseActivity
import ir.kindnesswall.R
import ir.kindnesswall.data.local.dao.charity.CharityModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.model.user.User
import ir.kindnesswall.databinding.ActivitySubmitCharityBinding
import ir.kindnesswall.utils.startMultiSelectingImagePicker
import ir.kindnesswall.view.main.addproduct.SelectedImagesAdapter
import ir.kindnesswall.view.main.charity.charitydetail.CharityDetailActivity
import ir.kindnesswall.view.main.more.userlist.UserListActivity
import kotlinx.android.synthetic.main.activity_submit_charity.*
import org.koin.android.viewmodel.ext.android.viewModel

class SubmitCharityActivity : BaseActivity() {

    lateinit var mBinding: ActivitySubmitCharityBinding

    private val mViewModel: SubmitCharityViewModel by viewModel()
    private lateinit var mAdapter: SelectedImagesAdapter
    private var mPeakedUser: User? = null

    companion object {
        const val REQUEST_CODE_PEAK_USER = 1000
        fun start(context: Context, charityModel: CharityModel? = null) {
            context.startActivity(
                Intent(context, SubmitCharityActivity::class.java)
                    .putExtra("charityModel", charityModel)
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_submit_charity)

        mViewModel.editableCharityModel =
            intent?.getSerializableExtra("charityModel") as CharityModel?

        if (mViewModel.editableCharityModel != null) {
            mViewModel.isNew = false
        }

        configureViews(savedInstanceState)
        configureViewModel()

        if (mViewModel.isNew) {
            mBinding.titleTextView.text = getString(R.string.submit_charity)
        } else {
            mBinding.titleTextView.text = getString(R.string.edit_charity)
            fillWithEditableCharity()
        }
    }

    private fun configureViewModel() {
        mViewModel.mTitleLive.observe(this) { checkSubmitButtonEnabling() }
        mViewModel.mDescriptionLive.observe(this) { checkSubmitButtonEnabling() }
        mViewModel.mAddressLive.observe(this) { checkSubmitButtonEnabling() }
        mViewModel.mPhoneNumberLive.observe(this) { checkSubmitButtonEnabling() }
        mViewModel.mTelephoneNumberLive.observe(this) { checkSubmitButtonEnabling() }
        mViewModel.mWebsiteLive.observe(this) { checkSubmitButtonEnabling() }
        mViewModel.mEmailLive.observe(this) { checkSubmitButtonEnabling() }
        mViewModel.mInstagramLive.observe(this) { checkSubmitButtonEnabling() }
        mViewModel.mTelegramLive.observe(this) { checkSubmitButtonEnabling() }
        mViewModel.mAddressLive.observe(this) { checkSubmitButtonEnabling() }

        mViewModel.uploadImagesLiveData.observe(this) {
            if (it.isFailed) {
                dismissProgressDialog()
                showToastMessage(getString(R.string.please_try_again))
                return@observe
            }

            mViewModel.uploadedImagesAddress.add(it.address)
            mViewModel.imagesToUpload.removeAt(0)
            if (mViewModel.imagesToUpload.isEmpty())
                registerGift()
            else
                mViewModel.uploadImages(this, this)

        }
    }

    override fun configureViews(savedInstanceState: Bundle?) {
        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this

        setOnClick()
        initRecyclerView()
    }

    private fun setOnClick() {
        clearAll.setOnClickListener { onClearAll() }
        addNewPhotoContainer.setOnClickListener { onPickImage() }
        submitButton.setOnClickListener { onSubmitButton() }
        tvPeakUser.setOnClickListener { onPeakUser() }
        backImageView.setOnClickListener { onBackPressed() }
        ivRemovePeakedUser.setOnClickListener { onRemovePeakedUser() }
    }

    private fun onClearAll() {
        mBinding.charityModel = CharityModel()
        mAdapter.clear()
        mViewModel.clearData()
    }

    private fun onSubmitButton() {
        if (mViewModel.editableCharityModel == null && mViewModel.isNew && mViewModel.imagesToUpload.isNotEmpty()) {
            showProgressDialog()
            mViewModel.uploadImages(this, this)
        } else
            registerGift()
    }

    private fun onPeakUser() {
        UserListActivity.startActivityForResult(this, REQUEST_CODE_PEAK_USER, UserListActivity.CLASS_TYPE_PEAK_USER)
    }

    private fun onPickImage() {
        startMultiSelectingImagePicker(this)
    }

    private fun fillWithEditableCharity() {
        mViewModel.editableCharityModel?.let {
            mViewModel.mTitleLive.value = it.name
            mViewModel.mDescriptionLive.value = it.description
            mViewModel.mPhoneNumberLive.value = it.mobileNumber
            mViewModel.mTelephoneNumberLive.value = it.telephoneNumber
            mViewModel.mWebsiteLive.value = it.website
            mViewModel.mEmailLive.value = it.email
            mViewModel.mInstagramLive.value = it.instagram
            mViewModel.mTelegramLive.value = it.telegram
            mViewModel.mAddressLive.value = it.address
            mViewModel.isNew = false

            mBinding.charityModel = it

            it.imageUrl?.takeIf { url -> url.isNotEmpty() }?.run {
                mViewModel.uploadedImagesAddress.add(this)
                mViewModel.imagesToShow.add(this)
                showImages()
            }
        }
    }

    private fun registerGift() {
        if (mViewModel.isNew)
            submitCharity()
        else
            showPromptDialog(
                messageToShow = getString(R.string.edit_charity_warning_message),
                onPositiveClickCallback = { submitCharity() })
    }

    private fun submitCharity() {
        mViewModel.submitCharity(mPeakedUser).observe(this) { it ->
            when (it.status) {
                CustomResult.Status.LOADING -> showProgressDialog()
                CustomResult.Status.ERROR -> {
                    dismissProgressDialog()
                    it.errorMessage?.errorBody?.let {error -> showToastMessage(error) }
                }

                CustomResult.Status.SUCCESS -> {
                    dismissProgressDialog()
                    it.data?.let { model -> showSuccessDialog(model) }
                }
            }
        }
    }

    private fun showSuccessDialog(model: CharityModel) {
        showPromptDialog(messageToShow = getString(R.string.charity_submitted_successfully),
            showNegativeButton = false,
            positiveButtonText = getString(R.string.ok),
            onPositiveClickCallback = { response ->
                CharityDetailActivity.start(this, model)
                finish()
            }
        )
    }

    private fun initRecyclerView() {
        mAdapter = SelectedImagesAdapter().apply {
            onClickCallback = { position, url ->
                if (mViewModel.editableCharityModel == null && mViewModel.isNew) {
                    mViewModel.imagesToShow.removeAt(position)
                    mViewModel.imagesToUpload.removeAt(position)
                } else {
                    mViewModel.uploadedImagesAddress.removeAt(position)
                    mViewModel.imagesToShow.removeAt(position)
                }
                showImages()
            }
        }

        mAdapter.setHasStableIds(true)

        mBinding.selectedImagesRecyclerView.adapter = mAdapter

        val animator = mBinding.selectedImagesRecyclerView.itemAnimator

        if (animator is SimpleItemAnimator) {
            animator.supportsChangeAnimations = false
        }
    }

    private fun showImages() {
        if (::mAdapter.isInitialized) {
            mAdapter.setItems(mViewModel.imagesToShow)

            addNewPhotoContainer.visibility = if (mViewModel.imagesToShow.size > 0) View.GONE
            else View.VISIBLE
        }

        checkSubmitButtonEnabling()
    }

    private fun checkSubmitButtonEnabling() {
        mBinding.submitButton.isEnabled =
            (!mViewModel.mDescriptionLive.value.isNullOrEmpty() && mViewModel.mDescriptionLive.value!!.length >= 5) &&
                    (!mViewModel.mTitleLive.value.isNullOrEmpty() && mViewModel.mTitleLive.value!!.length >= 5) &&
                    mViewModel.imagesToShow.isNotEmpty()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                Config.RC_PICK_IMAGES -> managePeakedPhoto(data)
                REQUEST_CODE_PEAK_USER -> managePeakedUser(data.getSerializableExtra(UserListActivity.KEY_USER_DATA) as User)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun managePeakedPhoto(data: Intent) {
        data.getParcelableArrayListExtra<Image>(Config.EXTRA_IMAGES)?.let {
            mViewModel.imagesToShow.addAll(it.map { image -> image.path })
            mViewModel.imagesToUpload.addAll(it.map { image -> image.path })
            showImages()
        }
    }

    private fun managePeakedUser(user: User) {
        user.isCharity?.takeIf { it }?.also {
            Toast.makeText(this, getString(R.string.error_peak_wrong_user), Toast.LENGTH_LONG).show()
            return
        }

        mPeakedUser = user
        cvPeakUser.visibility = View.VISIBLE
        val userInfo = "${user.name}  ${user.phoneNumber}"
        tvPeakedUser.text = userInfo
    }

    private fun onRemovePeakedUser() {
        mPeakedUser = null
        cvPeakUser.visibility = View.GONE
    }

    private fun getUserCharity() {
        /*     mViewModel.getUserCharity().observe(this) {
                 when (it.status) {
                     CustomResult.Status.SUCCESS -> {

                     }
                     CustomResult.Status.ERROR -> {
                         if (viewModel.triedToFetchCharityAndUserProfile) {
                             finish()
                             return@observe
                         }

                         viewModel.triedToFetchCharityAndUserProfile = true
                         viewModel.isCharity = false
                         getUserProfile()
                     }
             }
         }*/

    }


}
