package com.sizdev.arkhireforcompany.homepage.item.home

import com.sizdev.arkhireforcompany.homepage.item.account.AccountResponse
import com.sizdev.arkhireforcompany.homepage.item.account.profile.edit.CompanyEditProfileResponse
import com.sizdev.arkhireforcompany.homepage.item.home.android.AndroidDeveloperModel
import com.sizdev.arkhireforcompany.homepage.item.home.android.AndroidDeveloperResponse
import com.sizdev.arkhireforcompany.homepage.item.home.devops.DevOpsEngineerModel
import com.sizdev.arkhireforcompany.homepage.item.home.devops.DevOpsEngineerResponse
import com.sizdev.arkhireforcompany.homepage.item.home.fullstackmobile.FullStackMobileModel
import com.sizdev.arkhireforcompany.homepage.item.home.fullstackmobile.FullStackMobileResponse
import com.sizdev.arkhireforcompany.homepage.item.home.fullstackweb.FullStackWebModel
import com.sizdev.arkhireforcompany.homepage.item.home.fullstackweb.FullStackWebResponse
import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class HomePresenter(private val coroutineScope: CoroutineScope,
                     private val service: ArkhireApiService?) : HomeContract.Presenter {

    private var view: HomeContract.View? = null

    override fun bindToView(view: HomeContract.View) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun getCurrentAccount(accountID: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getAccountDataByHolderResponse(accountID)
                } catch (e: HttpException) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main){
                        when{
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
                if (result.success){
                    view?.setGreeting(result.data[0].accountName)
                    view?.setCompanyID(result.data[0].companyID)
                }
                else {
                    view?.hideProgressBar()
                    view?.setError(result.message)
                }
            }
        }
    }

    override fun getAndroidTalent() {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getTalentAndroidResponse()
                } catch (e: HttpException) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main){
                        when{
                            e.code() == 403 -> {
                                view?.setError("Session Expired !")
                            }

                            e.code()  == 404 -> {
                                view?.setError("No Talent Found !")
                            }

                            else -> {
                                view?.setError("Unknown Error, Please Try Again Later !")
                            }
                        }
                    }
                }
            }

            if (result is AndroidDeveloperResponse) {
                if (result.success){
                    val list = result.data?.map{
                        AndroidDeveloperModel(it.talentID, it.accountID, it.accountName, it.accountEmail, it.accountPhone, it.talentTitle, it.talentTime, it.talentCity, it.talentDesc, it.talentImage, it.talentGithub, it.talentCv, it.talentSkill1, it.talentSkill2, it.talentSkill3, it.talentSkill4, it.talentSkill5)
                    }
                    view?.addAndroidDeveloperTalent(list)
                }
                else {
                    view?.hideProgressBar()
                    view?.setError(result.message)
                }
            }
        }
    }

    override fun getDevOpsTalent() {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getTalentDevOpsResponse()
                } catch (e: HttpException) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main){
                        when{
                            e.code() == 403 -> {
                                view?.setError("Session Expired !")
                            }

                            e.code()  == 404 -> {
                                view?.setError("No Talent Found !")
                            }

                            else -> {
                                view?.setError("Unknown Error, Please Try Again Later !")
                            }
                        }
                    }
                }
            }

            if (result is DevOpsEngineerResponse) {
                if (result.success){
                    val list = result.data?.map{
                        DevOpsEngineerModel(it.talentID, it.accountID, it.accountName, it.accountEmail, it.accountPhone, it.talentTitle, it.talentTime, it.talentCity, it.talentDesc, it.talentImage, it.talentGithub, it.talentCv, it.talentSkill1, it.talentSkill2, it.talentSkill3, it.talentSkill4, it.talentSkill5)
                    }
                    view?.addDevOpsEngineerTalent(list)
                }
                else {
                    view?.hideProgressBar()
                    view?.setError(result.message)
                }
            }
        }
    }

    override fun getFullStackMobileTalent() {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getTalentFullStackMobileResponse()
                } catch (e: HttpException) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main){
                        when{
                            e.code() == 403 -> {
                                view?.setError("Session Expired !")
                            }

                            e.code()  == 404 -> {
                                view?.setError("No Talent Found !")
                            }

                            else -> {
                                view?.setError("Unknown Error, Please Try Again Later !")
                            }
                        }
                    }
                }
            }

            if (result is FullStackMobileResponse) {
                if (result.success){
                    val list = result.data?.map{
                        FullStackMobileModel(it.talentID, it.accountID, it.accountName, it.accountEmail, it.accountPhone, it.talentTitle, it.talentTime, it.talentCity, it.talentDesc, it.talentImage, it.talentGithub, it.talentCv, it.talentSkill1, it.talentSkill2, it.talentSkill3, it.talentSkill4, it.talentSkill5)
                    }
                    view?.addFullStackMobileTalent(list)
                }
                else {
                    view?.hideProgressBar()
                    view?.setError(result.message)
                }
            }
        }
    }

    override fun getFullStackWebTalent() {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getTalentFullStackWebResponse()
                } catch (e: HttpException) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main){
                        when{
                            e.code() == 403 -> {
                                view?.setError("Session Expired !")
                            }

                            e.code()  == 404 -> {
                                view?.setError("No Talent Found !")
                            }

                            else -> {
                                view?.setError("Unknown Error, Please Try Again Later !")
                            }
                        }
                    }
                }
            }

            if (result is FullStackWebResponse) {
                if (result.success){
                    val list = result.data?.map{
                        FullStackWebModel(it.talentID, it.accountID, it.accountName, it.accountEmail, it.accountPhone, it.talentTitle, it.talentTime, it.talentCity, it.talentDesc, it.talentImage, it.talentGithub, it.talentCv, it.talentSkill1, it.talentSkill2, it.talentSkill3, it.talentSkill4, it.talentSkill5)
                    }
                    view?.addFullStackWebTalent(list)
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