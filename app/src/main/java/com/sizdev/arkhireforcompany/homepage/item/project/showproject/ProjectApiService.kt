package com.sizdev.arkhireforcompany.homepage.item.project.showproject

import retrofit2.http.*

interface ProjectApiService {
    @GET("/projectresp/newer")
    suspend fun getAllProjectResponse(): ProjectResponse

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