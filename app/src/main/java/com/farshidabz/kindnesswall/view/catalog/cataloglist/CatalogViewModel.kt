package com.farshidabz.kindnesswall.view.catalog.cataloglist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farshidabz.kindnesswall.data.local.dao.catalog.GiftModel
import com.farshidabz.kindnesswall.data.model.CustomResult
import com.farshidabz.kindnesswall.data.repository.CatalogRepo

class CatalogViewModel(private val catalogRepo: CatalogRepo) : ViewModel() {
    private var lastId = 0L

    val catalogItems: LiveData<CustomResult<List<GiftModel>>> by lazy {
        catalogRepo.getGiftsFirstPage(viewModelScope)
    }

    fun getCatalogItemsFromServer(): LiveData<CustomResult<List<GiftModel>>> {
        lastId = catalogItems.value?.data?.last()?.id ?: 0
        return catalogRepo.getGifts(viewModelScope, lastId)
    }
}