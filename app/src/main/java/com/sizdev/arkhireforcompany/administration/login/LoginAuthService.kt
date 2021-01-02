package com.sizdev.arkhireforcompany.administration.login

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginAuthService {
    @FormUrlEncoded
    @POST("/account/login")
    suspend fun loginRequest(@Field("email") email: String,
                             @Field("password") password: String ) : LoginResponse
}