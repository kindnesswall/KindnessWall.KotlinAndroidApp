package ir.kindnesswall.view.main.addproduct

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.kindnesswall.data.local.AppPref
import ir.kindnesswall.data.local.dao.AppDatabase
import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.data.local.dao.submitrequest.RegisterGiftRequestModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.model.PhoneVisibility
import ir.kindnesswall.data.model.UploadImageResponse
import ir.kindnesswall.data.repository.FileUploadRepo
import ir.kindnesswall.data.repository.GiftRepo
import ir.kindnesswall.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.math.BigDecimal

class SubmitGiftViewModel(
    private val fileUploadRepo: FileUploadRepo,
    private val giftRepo: GiftRepo,
    private val appDatabase: AppDatabase
) : ViewModel() {
    var hasRegion: Boolean = false
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

    private val _images = mutableListOf<GiftImage>()
    val images: List<GiftImage> = _images

    var uploadImagesLiveData = MutableLiveData<UploadImageResponse>()

    var editableGiftModel: GiftModel? = null

    private val _phoneVisibilityLiveData = MediatorLiveData<PhoneVisibility?>()
    val phoneVisibilityLiveData: LiveData<PhoneVisibility?> get() = _phoneVisibilityLiveData

    init {
        getPhoneVisibility()
    }

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
        val registerGiftRequestModel = RegisterGiftRequestModel(
            title = title.value ?: "",
            description = description.value ?: "",
            price = price.value?.toBigDecimal() ?: BigDecimal.ZERO,
            giftImages = images.filterIsInstance<GiftImage.OnlineImage>().map { it.url },
            categoryId = categoryId.value?.toInt() ?: 0,
            provinceId = provinceId.value?.toInt() ?: 0,
            regionId = regionId.value?.toInt(),
            regionName = null,
            cityId = cityId.value?.toInt() ?: 0,
            categoryName = null,
            provinceName = null,
            cityName = null,
            isNew = isNew,
            isBackup = null
        )

        return if (isNew) {
            giftRepo.registerGift(viewModelScope, registerGiftRequestModel)
        } else {
            registerGiftRequestModel.id = editableGiftModel!!.id.toInt()
            giftRepo.updateGift(viewModelScope, registerGiftRequestModel)
        }
    }

    fun uploadImages(context: Context, lifecycleOwner: LifecycleOwner) {
        viewModelScope.launch {
            uploadImagesLiveData.value = fileUploadRepo.uploadFile(
                context,
                images.first { it is GiftImage.LocalImage }.let {
                    (it as GiftImage.LocalImage).uri
                }
            )
        }
    }

    fun applyUploadedLocalImage(address: String) {
        val uploadedImageIndex = images.indexOfFirst { it is GiftImage.LocalImage }

        _images[uploadedImageIndex] = GiftImage.OnlineImage(address)
    }

    fun addLocalImages(images: List<GiftImage.LocalImage>) {
        _images.addAll(images)
    }

    fun fillByOnlineImages(list: List<GiftImage.OnlineImage>) {
        _images.clear()
        _images.addAll(list)
    }

    fun fillByLocalImages(list: List<GiftImage.LocalImage>) {
        _images.clear()
        _images.addAll(list)
    }

    fun deleteImage(position: Int) {
        _images.removeAt(position)
    }

    private val _openCameraLiveData = MutableLiveData<Event<Uri>?>()
    val openCameraLiveData: LiveData<Event<Uri>?> get() = _openCameraLiveData
    private var isProcessingOpeningCamera = false

    fun attemptOpenCamera(context: Context) {
        if (isProcessingOpeningCamera) return

        isProcessingOpeningCamera = true
        viewModelScope.launch(Dispatchers.IO) {
            val directory = context.filesDir.resolve("camera_temporary")
            directory.mkdirs()

            val tempFile = File.createTempFile("pending_image", ".jpg", directory)

            val uri = FileProvider.getUriForFile(
                context,
                context.applicationContext.packageName.plus(".fileprovider"),
                tempFile
            )

            _openCameraLiveData.postValue(Event(uri))
            isProcessingOpeningCamera = false
        }
    }

    fun submitPendingImages() {
        val pendingUri = openCameraLiveData.value?.peekContent() ?: kotlin.run {
            Timber.w("submitting image occurred in a illegal state")
            return
        }

        _images.add(GiftImage.LocalImage(pendingUri))

        _openCameraLiveData.value = null
    }

    fun backupData(callback: (Boolean) -> Unit) {
        val registerGiftRequestModel = RegisterGiftRequestModel(
        title = title.value ?: "",
        description = description.value ?: "",
            giftImages = images.filterIsInstance<GiftImage.LocalImage>()
                .map { it.uri.toString() },
        categoryId = categoryId.value?.toInt() ?: 0,
        categoryName = categoryName.value ?: "",
        provinceId = provinceId.value?.toInt() ?: 0,
        provinceName = provinceName.value ?: "",
        regionId = regionId.value?.toInt(),
        regionName = regionName.value ?: "",
        cityId = cityId.value?.toInt() ?: 0,
        cityName = cityName.value ?: "",
        countryId = AppPref.countryId,
        isNew = isNew,
        isBackup = true,
        price =
            if (price.value.isNullOrEmpty()) BigDecimal.ZERO else price.value!!.toBigDecimal()
        )
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

        _images.clear()

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

    fun setPhoneVisibility(phoneVisibility: PhoneVisibility) {
        _phoneVisibilityLiveData.addSource(
            giftRepo.setSettingNumber(viewModelScope.coroutineContext, phoneVisibility)
        ) {
            when (it.status) {
                CustomResult.Status.SUCCESS -> getPhoneVisibility()
                CustomResult.Status.ERROR -> getPhoneVisibility()
                CustomResult.Status.LOADING -> {}
            }
        }
    }

    private fun getPhoneVisibility() {
        _phoneVisibilityLiveData.addSource(
            giftRepo.getSettingNumber(viewModelScope.coroutineContext)
        ) { _phoneVisibilityLiveData.value = it.data }
    }

    fun refreshPhoneVisibility() {
        getPhoneVisibility()
    }
}