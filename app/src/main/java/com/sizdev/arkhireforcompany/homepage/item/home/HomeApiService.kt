package com.sizdev.arkhireforcompany.homepage.item.home

import retrofit2.http.GET
import retrofit2.http.Path

interface HomeApiService {

    @GET("/account/{accountID}")
    suspend fun getAccountResponse(@Path("accountID") accountID: String): HomeResponse

}