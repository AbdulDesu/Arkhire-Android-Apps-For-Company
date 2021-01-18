package com.sizdev.arkhireforcompany.homepage.item.account.profile

import com.sizdev.arkhireforcompany.homepage.item.account.AccountContract
import com.sizdev.arkhireforcompany.homepage.item.account.AccountResponse
import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class CompanyProfilePresenter(private val coroutineScope: CoroutineScope,
                              private val service: ArkhireApiService?) : CompanyProfileContract.Presenter {

    private var view: CompanyProfileContract.View? = null

    override fun bindToView(view: CompanyProfileContract.View) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun getCompanyData(accountID: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getAccountDataByHolderResponse(accountID)
                } catch (e: HttpException) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        when {
                            e.code() == 403 -> {
                                view?.setError("Session Expired !")
                            }

                            else -> {
                                view?.setError("Unknown Error, Please Try Again Later !")
                            }
                        }
                    }
                }
            }

            if (result is AccountResponse) {
                if (result.success) {
                    view?.setCompanyData(
                            result.data[0].companyID,
                            result.data[0].accountID,
                            result.data[0].accountName,
                            result.data[0].companyName,
                            result.data[0].companyLocation.toString(),
                            result.data[0].companyPosition.toString(),
                            result.data[0].companyLatitude.toString(),
                            result.data[0].companyLongitude.toString(),
                            result.data[0].companyType.toString(),
                            result.data[0].companyDesc.toString(),
                            result.data[0].companyLinkedin.toString(),
                            result.data[0].companyInstagram.toString(),
                            result.data[0].companyFacebook.toString(),
                            result.data[0].companyImage.toString())

                    view?.hideProgressBar()
                }

                else {
                    view?.hideProgressBar()
                    view?.setError(result.message)
                }
            }
        }
    }
}