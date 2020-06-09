package ir.kindnesswall.view.main.reviewcharity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.repository.GiftRepo

class ReviewCharityViewModel(private val giftRepo: GiftRepo) : ViewModel() {
    var clickedItemPosition: Int = -1
    private var lastId = 0L

    val reviewItem = ArrayList<GiftModel>()

    fun getReviewItemsFirstPage(): LiveData<CustomResult<List<GiftModel>>> {
        return giftRepo.getReviewGiftsFirstPage(viewModelScope)
    }

    fun getReviewItemsFromServer(): LiveData<CustomResult<List<GiftModel>>> {
        lastId = reviewItem.last().id
        return giftRepo.getReviewGifts(viewModelScope, lastId)
    }

    fun rejectGift(giftId: Long, reason: String) = giftRepo.rejectGift(viewModelScope, giftId, reason)

    fun acceptGift(giftId: Long) = giftRepo.acceptGift(viewModelScope, giftId)
}