package ir.kindnesswall.data.api

import ir.kindnesswall.domain.entities.LoginResponseModel
import ir.kindnesswall.domain.entities.LoginUserRequestBodyBaseModel
import ir.kindnesswall.domain.entities.RegisterBaseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
  @POST("register")
  suspend fun registerUser(@Body registerModel: RegisterBaseModel): Response<Any>

  @POST("login")
  suspend fun loginUser(@Body loginModel: LoginUserRequestBodyBaseModel): Response<LoginResponseModel>
}