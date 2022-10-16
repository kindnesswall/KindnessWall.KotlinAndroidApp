package ir.kindnesswall.view.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.kindnesswall.data.repository.GeneralRepo
import ir.kindnesswall.data.repositories.user.UserDataSource
import ir.kindnesswall.domain.entities.ChatModel

class SplashViewModel(private val userRepo: UserDataSource, private val generalRepo: GeneralRepo) :
    ViewModel() {

    var requestChatModel: ChatModel? = null
    var isStartFromNotification: Boolean = false

    fun getUserProfile(): LiveData<ir.kindnesswall.domain.common.CustomResult<ir.kindnesswall.domain.entities.User>> {
        return userRepo.getUserProfile(viewModelScope)
    }

    fun getVersion() = generalRepo.getVersion(viewModelScope)
    fun getSetting() = generalRepo.getSetting(viewModelScope);
}