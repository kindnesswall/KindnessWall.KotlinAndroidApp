package com.farshidabz.kindnesswall.view.catalog.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farshidabz.kindnesswall.data.local.AppPref
import com.farshidabz.kindnesswall.data.local.dao.catalog.GiftModel
import com.farshidabz.kindnesswall.data.model.CustomResult
import com.farshidabz.kindnesswall.data.model.requestsmodel.GetGiftsRequestBody
import com.farshidabz.kindnesswall.data.repository.CatalogRepo

class SearchViewModel(private val catalogRepo: CatalogRepo) : ViewModel() {
    var searchWorld: String? = null

    var getGiftsRequestBody = GetGiftsRequestBody()

    fun onSearchTextChanged(text: CharSequence) {
        searchWorld = if (text.isEmpty()) {
            null
        } else {
            text.toString()
        }

        getGiftsRequestBody.searchWord = searchWorld
    }

    val searchItems = MutableLiveData<ArrayList<GiftModel>>()

    fun searchFirstPage(): LiveData<CustomResult<List<GiftModel>>> {
        return catalogRepo.searchForGiftFirstPage(viewModelScope, getGiftsRequestBody)
    }

    fun searchForItemFromServer(): LiveData<CustomResult<List<GiftModel>>> {
        val lastId = searchItems.value?.last()?.id ?: 0
        return catalogRepo.searchForGifts(viewModelScope, lastId, getGiftsRequestBody)
    }

    fun getPrvSearchItems(): ArrayList<String>? {
        return AppPref.getRecentSearch()
    }
}