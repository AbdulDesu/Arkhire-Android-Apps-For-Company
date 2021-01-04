package com.sizdev.arkhireforcompany.homepage.item.home

import com.sizdev.arkhireforcompany.homepage.item.home.alltalent.ShowAllTalentResponse
import com.sizdev.arkhireforcompany.homepage.item.home.android.AndroidDeveloperResponse
import com.sizdev.arkhireforcompany.homepage.item.home.devops.DevOpsEngineerResponse
import com.sizdev.arkhireforcompany.homepage.item.home.fullstackmobile.FullStackMobileResponse
import com.sizdev.arkhireforcompany.homepage.item.home.fullstackweb.FullStackWebResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface HomeApiService {

    @GET("/account/{accountID}")
    suspend fun getAccountResponse(@Path("accountID") accountID: String): HomeResponse

    @GET("/talent/filter/name")
    suspend fun getAllTalentResponse(): ShowAllTalentResponse

    @GET("/talent/filter/title?search=Android Developer")
    suspend fun getTalentAndroidResponse(): AndroidDeveloperResponse

    @GET("/talent/filter/title?search=DevOps Engineer")
    suspend fun getTalentDevOpsResponse(): DevOpsEngineerResponse

    @GET("/talent/filter/title?search=Fullstack Mobile")
    suspend fun getTalentFullStackMobileResponse(): FullStackMobileResponse

    @GET("/talent/filter/title?search=Fullstack Web")
    suspend fun getTalentFullStackWebResponse(): FullStackWebResponse

}