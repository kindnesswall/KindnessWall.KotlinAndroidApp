package ir.kindnesswall.view.main.conversation.chat.todonategifts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.kindnesswall.data.db.dao.catalog.GiftModel
import ir.kindnesswall.data.repositories.gift.GiftDataSource

class DonateGiftViewModel(private val giftRepo: GiftDataSource) : ViewModel() {
    var contactId: Long = 0
    var giftsList: ArrayList<GiftModel>? = ArrayList()

    fun donateGift(item: GiftModel) = giftRepo.donateGift(viewModelScope, item.id, contactId)
}