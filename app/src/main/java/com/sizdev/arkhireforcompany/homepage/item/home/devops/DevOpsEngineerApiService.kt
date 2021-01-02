package com.sizdev.arkhireforcompany.homepage.item.home.devops

import retrofit2.http.GET

interface DevOpsEngineerApiService {

    @GET("/talent/filter/title?search=DevOps Engineer")
    suspend fun getTalentDevOpsResponse(): DevOpsEngineerResponse
}