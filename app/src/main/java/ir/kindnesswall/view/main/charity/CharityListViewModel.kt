package ir.kindnesswall.view.main.charity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.kindnesswall.data.local.dao.charity.CharityModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.repository.CharityRepo

class CharityListViewModel(private val charityRepo: CharityRepo) : ViewModel() {
    val charityItems = ArrayList<CharityModel>()

    fun getCharityItemsFromServer(): LiveData<CustomResult<List<CharityModel>>> {
        return charityRepo.getCharities(viewModelScope)
    }
}