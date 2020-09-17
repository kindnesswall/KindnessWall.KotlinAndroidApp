package ir.kindnesswall.view.main.charity.addcharity

import android.content.Context
import androidx.lifecycle.*
import ir.kindnesswall.data.local.dao.charity.CharityModel
import ir.kindnesswall.data.local.dao.submitrequest.RegisterCharityModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.model.UploadImageResponse
import ir.kindnesswall.data.model.user.User
import ir.kindnesswall.data.repository.CharityRepo
import ir.kindnesswall.data.repository.FileUploadRepo

class SubmitCharityViewModel(private val fileUploadRepo: FileUploadRepo, private val charityRepo: CharityRepo) : ViewModel() {
    var mTitleLive = MutableLiveData<String>()
    var mDescriptionLive = MutableLiveData<String>()
    var mPhoneNumberLive = MutableLiveData<String>()
    var mTelephoneNumberLive = MutableLiveData<String>()
    var mWebsiteLive = MutableLiveData<String>()
    var mEmailLive = MutableLiveData<String>()
    var mInstagramLive = MutableLiveData<String>()
    var mTelegramLive = MutableLiveData<String>()
    var mAddressLive = MutableLiveData<String>()

    var isNew = true
    var imagesToShow = arrayListOf<String>()
    var imagesToUpload = arrayListOf<String>()
    var uploadedImagesAddress = arrayListOf<String>()
    var uploadImagesLiveData = MutableLiveData<UploadImageResponse>()
    var editableCharityModel: CharityModel? = null


    fun getUserCharity(): LiveData<CustomResult<CharityModel>> {
        return charityRepo.getUserCharity(viewModelScope)
    }

    fun submitCharity(user: User): LiveData<CustomResult<CharityModel>> {
        val model = RegisterCharityModel().apply {
            name = mTitleLive.value
            description = mDescriptionLive.value
            address = mAddressLive.value
            email = mEmailLive.value
            instagram = mInstagramLive.value
            telegram = mTelegramLive.value
            mobileNumber = mPhoneNumberLive.value
            telephoneNumber = mTelephoneNumberLive.value
            website = mWebsiteLive.value
            imageUrl = uploadedImagesAddress[0]
            description = mDescriptionLive.value
        }

        val userId = user.id

        return if (isNew) {
            charityRepo.registerCharity(viewModelScope, userId, model)
        } else {
            model.id = editableCharityModel!!.id.toInt().toLong()
            charityRepo.updateCharity(viewModelScope, userId, model)
        }
    }

    fun uploadImages(context: Context, lifecycleOwner: LifecycleOwner) {
        fileUploadRepo.uploadFile(context, lifecycleOwner, imagesToUpload[0], uploadImagesLiveData)
    }

    fun clearData() {
        imagesToShow.clear()
        imagesToUpload.clear()
        uploadedImagesAddress.clear()
        isNew = true
    }

    fun onTitleTextChange(text: CharSequence) {
        mTitleLive.value = text.toString()
    }

    fun onDescriptionTextChange(text: CharSequence) {
        mDescriptionLive.value = text.toString()
    }

    fun onPhoneTextChange(text: CharSequence) {
        mPhoneNumberLive.value = text.toString()
    }

    fun onTelephoneTextChange(text: CharSequence) {
        mTelephoneNumberLive.value = text.toString()
    }

    fun onWebsiteTextChange(text: CharSequence) {
        mWebsiteLive.value = text.toString()
    }

    fun onEmailTextChange(text: CharSequence) {
        mEmailLive.value = text.toString()
    }

    fun onInstagramTextChange(text: CharSequence) {
        mInstagramLive.value = text.toString()
    }

    fun onTelegramTextChange(text: CharSequence) {
        mTelegramLive.value = text.toString()
    }

    fun onAddressTextChange(text: CharSequence) {
        mAddressLive.value = text.toString()
    }
}