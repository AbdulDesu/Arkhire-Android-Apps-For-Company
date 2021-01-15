package com.sizdev.arkhireforcompany.homepage.item.project.showproject.detailproject.contributor

import com.google.gson.annotations.SerializedName

data class ProjectContributorResponse(val success: Boolean, val message: String, val data: List<Contributor>){
    data class Contributor(@SerializedName("account_name") val accountName: String,
                           @SerializedName("talent_tittle") val accountTitle: String,
                           @SerializedName("talent_image") val talentImage: String)
}
