package ir.kindnesswall.view.main.addproduct

import android.content.Context
import androidx.lifecycle.*
import ir.kindnesswall.data.local.AppPref
import ir.kindnesswall.data.local.dao.AppDatabase
import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.data.local.dao.submitrequest.RegisterGiftRequestModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.model.UploadImageResponse
import ir.kindnesswall.data.repository.FileUploadRepo
import ir.kindnesswall.data.repository.GiftRepo
import kotlinx.coroutines.launch
import java.math.BigDecimal

class SubmitGiftViewModel(
    private val fileUploadRepo: FileUploadRepo,
    private val giftRepo: GiftRepo,
    private val appDatabase: AppDatabase
) : ViewModel() {
    var title = MutableLiveData<String>()
    var description = MutableLiveData<String>()
    var price = MutableLiveData<String>()

    var backupDataLiveData = MutableLiveData<RegisterGiftRequestModel>()

    var categoryId = MutableLiveData<Int>()
    var categoryName = MutableLiveData<String>()

    var regionId = MutableLiveData<Int>()
    var regionName = MutableLiveData<String>()

    var provinceId = MutableLiveData<Int>()
    var provinceName = MutableLiveData<String>()

    var cityId = MutableLiveData<Int>()
    var cityName = MutableLiveData<String>()

    var isNew = true

    var imagesToShow = arrayListOf<String>()
    var imagesToUpload = arrayListOf<String>()

    var uploadedImagesAddress = arrayListOf<String>()

    var uploadImagesLiveData = MutableLiveData<UploadImageResponse>()

    var editableGiftModel: GiftModel? = null

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
        registerGiftRequestModel.price = price.value?.toBigDecimal() ?: BigDecimal.ZERO
        registerGiftRequestModel.giftImages.addAll(uploadedImagesAddress)
        registerGiftRequestModel.categoryId = categoryId.value?.toInt() ?: 0
        registerGiftRequestModel.provinceId = provinceId.value?.toInt() ?: 0
        registerGiftRequestModel.regionId = regionId.value?.toInt() ?: 0
        registerGiftRequestModel.regionName = null
        registerGiftRequestModel.cityId = cityId.value?.toInt() ?: 0
        registerGiftRequestModel.categoryName = null
        registerGiftRequestModel.provinceName = null
        registerGiftRequestModel.cityName = null
        registerGiftRequestModel.isNew = isNew
        registerGiftRequestModel.isBackup = null

        return if (isNew) {
            giftRepo.registerGift(viewModelScope, registerGiftRequestModel)
        } else {
            registerGiftRequestModel.id = editableGiftModel!!.id.toInt()
            giftRepo.updateGift(viewModelScope, registerGiftRequestModel)
        }
    }

    fun uploadImages(context: Context, lifecycleOwner: LifecycleOwner) {
        fileUploadRepo.uploadFile(context, lifecycleOwner, imagesToUpload[0], uploadImagesLiveData)
    }

    fun backupData(callback: (Boolean) -> Unit) {
        val registerGiftRequestModel = RegisterGiftRequestModel()
        registerGiftRequestModel.title = title.value ?: ""
        registerGiftRequestModel.description = description.value ?: ""
        registerGiftRequestModel.giftImages.addAll(imagesToShow)
        registerGiftRequestModel.categoryId = categoryId.value?.toInt() ?: 0
        registerGiftRequestModel.categoryName = categoryName.value ?: ""
        registerGiftRequestModel.provinceId = provinceId.value?.toInt() ?: 0
        registerGiftRequestModel.provinceName = provinceName.value ?: ""
        registerGiftRequestModel.regionId = regionId.value?.toInt() ?: 0
        registerGiftRequestModel.regionName = regionName.value ?: ""
        registerGiftRequestModel.cityId = cityId.value?.toInt() ?: 0
        registerGiftRequestModel.cityName = cityName.value ?: ""
        registerGiftRequestModel.countryId = AppPref.countryId
        registerGiftRequestModel.isNew = isNew
        registerGiftRequestModel.isBackup = true

        registerGiftRequestModel.price =
            if (price.value.isNullOrEmpty()) BigDecimal.ZERO else price.value!!.toBigDecimal()

        viewModelScope.launch {
            appDatabase.registerGiftRequestDao().insert(registerGiftRequestModel)
            callback.invoke(true)
        }
    }

    fun getBackUpData(): MutableLiveData<RegisterGiftRequestModel> {
        backupDataLiveData.value = appDatabase.registerGiftRequestDao().getItem()
        return backupDataLiveData
    }

    fun removeBackupData() {
        appDatabase.registerGiftRequestDao().delete()
    }

    fun clearData() {
        appDatabase.registerGiftRequestDao().delete()

        imagesToShow.clear()
        imagesToUpload.clear()
        uploadedImagesAddress.clear()

        categoryId.value = 0
        categoryName.value = ""
        provinceId.value = 0
        provinceName.value = ""
        regionId.value = 0
        regionName.value = ""
        cityId.value = 0
        cityName.value = ""
        isNew = true
    }
}