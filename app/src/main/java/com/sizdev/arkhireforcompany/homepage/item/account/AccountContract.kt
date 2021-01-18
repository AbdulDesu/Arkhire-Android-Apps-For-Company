package com.sizdev.arkhireforcompany.homepage.item.account

interface AccountContract {

    interface View {
        fun setError(error: String)
        fun setService()
        fun getCurrentLoginData()
        fun setAccountData(companyID: String,
                           accountID: String,
                           accountName: String,
                           companyName: String,
                           companyType: String,
                           companyPosition: String,
                           companyImage: String,
                           companyLatitude: String,
                           companyLongitude: String)
        fun setDataRefreshManagement()
        fun showProgressBar()
        fun hideProgressBar()
        fun showSessionExpiredAlert()
    }

    interface Presenter{
        fun bindToView(view: View)
        fun unbind()
        fun getAccountData(accountID: String)
    }

}