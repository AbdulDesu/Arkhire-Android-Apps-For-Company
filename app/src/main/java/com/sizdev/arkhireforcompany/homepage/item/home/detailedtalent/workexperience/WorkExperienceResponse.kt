package com.sizdev.arkhireforcompany.homepage.item.home.detailedtalent.workexperience

import com.google.gson.annotations.SerializedName

data class WorkExperienceResponse(val success: Boolean, val message: String, val data: List<Experience>) {

    data class Experience(@SerializedName("experienceID") val experienceID: String,
                          @SerializedName("experience_owner") val experienceOwner: String,
                          @SerializedName("experience_title") val experienceTitle: String,
                          @SerializedName("experience_source") val experienceSource: String,
                          @SerializedName("experience_start") val experienceStart: String,
                          @SerializedName("experience_end") val experienceEnd: String,
                          @SerializedName("experience_desc") val experienceDesc: String)
}