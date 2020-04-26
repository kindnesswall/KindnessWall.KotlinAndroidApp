package ir.kindnesswall.view.main.charity.charitydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.kindnesswall.data.local.dao.charity.CharityModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.model.user.User
import ir.kindnesswall.data.repository.UserRepo

class CharityViewModel(private val userRepo: UserRepo) : ViewModel() {
    var charityModel: CharityModel? = null

    var charityViewListener: CharityViewListener? = null

    fun onBackButtonClicked() {
        charityViewListener?.onBackButtonClicked()
    }

    fun onStartChatClicked() {
        charityViewListener?.onStartChatClicked()
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