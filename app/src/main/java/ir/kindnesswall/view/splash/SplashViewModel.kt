package ir.kindnesswall.view.splash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.kindnesswall.data.model.ChatModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.model.user.User
import ir.kindnesswall.data.repository.GeneralRepo
import ir.kindnesswall.data.repository.UserRepo

class SplashViewModel(private val userRepo: UserRepo, private val generalRepo: GeneralRepo) :
    ViewModel() {

    var requestChatModel: ChatModel? = null
    var isStartFromNotification: Boolean = false

    fun getUserProfile(): LiveData<CustomResult<User>> {
        Log.i("56456465456", "Run")
        return userRepo.getUserProfile(viewModelScope)
    }

    fun getVersion() = generalRepo.getVersion(viewModelScope)
    fun getSetting() = generalRepo.getSetting(viewModelScope);
}