package ir.kindnesswall.data.remote.network

import ir.kindnesswall.data.model.LoginResponseModel
import ir.kindnesswall.data.model.requestsmodel.LoginUserRequestBodyBaseModel
import ir.kindnesswall.data.model.requestsmodel.RegisterBaseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by Farshid Abazari since 25/10/19
 *
 * Usage: a interface to define end points
 *
 * How to call: just add in appInjector and repositoryImpl that you wanna use
 *
 */
interface AuthApi {
    @POST("register")
    suspend fun registerUser(@Body registerModel: RegisterBaseModel): Response<Any>

    @POST("login")
    suspend fun loginUser(@Body loginModel: LoginUserRequestBodyBaseModel): Response<LoginResponseModel>
}