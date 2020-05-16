package ir.kindnesswall.view.giftdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.model.RequestChatModel
import ir.kindnesswall.data.repository.GiftRepo

class GiftDetailViewModel(private val giftRepo: GiftRepo) : ViewModel() {

    var isMyGift: Boolean = false
    var selectedImageIndex: Int = 0

    var giftModel: GiftModel? = null

    var giftViewListener: GiftViewListener? = null

    fun onBackButtonClicked() {
        giftViewListener?.onBackButtonClicked()
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

    fun requestGift(): LiveData<CustomResult<RequestChatModel>> {
        return giftRepo.requestGift(viewModelScope, giftModel?.id ?: 0)
    }
}