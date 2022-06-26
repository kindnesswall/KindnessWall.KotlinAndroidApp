//package ir.kindnesswall.data.api
//
//import android.content.Context
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.liveData
//import androidx.lifecycle.map
//import ir.kindnesswall.data.repositories.base.BaseDataSource
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.flow.collect
//
//class AuthRepo(context: Context, private var authApi: AuthApi) : BaseDataSource(context) {
//
//  fun registerUser(
//    viewModelScope: CoroutineScope,
//    phoneNumber: String
//  ): LiveData<CustomResult<Any?>> =
//    liveData<CustomResult<Any?>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
//      emit(CustomResult.loading())
//
//      getNullableResultWithExponentialBackoffStrategy {
//        authApi.registerUser(RegisterBaseModel(phoneNumber))
//      }.collect { result: CustomResult<Any?> ->
//        when (result.status) {
//          CustomResult.Status.SUCCESS -> {
//            emit(CustomResult.success(result.data))
//          }
//          CustomResult.Status.ERROR -> {
//            emit(CustomResult.error<Any>(result.errorMessage))
//          }
//          CustomResult.Status.LOADING -> emit(CustomResult.loading<Any>())
//        }
//      }
//    }
//
//  fun loginUser(viewModelScope: CoroutineScope, phoneNumber: String, verificationCode: String):
//    LiveData<CustomResult<LoginResponseModel>> =
//    liveData<CustomResult<LoginResponseModel>>(
//      viewModelScope.coroutineContext,
//      timeoutInMs = 0
//    ) {
//      emit(CustomResult.loading())
//
//      getResultWithExponentialBackoffStrategy {
//        authApi.loginUser(LoginUserRequestBodyBaseModel(phoneNumber, verificationCode))
//      }.collect { result ->
//        when (result.status) {
//          CustomResult.Status.SUCCESS -> {
//            emitSource(MutableLiveData<LoginResponseModel>().apply {
//              value = result.data
//            }.map { CustomResult.success(it) })
//          }
//          CustomResult.Status.ERROR -> {
//            emit(CustomResult.error(result.errorMessage))
//          }
//          CustomResult.Status.LOADING -> emit(CustomResult.loading())
//        }
//      }
//    }
//}