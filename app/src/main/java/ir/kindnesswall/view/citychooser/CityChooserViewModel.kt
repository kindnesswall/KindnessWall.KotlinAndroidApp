package ir.kindnesswall.view.citychooser

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.kindnesswall.data.local.dao.province.ProvinceModel
import ir.kindnesswall.data.model.CityModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.model.RegionModel
import ir.kindnesswall.data.repository.GeneralRepo
import ir.kindnesswall.utils.OnItemClickListener

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