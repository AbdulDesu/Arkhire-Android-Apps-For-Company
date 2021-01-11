package com.sizdev.arkhireforcompany.homepage.item.account.profile.edit

import com.google.gson.annotations.SerializedName

data class CompanyEditProfileResponse (val success: Boolean, val message: String, val data: CompanyData?){
    data class CompanyData(
        @SerializedName("companyID") val companyID: String?,
        @SerializedName("company_location") val companyLocation: String?,
        @SerializedName("company_latitude") val companyLatitude: String?,
        @SerializedName("company_longitude") val companyLongitude: String?,
        @SerializedName("company_type") val companyType: String?,
        @SerializedName("company_detail") val companyDesc: String?,
        @SerializedName("company_linkedin") val companyLinkedin: String?,
        @SerializedName("company_instagram") val companyInstagram: String?,
        @SerializedName("company_facebook") val companyFacebook: String?,
        @SerializedName("company_image") val companyImage: String?,
    )
}