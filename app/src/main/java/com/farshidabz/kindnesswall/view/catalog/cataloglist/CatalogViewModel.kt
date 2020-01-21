package com.farshidabz.kindnesswall.view.catalog.cataloglist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farshidabz.kindnesswall.data.model.CustomResult
import com.farshidabz.kindnesswall.data.model.gift.GiftResponseModel
import com.farshidabz.kindnesswall.data.repository.CatalogRepo

class CatalogViewModel(private val catalogRepo: CatalogRepo) : ViewModel() {
    val catalogItems: LiveData<CustomResult<GiftResponseModel>> by lazy {
        catalogRepo.getGifts(viewModelScope)
    }

    fun getCatalogItemsFromServer(): LiveData<CustomResult<GiftResponseModel>> {
        return catalogRepo.getGifts(viewModelScope)
    }
}