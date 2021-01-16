package com.sizdev.arkhireforcompany.homepage.item.home.fullstackmobile

import com.google.gson.annotations.SerializedName

data class FullStackMobileResponse(val success: Boolean, val message: String, val data: List<TalentFullStackMobile>) {
    data class TalentFullStackMobile(val talentID : String?,
                                     val accountID: String?,
                                     @SerializedName("account_name")  val accountName: String?,
                                     @SerializedName("account_email") val accountEmail: String?,
                                     @SerializedName("account_phone") val accountPhone: String?,
                                     @SerializedName("talent_tittle") val talentTitle: String?,
                                     @SerializedName("talent_time") val talentTime: String?,
                                     @SerializedName("talent_city")val talentCity: String?,
                                     @SerializedName("talent_profile")val talentDesc: String?,
                                     @SerializedName("talent_image")val talentImage: String?,
                                     @SerializedName("talent_github")val talentGithub: String?,
                                     @SerializedName("talent_cv")val talentCv: String?,
                                     @SerializedName("skill_1")val talentSkill1: String?,
                                     @SerializedName("skill_2")val talentSkill2: String?,
                                     @SerializedName("skill_3")val talentSkill3: String?,
                                     @SerializedName("skill_4")val talentSkill4: String?,
                                     @SerializedName("skill_5")val talentSkill5: String?)
}
