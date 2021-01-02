package com.sizdev.arkhireforcompany.administration.register

import com.google.gson.annotations.SerializedName

data class RegisterResponse(val success: Boolean, val message: String, val data: Data? ) {
    data class Data(
        @SerializedName("account_name") val acName: String?,
        @SerializedName("account_email") val acEmail: String?,
        @SerializedName("account_phone") val acPhone: String?,
        val password: String?,
        val privilege: Int?,
        @SerializedName("company_name") val companyName: String?,
        @SerializedName("company_position") val companyPosition: String?
    )
}
