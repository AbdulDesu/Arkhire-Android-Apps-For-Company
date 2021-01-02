package com.sizdev.arkhireforcompany.homepage.item.home.fullstackweb

import retrofit2.http.GET

interface FullStackWebApiService {

    @GET("/talent/filter/title?search=Fullstack Web")
    suspend fun getTalentFullStackWebResponse(): FullStackWebResponse
}