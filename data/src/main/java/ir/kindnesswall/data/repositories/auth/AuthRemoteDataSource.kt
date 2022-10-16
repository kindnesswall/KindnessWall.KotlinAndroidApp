package ir.kindnesswall.data.repositories.auth

import androidx.lifecycle.LiveData
import ir.kindnesswall.domain.common.CustomResult
import ir.kindnesswall.domain.entities.LoginResponseModel
import kotlinx.coroutines.CoroutineScope

interface AuthRemoteDataSource {

   fun registerUser(
    viewModelScope: CoroutineScope,
    phoneNumber: String)
  : LiveData<CustomResult<Any?>>

   fun loginUser(
    viewModelScope: CoroutineScope,
    phoneNumber: String,
    verificationCode: String
  ): LiveData<CustomResult<LoginResponseModel>>
}