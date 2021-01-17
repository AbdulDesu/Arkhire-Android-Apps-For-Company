package com.sizdev.arkhireforcompany.homepage.item.home.detailedtalent

interface TalentProfileContract {

    interface View {
        fun setError(error: String)
        fun setService()
        fun showProgressBar()
        fun getCurrentLoginData()
        fun setTalentData(talentID: String?,
                          accountName: String?,
                          accountEmail: String?,
                          accountPhone: String?,
                          talentTitle: String?,
                          talentTime: String?,
                          talentCity: String?,
                          talentDesc: String?,
                          talentImage: String?,
                          talentGithub: String?,
                          talentCv: String?,
                          talentSkill1: String?,
                          talentSkill2: String?,
                          talentSkill3: String?,
                          talentSkill4: String?,
                          talentSkill5: String?)
        fun hideProgressBar()
        fun sessionExpiredAlert()
        fun setProfileTab()
        fun setDataRefreshManager()
        fun setFloatingButton()
    }

    interface Presenter {
        fun bindToView(view: View)
        fun unbind()
        fun getTalentByID(talentID: String)
    }

}