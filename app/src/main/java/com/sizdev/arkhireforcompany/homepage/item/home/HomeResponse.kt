package com.sizdev.arkhireforcompany.homepage.item.home

import com.google.gson.annotations.SerializedName

data class HomeResponse (val success: String, val message: String, val data: CompanyOwner) {
    data class CompanyOwner(@SerializedName("account_name") val accountName: String)
}
