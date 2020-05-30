package ir.kindnesswall.view.main.catalog.cataloglist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.repository.GiftRepo

class CatalogViewModel(private val giftRepo: GiftRepo) : ViewModel() {
    private var lastId = Long.MAX_VALUE

    val catalogItems = ArrayList<GiftModel>()
    
    fun getCatalogItemsFromServer(): LiveData<CustomResult<List<GiftModel>>> {
        lastId = if (catalogItems.isNullOrEmpty()) {
            Long.MAX_VALUE
        } else {
            catalogItems.last().id
        }
        return giftRepo.getGifts(viewModelScope, lastId)
    }
}