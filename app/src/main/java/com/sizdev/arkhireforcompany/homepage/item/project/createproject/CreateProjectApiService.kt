package com.sizdev.arkhireforcompany.homepage.item.project.createproject

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface CreateProjectApiService {
    @FormUrlEncoded
    @POST("/project")
    suspend fun createProjectRequest(@Field("project_tittle") projectName: String,
                                     @Field("project_duration") projectDuration: String,
                                     @Field("project_desc") projectDesc:String,
                                     @Field("project_sallary") projectSalary:String) : CreateProjectResponse
}