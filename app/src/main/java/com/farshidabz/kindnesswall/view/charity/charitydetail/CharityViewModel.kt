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

    var charityViewListener: CharityViewListener? = null

    fun onBackButtonClicked() {
        charityViewListener?.onBackButtonClicked()
    }

    fun onRequestClicked() {
        charityViewListener?.onRequestClicked()
    }

    fun onShareClicked() {
        charityViewListener?.onShareClicked()
    }

    fun onBookmarkClicked() {
        charityViewListener?.onBookmarkClicked()
    }

    fun onCallClicked() {
        charityViewListener?.onCallClicked()
    }

    fun onTelegramClicked() {
        charityViewListener?.onTelegramClicked()
    }

    fun onInstagramClicked() {
        charityViewListener?.onInstagramClicked()
    }

    fun onEmailClicked() {
        charityViewListener?.onEmailClicked()
    }

    fun onWebsiteClicked() {
        charityViewListener?.onWebsiteClicked()
    }

    fun getUserInformation(): LiveData<CustomResult<User>> =
        userRepo.getOtherUsersProfile(viewModelScope, charityModel?.userId)
}