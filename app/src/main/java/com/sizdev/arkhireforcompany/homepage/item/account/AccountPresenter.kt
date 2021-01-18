package com.sizdev.arkhireforcompany.homepage.item.account

import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class AccountPresenter (private val coroutineScope: CoroutineScope,
                        private val service: ArkhireApiService?) : AccountContract.Presenter {

    private var view: AccountContract.View? = null

    override fun bindToView(view: AccountContract.View) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun getAccountData(accountID: String) {
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
                    view?.hideProgressBar()
                    view?.setAccountData(
                            result.data[0].companyID,
                            result.data[0].accountID,
                            result.data[0].accountName,
                            result.data[0].companyName,
                            result.data[0].companyType.toString(),
                            result.data[0].companyPosition.toString(),
                            result.data[0].companyImage.toString(),
                            result.data[0].companyLatitude.toString(),
                            result.data[0].companyLongitude.toString())
                }

                else {
                    view?.hideProgressBar()
                    view?.setError(result.message)
                }
            }
        }
    }


}