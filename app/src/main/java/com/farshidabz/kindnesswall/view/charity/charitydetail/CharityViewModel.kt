package com.farshidabz.kindnesswall.view.charity.charitydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farshidabz.kindnesswall.data.local.dao.charity.CharityModel
import com.farshidabz.kindnesswall.data.model.CustomResult
import com.farshidabz.kindnesswall.data.model.user.User
import com.farshidabz.kindnesswall.data.repository.UserRepo

class CharityViewModel(private val userRepo: UserRepo) : ViewModel() {
    var charityModel: CharityModel? = null

    public var charityViewListener: CharityViewListener? = null

    public fun onBackButtonClicked() {
        charityViewListener?.onBackButtonClicked()
    }

    public fun onRequestClicked() {
        charityViewListener?.onRequestClicked()
    }

    public fun onShareClicked() {
        charityViewListener?.onShareClicked()
    }

    public fun onBookmarkClicked() {
        charityViewListener?.onBookmarkClicked()
    }

    fun getUserInformation(): LiveData<CustomResult<User>> =
        userRepo.getOtherUsersProfile(viewModelScope, charityModel?.userId)
}