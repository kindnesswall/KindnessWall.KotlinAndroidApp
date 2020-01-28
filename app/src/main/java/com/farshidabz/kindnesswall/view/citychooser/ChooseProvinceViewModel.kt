package com.farshidabz.kindnesswall.view.citychooser

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farshidabz.kindnesswall.data.local.dao.province.ProvinceModel
import com.farshidabz.kindnesswall.data.model.CustomResult
import com.farshidabz.kindnesswall.data.repository.GeneralRepo

class ChooseProvinceViewModel(private val generalRepo: GeneralRepo) : ViewModel() {
    val catalogItems: LiveData<CustomResult<List<ProvinceModel>>> by lazy {
        generalRepo.getProvinces(viewModelScope)
    }
}