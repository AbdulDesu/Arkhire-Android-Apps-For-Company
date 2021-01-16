package com.sizdev.arkhireforcompany.homepage.item.home

import com.sizdev.arkhireforcompany.homepage.item.home.android.AndroidDeveloperModel
import com.sizdev.arkhireforcompany.homepage.item.home.devops.DevOpsEngineerModel
import com.sizdev.arkhireforcompany.homepage.item.home.fullstackmobile.FullStackMobileModel
import com.sizdev.arkhireforcompany.homepage.item.home.fullstackweb.FullStackWebModel

interface HomeContract {

    interface View {
        fun getCurrentAccount()
        fun setCompanyID(companyID: String)
        fun addAndroidDeveloperTalent(list: List<AndroidDeveloperModel>)
        fun addDevOpsEngineerTalent(list: List<DevOpsEngineerModel>)
        fun addFullStackMobileTalent(list: List<FullStackMobileModel>)
        fun addFullStackWebTalent(list: List<FullStackWebModel>)
        fun setGreeting(name: String)
        fun setError(error: String)
        fun hideProgressBar()
    }

    interface Presenter{
        fun bindToView(view: View)
        fun unbind()
        fun getCurrentAccount(accountID: String)
        fun getAndroidTalent()
        fun getDevOpsTalent()
        fun getFullStackMobileTalent()
        fun getFullStackWebTalent()
    }

}