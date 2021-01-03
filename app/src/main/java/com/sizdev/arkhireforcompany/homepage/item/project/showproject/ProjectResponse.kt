package com.sizdev.arkhireforcompany.homepage.item.project.showproject

import com.google.gson.annotations.SerializedName

data class ProjectResponse(val success: String, val message: String, val data: List<Project>) {
    data class Project(@SerializedName("offeringID") val offeringID: String,
                       @SerializedName("projectID") val projectID: String,
                       @SerializedName("project_tittle") val projectTitle: String,
                       @SerializedName("project_duration") val projectDuration: String,
                       @SerializedName("project_desc") val projectDesc: String,
                       @SerializedName("project_sallary") val projectSalary: String,
                       @SerializedName("hiring_status") val hiringStatus: String,
                       @SerializedName("reply_message") val replyMsg: String,
                       @SerializedName("repliedAt") val repliedAt: String)
}
