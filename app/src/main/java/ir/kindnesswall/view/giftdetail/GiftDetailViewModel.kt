package ir.kindnesswall.view.giftdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.data.model.ChatModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.repository.GiftRepo

class GiftDetailViewModel(private val giftRepo: GiftRepo) : ViewModel() {

    var isReceivedGift: Boolean = false

    var isMyGift: Boolean = false
    var selectedImageIndex: Int = 0

    var giftModel: GiftModel? = null

    var giftViewListener: GiftViewListener? = null

    fun onBackButtonClicked() {
        giftViewListener?.onBackButtonClicked()
    }

    fun onEditButtonClicked() {
        giftViewListener?.onEditButtonClicked()
    }

    fun onRequestClicked() {
        giftViewListener?.onRequestClicked()
    }

    fun onShareClicked() {
        giftViewListener?.onShareClicked()
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

    fun requestGift(): LiveData<CustomResult<ChatModel>> {
        return giftRepo.requestGift(viewModelScope, giftModel?.id ?: 0)
    }

    fun rejectGift(giftId: Long, reason: String) =
        giftRepo.rejectGift(viewModelScope, giftId, reason)

    fun acceptGift(giftId: Long) = giftRepo.acceptGift(viewModelScope, giftId)

    fun getRequestStatus() = giftRepo.getGiftRequestStatus(viewModelScope, giftModel!!.id)
}