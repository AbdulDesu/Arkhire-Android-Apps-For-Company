package com.sizdev.arkhireforcompany.homepage.item.home.fullstackmobile

import retrofit2.http.GET

interface FullStackMobileApiService {

    @GET("/talent/filter/title?search=Fullstack Mobile")
    suspend fun getTalentFullStackMobileResponse(): FullStackMobileResponse
}