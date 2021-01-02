package com.sizdev.arkhireforcompany.homepage.item.home.android

import retrofit2.http.GET

interface AndroidDeveloperApiService {

    @GET("/talent/filter/title?search=Android Developer")
    suspend fun getTalentAndroidResponse(): AndroidDeveloperResponse
}