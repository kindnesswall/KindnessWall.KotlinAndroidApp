package com.farshidabz.kindnesswall.view.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.farshidabz.kindnesswall.BuildConfig
import com.farshidabz.kindnesswall.annotation.Filter
import com.farshidabz.kindnesswall.data.local.UserInfoPref
import com.farshidabz.kindnesswall.data.local.dao.catalog.GiftModel
import com.farshidabz.kindnesswall.data.model.CustomResult
import com.farshidabz.kindnesswall.data.model.UploadImageResponse
import com.farshidabz.kindnesswall.data.repository.FileUploadRepo
import com.farshidabz.kindnesswall.data.repository.UserRepo
import com.farshidabz.kindnesswall.utils.wrapInBearer
import com.google.gson.Gson
import net.gotev.uploadservice.data.UploadInfo
import net.gotev.uploadservice.network.ServerResponse
import net.gotev.uploadservice.observer.request.RequestObserverDelegate
import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest
import java.io.File

class MyProfileViewModel(private val userRepo: UserRepo, private val fileUploadRepo: FileUploadRepo) : ViewModel() {
    val newUserName: String = ""
    var newImageUrlLiveData =
        MutableLiveData<UploadImageResponse>().apply { value = UploadImageResponse("") }

    var selectedImageFile: File? = null
    lateinit var selectedImagePath: String

    var currentFilter = Filter.REGISTERED

    fun getGifts(): LiveData<CustomResult<List<GiftModel>>> {
        return when (currentFilter) {
            Filter.REGISTERED -> userRepo.getUserRegisteredGifts(
                viewModelScope,
                UserInfoPref.userId
            )

            Filter.DONATED -> userRepo.getUserDonatedGifts(viewModelScope, UserInfoPref.userId)
            Filter.RECEIVED -> userRepo.getUserReceivedGifts(viewModelScope, UserInfoPref.userId)
            else -> userRepo.getUserReceivedGifts(viewModelScope, UserInfoPref.userId)
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
}