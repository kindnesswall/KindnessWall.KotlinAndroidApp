package com.farshidabz.kindnesswall.view.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farshidabz.kindnesswall.annotation.Filter
import com.farshidabz.kindnesswall.data.local.UserInfoPref
import com.farshidabz.kindnesswall.data.local.dao.catalog.GiftModel
import com.farshidabz.kindnesswall.data.model.CustomResult
import com.farshidabz.kindnesswall.data.repository.UserRepo

class MyProfileViewModel(private val userRepo: UserRepo) : ViewModel() {
    var currentFilter = Filter.REGISTERED

    fun getGifts(): LiveData<CustomResult<List<GiftModel>>> {
        return when (currentFilter) {
            Filter.REGISTERED -> userRepo.getUserReceivedGifts(viewModelScope, UserInfoPref.userId)
            Filter.DONATED -> userRepo.getUserReceivedGifts(viewModelScope, UserInfoPref.userId)
            Filter.RECEIVED -> userRepo.getUserReceivedGifts(viewModelScope, UserInfoPref.userId)
            else -> userRepo.getUserReceivedGifts(viewModelScope, UserInfoPref.userId)
        }
    }
}