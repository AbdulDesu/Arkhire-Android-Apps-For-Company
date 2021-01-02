package com.sizdev.arkhireforcompany.administration.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(val success: Boolean, val message: String, val data: Data? ) {
    data class Data(
        @SerializedName("accountID") val userId: String?,
        @SerializedName("account_name") val userName: String?,
        @SerializedName("account_email") val userEmail: String?,
        val privilege: String?,
        val token: String?
    )
}