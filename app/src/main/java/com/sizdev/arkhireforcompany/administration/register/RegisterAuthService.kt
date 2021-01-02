package com.sizdev.arkhireforcompany.administration.register

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RegisterAuthService {
    @FormUrlEncoded
    @POST("/account/register")
    suspend fun registerRequest(@Field("account_name") acName: String,
                                @Field("account_email") acEmail: String,
                                @Field("account_phone") acPhone: String,
                                @Field("password") password: String,
                                @Field("privilege") privilege: Int,
                                @Field("company_name") companyName: String?,
                                @Field("company_position") companyPosition: String?
    ) : RegisterResponse
}