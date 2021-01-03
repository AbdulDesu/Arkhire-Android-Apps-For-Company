package com.sizdev.arkhireforcompany.homepage.item.account

import com.google.gson.annotations.SerializedName

data class AccountResponse(val success: String, val message: String, val data: List<AccountData>) {

    data class AccountData(
            val companyID: String?,
            val accountID: String?,
            @SerializedName("account_name") val accountName: String?,
            @SerializedName("company_name") val companyName: String?,
            @SerializedName("company_position") val companyPosition: String,
            @SerializedName("company_image") val companyImage: String
    )
}
