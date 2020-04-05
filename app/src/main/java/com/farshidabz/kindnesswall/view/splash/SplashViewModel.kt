package com.farshidabz.kindnesswall.view.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farshidabz.kindnesswall.data.model.CustomResult
import com.farshidabz.kindnesswall.data.model.user.User
import com.farshidabz.kindnesswall.data.repository.UserRepo

class SplashViewModel(private val userRepo: UserRepo) : ViewModel() {

    fun getUserProfile(): LiveData<CustomResult<User>> {
        return userRepo.getUserProfile(viewModelScope)
    }
}