package ir.kindnesswall.view.profile

import android.content.Context
import androidx.lifecycle.*
import ir.kindnesswall.annotation.Filter
import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.model.UploadImageResponse
import ir.kindnesswall.data.model.user.User
import ir.kindnesswall.data.repository.FileUploadRepo
import ir.kindnesswall.data.repository.UserRepo
import java.io.File

class MyProfileViewModel(
    private val userRepo: UserRepo,
    private val fileUploadRepo: FileUploadRepo
) : ViewModel() {
    var gifts: List<GiftModel>? = arrayListOf()
    lateinit var user: User
    var newUserName: String = ""
    var newImageUrlLiveData = MutableLiveData<UploadImageResponse>()

    var selectedImageFile: File? = null
    lateinit var selectedImagePath: String

    var currentFilter = Filter.REGISTERED

    fun getGifts(): LiveData<CustomResult<List<GiftModel>>> {
        return when (currentFilter) {
            Filter.REGISTERED -> userRepo.getUserRegisteredGifts(viewModelScope, user.id)
            Filter.DONATED -> userRepo.getUserDonatedGifts(viewModelScope, user.id)
            Filter.RECEIVED -> userRepo.getUserReceivedGifts(viewModelScope, user.id)
            Filter.ACCEPTED -> userRepo.getUserAcceptedGifts(viewModelScope, user.id)
            Filter.REJECTED -> userRepo.getUserRejectedGifts(viewModelScope, user.id)
            else -> userRepo.getUserReceivedGifts(viewModelScope, user.id)
        }
    }

    fun uploadImage(context: Context, lifecycleOwner: LifecycleOwner) {
        fileUploadRepo.uploadFile(context, lifecycleOwner, selectedImagePath, newImageUrlLiveData)
    }

    fun updateUserProfile(): LiveData<CustomResult<Any>> {
        return userRepo.updateUserProfile(
            viewModelScope,
            newUserName,
            newImageUrlLiveData.value?.address ?: ""
        )
    }

    fun onNameTextChanged(text: CharSequence) {
        newUserName = text.toString()
    }
}