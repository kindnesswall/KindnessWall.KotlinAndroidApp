package ir.kindnesswall.view.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.*
import ir.kindnesswall.annotation.Filter
import ir.kindnesswall.data.db.dao.catalog.GiftModel
import ir.kindnesswall.data.model.UploadImageResponse
import ir.kindnesswall.data.repository.FileUploadRepo
import ir.kindnesswall.data.repositories.user.UserDataSource
import ir.kindnesswall.domain.entities.User
import kotlinx.coroutines.launch

class MyProfileViewModel(
    private val userRepo: UserDataSource,
    private val fileUploadRepo: FileUploadRepo
) : ViewModel() {
    var gifts: List<GiftModel>? = arrayListOf()
    lateinit var user: User
    var newUserName: String = ""
    var newImageUrlLiveData = MutableLiveData<UploadImageResponse>()

    var selectedImageUri: Uri? = null

    var currentFilter = Filter.REGISTERED

    fun getGifts(): LiveData<ir.kindnesswall.domain.common.CustomResult<List<ir.kindnesswall.data.db.dao.catalog.GiftModel>>> {
        return when (currentFilter) {
            Filter.REGISTERED -> userRepo.getUserRegisteredGifts(viewModelScope, user.id)
            Filter.DONATED -> userRepo.getUserDonatedGifts(viewModelScope, user.id)
            Filter.RECEIVED -> userRepo.getUserReceivedGifts(viewModelScope, user.id)
            //Filter.ACCEPTED -> userRepo.getUserAcceptedGifts(viewModelScope, user.id)
            //Filter.REJECTED -> userRepo.getUserRejectedGifts(viewModelScope, user.id)
            else -> userRepo.getUserReceivedGifts(viewModelScope, user.id)
        }
    }

    fun uploadImage(context: Context, lifecycleOwner: LifecycleOwner, uri: Uri) {
        viewModelScope.launch {
            newImageUrlLiveData.value = fileUploadRepo.uploadFile(context, uri)
        }
    }

    fun updateUserProfile(): LiveData<ir.kindnesswall.domain.common.CustomResult<Any>> {

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