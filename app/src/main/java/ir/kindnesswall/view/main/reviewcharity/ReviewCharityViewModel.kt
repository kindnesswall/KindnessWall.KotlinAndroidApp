package ir.kindnesswall.view.main.reviewcharity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.data.local.dao.charity.CharityModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.repository.CharityRepo
import ir.kindnesswall.data.repository.GiftRepo

class ReviewCharityViewModel(private val charityRepo: CharityRepo) : ViewModel() {
    var clickedItemPosition: Int = -1
    private var lastId = 0L

    val reviewItem = ArrayList<CharityModel>()

    fun getReviewItemsFirstPage(): LiveData<CustomResult<List<CharityModel>>> {
            return charityRepo.getReviewCharityFirstPage(viewModelScope)
    }

    fun getReviewItemsFromServer(): LiveData<CustomResult<List<CharityModel>>> {
        return charityRepo.getReviewCharity(viewModelScope)
    }

    fun rejectCharity(giftId: Long, reason: String) = charityRepo.rejectCharity(viewModelScope, giftId, reason)

    fun acceptCharity(giftId: Long) = charityRepo.acceptCharity(viewModelScope, giftId)
}