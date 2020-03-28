package com.farshidabz.kindnesswall.view.catalog.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farshidabz.kindnesswall.data.local.AppPref
import com.farshidabz.kindnesswall.data.local.dao.catalog.GiftModel
import com.farshidabz.kindnesswall.data.model.CustomResult
import com.farshidabz.kindnesswall.data.model.FilterModel
import com.farshidabz.kindnesswall.data.model.requestsmodel.GetGiftsRequestBaseBody
import com.farshidabz.kindnesswall.data.repository.CatalogRepo

class SearchViewModel(private val catalogRepo: CatalogRepo) : ViewModel() {
    var searchWorld: String? = null

    var getGiftsRequestBody = GetGiftsRequestBaseBody()
    var filterModel = FilterModel()

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
        setFilterModel()
        return catalogRepo.searchForGiftFirstPage(viewModelScope, getGiftsRequestBody)
    }

    private fun setFilterModel() {
        filterModel.let {
            with(getGiftsRequestBody) {
                getGiftsRequestBody.clear()

                searchWord = searchWorld
                beforeId = Long.MAX_VALUE
                count = 50

                it.categoryModel?.let { cats ->
                    if (categoryIds == null) {
                        categoryIds = arrayListOf()
                    }

                    for (cat in cats) {
                        categoryIds!!.add(cat.id)
                    }
                }

                if (it.city != null) {
                    provinceId = it.city?.provinceId
                    cityId = it.city?.id

                    if (provinceId != null) {
                        if (provinceId!! <= 0) provinceId = null
                    }
                    if (cityId != null) {
                        if (cityId!! <= 0) cityId = null
                    }

                } else if (it.regionModel != null) {
                    provinceId = it.regionModel?.province_id
                    regionIds = it.regionModel?.id
                    cityId = null

                    if (provinceId != null) {
                        if (provinceId!! <= 0) provinceId = null
                    }
                    if (regionIds != null) {
                        if (regionIds!! <= 0) regionIds = null
                    }
                }

                if (categoryIds.isNullOrEmpty()) {
                    categoryIds = null
                }

                if (regionIds == 0) {
                    regionIds = null
                }

                if (cityId == 0) {
                    cityId = null
                }
            }
        }
    }

    fun searchForItemFromServer(): LiveData<CustomResult<List<GiftModel>>> {
        val lastId = searchItems.value?.last()?.id ?: 0
        return catalogRepo.searchForGifts(viewModelScope, lastId, getGiftsRequestBody)
    }

    fun getPrvSearchItems(): ArrayList<String>? {
        return AppPref.getRecentSearch()
    }
}