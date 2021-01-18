package com.sizdev.arkhireforcompany.homepage.item.account.profile

interface CompanyProfileContract {

    interface View {
        fun setError(error: String)
        fun setService()
        fun getCurrentLoginData()
        fun setMap()
        fun setCompanyData(companyID: String,
                           accountID: String,
                           accountName: String,
                           companyName: String,
                           companyLocation: String,
                           companyPosition: String,
                           companyLatitude: String,
                           companyLongitude: String,
                           companyType: String,
                           companyDesc: String,
                           companyLinkedin: String,
                           companyInstagram: String,
                           companyFacebook: String,
                           companyImage: String)
        fun setDataRefreshManagement()
        fun showProgressBar()
        fun hideProgressBar()
        fun showSessionExpiredAlert()
    }

    interface Presenter{
        fun bindToView(view: View)
        fun unbind()
        fun getCompanyData(accountID: String)
    }

}