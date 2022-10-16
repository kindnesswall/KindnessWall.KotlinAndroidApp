package ir.kindnesswall.view.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.kindnesswall.data.repositories.auth.AuthRemoteDataSource
import ir.kindnesswall.data.repositories.user.UserDataSource
import ir.kindnesswall.domain.common.CustomResult
import ir.kindnesswall.domain.entities.LoginResponseModel


/**
 * Created by farshid.abazari since 2019-10-31
 *
 * Usage:
 *
 * How to call:
 *
 * Useful parameter:
 *
 */

class AuthenticationViewModel(private val authRepo: AuthRemoteDataSource, private val userRepo: UserDataSource) :
    ViewModel() {
    var phoneNumber = MutableLiveData<String>()

    /**
     * a function for intercepting mobile number change and send it to view class
     */
    fun onPhoneNumberChanged(text: CharSequence) {
        phoneNumber.postValue(text.toString())
    }

    fun registerUser(): LiveData<CustomResult<Any?>> {
        return authRepo.registerUser(viewModelScope, phoneNumber.value ?: "")
    }

    fun loginUser(verificationCode: String): LiveData<CustomResult<LoginResponseModel>> {
        return authRepo.loginUser(viewModelScope, phoneNumber.value ?: "", verificationCode)
    }

    fun getUserProfile(): LiveData<ir.kindnesswall.domain.common.CustomResult<ir.kindnesswall.domain.entities.User>> {
        return userRepo.getUserProfile(viewModelScope)
    }

    fun updateUserProfile(userName: String, image: String): LiveData<ir.kindnesswall.domain.common.CustomResult<Any>> {
        return userRepo.updateUserProfile(viewModelScope, userName, image)
    }
}