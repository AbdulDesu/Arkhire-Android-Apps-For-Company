package com.sizdev.arkhireforcompany.homepage.item.project.createproject

import com.google.gson.annotations.SerializedName

data class CreateProjectResponse(val success: Boolean, val message: String, val data: ProjectData?) {
    data class ProjectData(
            @SerializedName("project_title") val projectName:String?,
            @SerializedName("project_duration") val projectDuration:String?,
            @SerializedName("project_desc") val projectDesc:String?,
            @SerializedName("project_sallary") val projectSalary:String?
    )
}
