package com.sizdev.arkhireforcompany.networking

import com.sizdev.arkhireforcompany.administration.login.LoginResponse
import com.sizdev.arkhireforcompany.administration.register.RegisterResponse
import com.sizdev.arkhireforcompany.homepage.item.account.AccountResponse
import com.sizdev.arkhireforcompany.homepage.item.home.HomeResponse
import com.sizdev.arkhireforcompany.homepage.item.home.alltalent.ShowAllTalentResponse
import com.sizdev.arkhireforcompany.homepage.item.home.android.AndroidDeveloperResponse
import com.sizdev.arkhireforcompany.homepage.item.home.devops.DevOpsEngineerResponse
import com.sizdev.arkhireforcompany.homepage.item.home.fullstackmobile.FullStackMobileResponse
import com.sizdev.arkhireforcompany.homepage.item.home.fullstackweb.FullStackWebResponse
import com.sizdev.arkhireforcompany.homepage.item.project.createproject.CreateProjectResponse
import com.sizdev.arkhireforcompany.homepage.item.project.showproject.ProjectResponse
import retrofit2.http.*

interface ArkhireApiService {

    // Login Service
    @FormUrlEncoded
    @POST("/account/login")
    suspend fun loginRequest(@Field("email") email: String,
                             @Field("password") password: String ) : LoginResponse

    // Register Service
    @FormUrlEncoded
    @POST("/account/register")
    suspend fun registerRequest(@Field("account_name") acName: String,
                                @Field("account_email") acEmail: String,
                                @Field("account_phone") acPhone: String,
                                @Field("password") password: String,
                                @Field("privilege") privilege: Int,
                                @Field("company_name") companyName: String?,
                                @Field("company_position") companyPosition: String?) : RegisterResponse

    // Account Service
    @GET("/company/holder")
    suspend fun getAccountDataByNameResponse(@Query("search") accountName : String): AccountResponse

    // Home Service
    @GET("/account/{accountID}")
    suspend fun getAccountResponse(@Path("accountID") accountID: String): HomeResponse

    // Talent Service
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

    // Project Service
    @GET("/projectresp/newer")
    suspend fun getAllProjectResponse(): ProjectResponse

    @FormUrlEncoded
    @POST("/project")
    suspend fun createProjectRequest(@Field("project_tittle") projectName: String,
                                     @Field("project_duration") projectDuration: String,
                                     @Field("project_desc") projectDesc:String,
                                     @Field("project_sallary") projectSalary:String) : CreateProjectResponse

    @FormUrlEncoded
    @PUT("/project/{projectID}")
    suspend fun updateProjectResponse(@Path("projectID") projectID: String,
                                      @Field ("project_tittle") projectTitle: String,
                                      @Field("project_duration") projectDuration: String,
                                      @Field("project_desc") projectDesc: String,
                                      @Field("project_sallary") projectSalary: String): ProjectResponse

    @DELETE("/project/{projectID}")
    suspend fun deleteProjectResponse(@Path ("projectID") projectID: String): ProjectResponse

}