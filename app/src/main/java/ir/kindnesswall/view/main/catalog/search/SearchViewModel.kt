package ir.kindnesswall.view.main.catalog.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.kindnesswall.data.local.AppPref
import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.model.FilterModel
import ir.kindnesswall.data.model.requestsmodel.GetGiftsRequestBaseBody
import ir.kindnesswall.data.repository.GiftRepo

class SearchViewModel(private val giftRepo: GiftRepo) : ViewModel() {
    var searchWorld: String? = null

    var lastId = Long.MAX_VALUE

    var getGiftsRequestBody = GetGiftsRequestBaseBody()
    var filterModel: FilterModel? = FilterModel()

    fun onSearchTextChanged(text: CharSequence) {
        searchWorld = if (text.isEmpty()) {
            null
        } else {
            text.toString()
        }

        getGiftsRequestBody.searchWord = searchWorld
    }

    val searchItems = MutableLiveData<ArrayList<GiftModel>>()

    private fun setFilterModel() {
        filterModel?.let {
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
        setFilterModel()

        lastId = if (searchItems.value != null) {
            if (searchItems.value!!.isNotEmpty()) {
                searchItems.value!!.last().id
            } else {
                Long.MAX_VALUE
            }
        } else {
            Long.MAX_VALUE
        }

        return giftRepo.searchForGifts(viewModelScope, lastId, getGiftsRequestBody)
    }

    fun getPrvSearchItems(): ArrayList<String>? {
        return AppPref.getRecentSearch()
    }
}