package ir.kindnesswall.data.repositories.auth

import androidx.lifecycle.LiveData
import ir.kindnesswall.domain.entities.LoginResponseModel
import ir.kindnesswall.domain.common.CustomResult
import kotlinx.coroutines.CoroutineScope

interface AuthRemoteDataSource {

  suspend fun registerUser(
    viewModelScope: CoroutineScope,
    phoneNumber: String)
  : LiveData<CustomResult<Any?>>

  suspend fun loginUser(
    viewModelScope: CoroutineScope,
    phoneNumber: String,
    verificationCode: String
  ): LiveData<CustomResult<LoginResponseModel>>
}