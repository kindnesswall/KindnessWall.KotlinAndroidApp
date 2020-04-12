package com.farshidabz.kindnesswall.view.addproduct

import android.content.Context
import androidx.lifecycle.*
import com.farshidabz.kindnesswall.data.local.dao.catalog.GiftModel
import com.farshidabz.kindnesswall.data.model.CustomResult
import com.farshidabz.kindnesswall.data.model.UploadImageResponse
import com.farshidabz.kindnesswall.data.model.requestsmodel.RegisterGiftRequestModel
import com.farshidabz.kindnesswall.data.repository.FileUploadRepo
import com.farshidabz.kindnesswall.data.repository.GiftRepo

class SubmitGiftViewModel(
    private val fileUploadRepo: FileUploadRepo,
    private val giftRepo: GiftRepo
) : ViewModel() {
    var title = MutableLiveData<String>()
    var description = MutableLiveData<String>()
    var price = MutableLiveData<String>()

    var categoryId = MutableLiveData<Int>()

    var provinceId = MutableLiveData<Int>()
    var cityId = MutableLiveData<Int>()

    var isNew = true

    var selectedImages = arrayListOf<String>()
    var imagesToUpload = arrayListOf<String>()

    var uploadedImagesAddress = arrayListOf<String>()

    var uploadImagesLiveData = MutableLiveData<UploadImageResponse>()

    fun onTitleTextChange(text: CharSequence) {
        title.value = text.toString()
    }

    fun onDescriptionTextChange(text: CharSequence) {
        description.value = text.toString()
    }

    fun onPriceTextChange(text: CharSequence) {
        price.value = text.toString()
    }

    fun submitGift(): LiveData<CustomResult<GiftModel>> {
        val registerGiftRequestModel = RegisterGiftRequestModel()
        registerGiftRequestModel.title = title.value ?: ""
        registerGiftRequestModel.description = description.value ?: ""
        registerGiftRequestModel.price = price.value?.toInt() ?: 0
        registerGiftRequestModel.giftImages.addAll(uploadedImagesAddress)
        registerGiftRequestModel.categoryId = categoryId.value?.toInt() ?: 0
        registerGiftRequestModel.provinceId = provinceId.value?.toInt() ?: 0
        registerGiftRequestModel.cityId = cityId.value?.toInt() ?: 0
        registerGiftRequestModel.isNew = isNew

        return giftRepo.registerGift(viewModelScope, registerGiftRequestModel)
    }

    fun uploadImages(context: Context, lifecycleOwner: LifecycleOwner) {
        fileUploadRepo.uploadFile(context, lifecycleOwner, imagesToUpload[0], uploadImagesLiveData)
    }
}