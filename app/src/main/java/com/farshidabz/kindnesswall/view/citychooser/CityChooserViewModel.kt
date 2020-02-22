package com.farshidabz.kindnesswall.view.citychooser

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farshidabz.kindnesswall.data.local.dao.province.ProvinceModel
import com.farshidabz.kindnesswall.data.model.CityModel
import com.farshidabz.kindnesswall.data.model.CustomResult
import com.farshidabz.kindnesswall.data.model.RegionModel
import com.farshidabz.kindnesswall.data.repository.GeneralRepo
import com.farshidabz.kindnesswall.utils.OnItemClickListener

class CityChooserViewModel(private val generalRepo: GeneralRepo) : ViewModel() {
    var chosenCity: CityModel? = null
    var chosenRegions: RegionModel? = null

    lateinit var chosenProvince: ProvinceModel

    public lateinit var onClickCallback: OnItemClickListener

    val provinceList: LiveData<CustomResult<List<ProvinceModel>>> by lazy {
        generalRepo.getProvinces(viewModelScope)
    }

    fun getCities(): LiveData<CustomResult<List<CityModel>>> {
        return generalRepo.getCities(viewModelScope, chosenProvince.id)
    }

    fun getRegions(): LiveData<CustomResult<List<RegionModel>>> {
        return generalRepo.getRegions(viewModelScope, chosenCity?.id ?: 0)
    }
}