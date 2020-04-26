package ir.kindnesswall.view.main.catalog.cataloglist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.repository.GiftRepo

class CatalogViewModel(private val giftRepo: GiftRepo) : ViewModel() {
    private var lastId = 0L

    val catalogItems: LiveData<CustomResult<List<GiftModel>>> by lazy {
        giftRepo.getGiftsFirstPage(viewModelScope)
    }

    fun getCatalogItemsFromServer(): LiveData<CustomResult<List<GiftModel>>> {
        lastId = catalogItems.value?.data?.last()?.id ?: 0
        return giftRepo.getGifts(viewModelScope, lastId)
    }
}