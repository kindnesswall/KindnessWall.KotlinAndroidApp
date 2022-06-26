package ir.kindnesswall.data.repositories.charity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import ir.kindnesswall.data.api.CharityApi
import ir.kindnesswall.data.db.dao.charity.CharityModel
import ir.kindnesswall.data.repositories.base.BaseDataSource
import ir.kindnesswall.domain.common.CustomResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect

class CharityRemoteDataSourceImpl(
  private val charityApi: CharityApi
) : CharityRemoteDataSource, BaseDataSource() {

  override suspend fun getCharities(viewModelScope: CoroutineScope): LiveData<CustomResult<List<CharityModel>>> =
    liveData<CustomResult<List<CharityModel>>>(
      viewModelScope.coroutineContext,
      timeoutInMs = 0
    ) {
      emit(CustomResult.loading())

      getResultWithExponentialBackoffStrategy { charityApi.getCharities() }
        .collect { result ->
          when (result.status) {
            CustomResult.Status.SUCCESS -> {
              if (result.data == null) {
                emit(CustomResult.error(result.errorMessage))
              } else {
                emitSource(MutableLiveData<List<CharityModel>>().apply {
                  value = result.data
                }.map { CustomResult.success(it) })
              }
            }
            CustomResult.Status.LOADING -> emit(CustomResult.loading())
            else -> emit(CustomResult.error(result.errorMessage))
          }
        }
    }

  override suspend fun getCharity(
    viewModelScope: CoroutineScope,
    charityId: Long
  ): LiveData<CustomResult<CharityModel>> =
    liveData<CustomResult<CharityModel>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
      emit(CustomResult.loading())

      getResultWithExponentialBackoffStrategy { charityApi.getCharity(charityId) }
        .collect { result ->
          when (result.status) {
            CustomResult.Status.SUCCESS -> {
              if (result.data == null) {
                emit(CustomResult.error(result.errorMessage))
              } else {
                emitSource(MutableLiveData<CharityModel>().apply {
                  value = result.data
                }
                  .map { CustomResult.success(it) })
              }
            }
            CustomResult.Status.LOADING -> emit(CustomResult.loading())
            else -> emit(CustomResult.error(result.errorMessage))
          }
        }
    }
}