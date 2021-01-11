package com.sizdev.arkhireforcompany.networking

import com.sizdev.arkhireforcompany.administration.login.LoginResponse
import com.sizdev.arkhireforcompany.administration.register.RegisterResponse
import com.sizdev.arkhireforcompany.homepage.item.account.AccountResponse
import com.sizdev.arkhireforcompany.homepage.item.account.profile.edit.CompanyEditProfileResponse
import com.sizdev.arkhireforcompany.homepage.item.home.HomeResponse
import com.sizdev.arkhireforcompany.homepage.item.explore.ExploreResponse
import com.sizdev.arkhireforcompany.homepage.item.home.android.AndroidDeveloperResponse
import com.sizdev.arkhireforcompany.homepage.item.home.detailedtalent.portfolio.PortfolioResponse
import com.sizdev.arkhireforcompany.homepage.item.home.detailedtalent.workexperience.WorkExperienceResponse
import com.sizdev.arkhireforcompany.homepage.item.home.devops.DevOpsEngineerResponse
import com.sizdev.arkhireforcompany.homepage.item.home.fullstackmobile.FullStackMobileResponse
import com.sizdev.arkhireforcompany.homepage.item.home.fullstackweb.FullStackWebResponse
import com.sizdev.arkhireforcompany.homepage.item.project.createproject.CreateProjectResponse
import com.sizdev.arkhireforcompany.homepage.item.project.showproject.ProjectResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
    @GET("/company/holder/{accountID}")
    suspend fun getAccountDataByNameResponse(@Path("accountID") accountID : String): AccountResponse

    // Profile Service
    @Multipart
    @PUT("company/{companyID}")
    suspend fun updateCompany(@Path("companyID") companyID: String,
                              @Part("company_location") companyLocation: RequestBody,
                              @Part("company_latitude") companyLatitude: RequestBody,
                              @Part("company_longitude") companyLongitude: RequestBody,
                              @Part("company_type") companyType: RequestBody,
                              @Part("company_detail") companyDetail: RequestBody,
                              @Part("company_linkedin") companyLinkedin: RequestBody,
                              @Part("company_instagram") companyInstagram: RequestBody,
                              @Part("company_facebook") companyFacebook: RequestBody,
                              @Part companyImage: MultipartBody.Part) : CompanyEditProfileResponse

    // Home Service
    @GET("/account/{accountID}")
    suspend fun getAccountResponse(@Path("accountID") accountID: String): HomeResponse

    // Talent Service
    @GET("/talent/filter/name")
    suspend fun getAllTalentResponse(): ExploreResponse

    @GET("/talent/filter/name")
    suspend fun filterTalentByName(@Query("search") name: String): ExploreResponse

    @GET("/talent/filter/location")
    suspend fun filterTalentByLocation(@Query("search") location: String): ExploreResponse

    @GET("/talent/filter/time")
    suspend fun filterTalentByTimeWork(@Query("search") time: String): ExploreResponse

    @GET("/talent/filter/title?search=Android Developer")
    suspend fun getTalentAndroidResponse(): AndroidDeveloperResponse

    @GET("/talent/filter/title?search=DevOps Engineer")
    suspend fun getTalentDevOpsResponse(): DevOpsEngineerResponse

    @GET("/talent/filter/title?search=Fullstack Mobile")
    suspend fun getTalentFullStackMobileResponse(): FullStackMobileResponse

    @GET("/talent/filter/title?search=Fullstack Web")
    suspend fun getTalentFullStackWebResponse(): FullStackWebResponse


    // Project Service
    @GET("projectresp/owner/{accountID}")
    suspend fun getAllProjectResponse(@Path("accountID") accountID: String): ProjectResponse

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

    // Portfolio Service
    @GET("portfolio/owner/{accountID}")
    suspend fun getPortfolio(@Path("accountID") accountID: String): PortfolioResponse

    // Work Experience Service
    @GET("talentexperience/owner/{accountID}")
    suspend fun getWorkExperiences(@Path("accountID") accountID: String): WorkExperienceResponse

}