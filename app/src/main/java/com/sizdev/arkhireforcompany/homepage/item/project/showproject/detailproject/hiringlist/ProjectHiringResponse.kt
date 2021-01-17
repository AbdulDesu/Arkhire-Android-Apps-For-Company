package com.sizdev.arkhireforcompany.homepage.item.project.showproject.detailproject.hiringlist

import com.google.gson.annotations.SerializedName


data class ProjectHiringResponse (val success: Boolean, val message: String, val data: List<ProjectHiringResponse.HiringList>) {
    data class HiringList ( @SerializedName("offeringID") val offeringID : String,
                            @SerializedName("hiring_status") val hiringStatus: String,
                            @SerializedName("offered_salary") val offeredSalary: String,
                            @SerializedName("reply_message") val replyMsg: String,
                            @SerializedName("offering_owner") val talentID: String,
                            @SerializedName("account_name") val talentName: String,
                            @SerializedName("talent_tittle") val talentTitle: String,
                            @SerializedName("talent_image") val talentImage: String,
                            @SerializedName("project_sallary") val projectSalary: String,
                            @SerializedName("project_image") val projectImage: String)
}