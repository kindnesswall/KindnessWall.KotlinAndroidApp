package ir.kindnesswall.data.repositories.general

import androidx.lifecycle.LiveData
import ir.kindnesswall.data.db.dao.province.ProvinceModel
import ir.kindnesswall.domain.common.CustomResult
import ir.kindnesswall.domain.entities.*
import kotlinx.coroutines.CoroutineScope

interface GeneralDataSource {

  suspend fun getProvinces(viewModelScope: CoroutineScope): LiveData<CustomResult<List<ProvinceModel>>>

  suspend fun getCities(
    viewModelScope: CoroutineScope,
    provinceId: Int
  ): LiveData<CustomResult<List<CityModel>>>

  suspend fun getRegions(
    viewModelScope: CoroutineScope,
    cityId: Int
  ): LiveData<CustomResult<List<RegionModel>>>

  suspend fun getAllCatgories(viewModelScope: CoroutineScope): LiveData<CustomResult<List<CategoryModel>>>

  suspend fun getVersion(viewModelScope: CoroutineScope): LiveData<CustomResult<UpdateModel>>

  suspend fun getSetting(viewModelScope: CoroutineScope): LiveData<CustomResult<SettingModel>>
}