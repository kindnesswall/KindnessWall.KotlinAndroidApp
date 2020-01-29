package com.farshidabz.kindnesswall.view.citychooser

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farshidabz.kindnesswall.data.local.dao.province.ProvinceModel
import com.farshidabz.kindnesswall.data.model.CityModel
import com.farshidabz.kindnesswall.data.model.CustomResult
import com.farshidabz.kindnesswall.data.repository.GeneralRepo

class CityChooserViewModel(private val generalRepo: GeneralRepo) : ViewModel() {
    var chosenCity: CityModel? = null

    lateinit var chosenProvince: ProvinceModel

    val provinceList: LiveData<CustomResult<List<ProvinceModel>>> by lazy {
        generalRepo.getProvinces(viewModelScope)
    }

    val cityList: LiveData<CustomResult<List<CityModel>>> by lazy {
        generalRepo.getCities(viewModelScope, chosenProvince.id)
    }
}