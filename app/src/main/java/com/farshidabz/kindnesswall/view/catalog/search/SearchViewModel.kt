package com.farshidabz.kindnesswall.view.catalog.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farshidabz.kindnesswall.data.local.dao.catalog.GiftModel
import com.farshidabz.kindnesswall.data.model.CustomResult
import com.farshidabz.kindnesswall.data.repository.CatalogRepo

class SearchViewModel(private val catalogRepo: CatalogRepo) : ViewModel() {
    fun searchForItem(): LiveData<CustomResult<List<GiftModel>>> = catalogRepo.getGiftsFirstPage(viewModelScope)
}