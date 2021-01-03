package com.sizdev.arkhireforcompany.homepage.item.account

import retrofit2.http.GET
import retrofit2.http.Query

interface AccountApiService {

    @GET("/company/holder")
    suspend fun getAccountDataByNameResponse(@Query("search") accountName : String): AccountResponse
}