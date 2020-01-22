package com.farshidabz.kindnesswall.view.catalog.cataloglist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farshidabz.kindnesswall.data.model.CustomResult
import com.farshidabz.kindnesswall.data.model.gift.GiftModel
import com.farshidabz.kindnesswall.data.model.gift.GiftResponseModel
import com.farshidabz.kindnesswall.data.repository.CatalogRepo

class CatalogViewModel(private val catalogRepo: CatalogRepo) : ViewModel() {
    private var lastId = 50

    val catalogItems: LiveData<CustomResult<ArrayList<GiftModel>>> by lazy {
        catalogRepo.getGifts(viewModelScope)
    }

    fun getCatalogItemsFromServer(): LiveData<CustomResult<ArrayList<GiftModel>>> {
        lastId += 50
        return catalogRepo.getGifts(viewModelScope)
    }
}