package com.farshidabz.kindnesswall.view.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farshidabz.kindnesswall.data.model.CustomResult
import com.farshidabz.kindnesswall.data.model.LoginResponseModel
import com.farshidabz.kindnesswall.data.model.user.User
import com.farshidabz.kindnesswall.data.repository.AuthRepo
import com.farshidabz.kindnesswall.data.repository.UserRepo


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

class AuthenticationViewModel(private val authRepo: AuthRepo, private val userRepo: UserRepo) :
    ViewModel() {
    var phoneNumber = MutableLiveData<String>()

    /**
     * a function for intercepting mobile number change and send it to view class
     */
    fun onPhoneNumberChanged(text: CharSequence) {
        phoneNumber.postValue(text.toString())
    }

    fun registerUser(): LiveData<CustomResult<Any>> {
        return authRepo.registerUser(viewModelScope, phoneNumber.value ?: "")
    }

    fun loginUser(verificationCode: String): LiveData<CustomResult<LoginResponseModel>> {
        return authRepo.loginUser(viewModelScope, phoneNumber.value ?: "", verificationCode)
    }

    fun getUserProfile(): LiveData<CustomResult<User>> {
        return userRepo.getUserProfile(viewModelScope)
    }

    fun updateUserProfile(userName: String, image: String): LiveData<CustomResult<Any>> {
        return userRepo.updateUserProfile(viewModelScope, userName, image)
    }
}