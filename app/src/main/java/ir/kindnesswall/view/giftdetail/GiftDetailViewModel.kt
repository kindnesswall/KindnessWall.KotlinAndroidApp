package ir.kindnesswall.view.giftdetail

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.kindnesswall.data.local.UserInfoPref
import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.data.model.ChatContactModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.repository.GiftRepo
import ir.kindnesswall.view.authentication.AuthenticationActivity

class GiftDetailViewModel(private val giftRepo: GiftRepo) : ViewModel() {
    var callPageStatus :Boolean = false
    var isDonatedToSomeone: Boolean = false

    var isReceivedGift: Boolean = false

    var isMyGift: Boolean = false
    var selectedImageIndex: Int = 0

    var giftModel: GiftModel? = null

    var giftViewListener: GiftViewListener? = null

    fun onBackButtonClicked() {
        giftViewListener?.onBackButtonClicked()
    }

    fun onRequestClicked() {
        if (giftModel?.donatedToUserId == UserInfoPref.userId) {
            giftViewListener?.onRequestClicked()
        } else if (giftModel?.donatedToUserId != null && giftModel?.donatedToUserId!! > 0) {
            giftViewListener?.onRequestClicked()
        } else if (isMyGift) {
            giftViewListener?.onEditButtonClicked()
        } else {
            giftViewListener?.onRequestClicked()
        }
    }

    fun onShareClicked() {
        giftViewListener?.onShareClicked()
    }
    fun onCallClick(view :View){
        giftViewListener?.onCallButtonClick(view)

        }
        fun callPage(){
            giftViewListener?.onCallPageClick()
        }
    fun onBookmarkClicked() {
        giftViewListener?.onBookmarkClicked()
    }

    fun onAcceptGiftClicked() {
        giftViewListener?.onAcceptGiftClicked()
    }

    fun onRejectGiftClicked() {
        giftViewListener?.onRejectGiftClicked()
    }

    fun onDeleteButtonClicked() {
        giftViewListener?.onDeleteButtonClicked()
    }

    fun requestGift(): LiveData<CustomResult<ChatContactModel>> {
        return giftRepo.requestGift(viewModelScope, giftModel?.id ?: 0)
    }

    fun rejectGift(giftId: Long, reason: String) =
        giftRepo.rejectGift(viewModelScope, giftId, reason)

    fun acceptGift(giftId: Long) = giftRepo.acceptGift(viewModelScope, giftId)

    fun getRequestStatus() = giftRepo.getGiftRequestStatus(viewModelScope, giftModel!!.id)

    fun deleteGift() = giftRepo.deleteGift(viewModelScope, giftModel!!.id)

    fun getSetting()=giftRepo.getSetting(viewModelScope , giftModel!!.userId)

    fun getUserNumber()=giftRepo.getUserNmber(viewModelScope,giftModel!!.userId)

}