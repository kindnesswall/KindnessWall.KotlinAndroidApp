package ir.kindnesswall.data.repositories.charity

import androidx.lifecycle.LiveData
import ir.kindnesswall.data.db.dao.charity.CharityModel
import ir.kindnesswall.domain.common.CustomResult
import kotlinx.coroutines.CoroutineScope

interface CharityRemoteDataSource {

  suspend fun getCharities(viewModelScope: CoroutineScope):
    LiveData<CustomResult<List<CharityModel>>>

  suspend fun getCharity(viewModelScope: CoroutineScope, charityId: Long):
    LiveData<CustomResult<CharityModel>>
}