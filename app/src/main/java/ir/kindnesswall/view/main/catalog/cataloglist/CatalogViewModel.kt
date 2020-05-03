package ir.kindnesswall.view.main.catalog.cataloglist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.repository.GiftRepo

class CatalogViewModel(private val giftRepo: GiftRepo) : ViewModel() {
    private var lastId = 0L

    val catalogItems = ArrayList<GiftModel>()

    fun getCatalogItemsFirstPage(): LiveData<CustomResult<List<GiftModel>>> {
        return giftRepo.getGiftsFirstPage(viewModelScope)
    }

    fun getCatalogItemsFromServer(): LiveData<CustomResult<List<GiftModel>>> {
        lastId = catalogItems.last().id
        return giftRepo.getGifts(viewModelScope, lastId)
    }
}