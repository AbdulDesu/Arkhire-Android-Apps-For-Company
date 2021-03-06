package com.sizdev.arkhireforcompany.homepage.item.account

import com.google.gson.annotations.SerializedName

data class AccountResponse(val success: Boolean, val message: String, val data: List<AccountData>) {
    data class AccountData(
            val companyID: String,
            val accountID: String,
            @SerializedName("account_name") val accountName: String,
            @SerializedName("company_name") val companyName: String,
            @SerializedName("company_position") val companyPosition: String?,
            @SerializedName("company_location") val companyLocation: String?,
            @SerializedName("company_latitude") val companyLatitude: String?,
            @SerializedName("company_longitude") val companyLongitude: String?,
            @SerializedName("company_type") val companyType: String?,
            @SerializedName("company_detail") val companyDesc: String?,
            @SerializedName("company_linkedin") val companyLinkedin: String?,
            @SerializedName("company_instagram") val companyInstagram: String?,
            @SerializedName("company_facebook") val companyFacebook: String?,
            @SerializedName("company_image") val companyImage: String?
    )
}
