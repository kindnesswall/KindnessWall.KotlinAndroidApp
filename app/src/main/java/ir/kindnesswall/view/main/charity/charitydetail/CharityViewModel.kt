package ir.kindnesswall.view.main.charity.charitydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.kindnesswall.data.local.dao.charity.CharityModel
import ir.kindnesswall.data.repositories.chat.ChatRemoteDataSource
import ir.kindnesswall.data.repositories.gift.GiftDataSource
import ir.kindnesswall.data.repositories.user.UserDataSource
import ir.kindnesswall.domain.common.CustomResult

class CharityViewModel(
    private val userRepo: UserDataSource,
    private val giftRepo: GiftDataSource,
    private val chatRepo: ChatRemoteDataSource
) :
    ViewModel() {
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

    fun onRating() {
        charityViewListener?.onRatingClick()
    }

    fun getUserInformation(): LiveData<CustomResult<ir.kindnesswall.domain.entities.User>> =
        userRepo.getOtherUsersProfile(viewModelScope, charityModel?.userId)

    fun getChatId(): LiveData<CustomResult<ir.kindnesswall.domain.entities.ChatContactModel>> {
        return chatRepo.getChatId(viewModelScope, charityModel?.userId ?: 0)
    }
}