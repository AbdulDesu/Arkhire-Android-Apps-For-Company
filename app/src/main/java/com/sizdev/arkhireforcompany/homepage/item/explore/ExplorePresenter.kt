package com.sizdev.arkhireforcompany.homepage.item.explore

import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class ExplorePresenter(private val coroutineScope: CoroutineScope,
                       private val service: ArkhireApiService?) : ExploreContract.Presenter {

    private var view: ExploreContract.View? = null

    override fun bindToView(view: ExploreContract.View) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun getTalent() {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getAllTalentResponse()
                } catch (e: HttpException) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main){
                        when{
                            e.code() == 403 -> {
                                view?.setError("Session Expired !")
                            }

                            e.code() == 404 -> {
                                view?.setError("Search Result Not Found !")
                            }
                            else -> {
                                view?.setError("Unknown Error, Please Try Again Later !")
                            }
                        }
                    }
                }
            }

            if (result is ExploreResponse) {
                if (result.success){
                    val list = result.data?.map{
                        ExploreModel(it.talentID, it.accountID, it.accountName, it.accountEmail, it.accountPhone, it.talentTitle, it.talentTime, it.talentCity, it.talentDesc, it.talentImage, it.talentGithub, it.talentCv, it.talentSkill1, it.talentSkill2, it.talentSkill3, it.talentSkill4, it.talentSkill5)
                    }
                    view?.hideProgressBar()
                    view?.addExploreList(list)
                }
                else {
                    view?.hideProgressBar()
                    view?.setError(result.message)
                }
            }
        }
    }

    override fun searchByName(name: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.filterTalentByName(name)
                } catch (e: HttpException) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        when {
                            e.code() == 403 -> {
                                view?.setError("Session Expired !")
                            }

                            e.code() == 404 -> {
                                view?.hideProgressBar()
                                view?.setError("Search Result Not Found !")
                            }

                            else -> {
                                view?.setError("Unknown Error, Please Try Again Later !")
                            }
                        }
                    }
                }
            }

            if (result is ExploreResponse) {
                if (result.success) {
                    val list = result.data?.map {
                        ExploreModel(it.talentID, it.accountID, it.accountName, it.accountEmail, it.accountPhone, it.talentTitle, it.talentTime, it.talentCity, it.talentDesc, it.talentImage, it.talentGithub, it.talentCv, it.talentSkill1, it.talentSkill2, it.talentSkill3, it.talentSkill4, it.talentSkill5)
                    }
                    view?.hideProgressBar()
                    view?.addExploreList(list)
                } else {
                    view?.hideProgressBar()
                    view?.setError(result.message)
                }
            }
        }
    }

    override fun searchByTitle(title: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.filterTalentByTitle(title)
                } catch (e: HttpException) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        when {
                            e.code() == 403 -> {
                                view?.setError("Session Expired !")
                            }

                            e.code() == 404 -> {
                                view?.hideProgressBar()
                                view?.setError("Search Result Not Found !")
                            }

                            else -> {
                                view?.setError("Unknown Error, Please Try Again Later !")
                            }
                        }
                    }
                }
            }

            if (result is ExploreResponse) {
                if (result.success) {
                    val list = result.data?.map {
                        ExploreModel(it.talentID, it.accountID, it.accountName, it.accountEmail, it.accountPhone, it.talentTitle, it.talentTime, it.talentCity, it.talentDesc, it.talentImage, it.talentGithub, it.talentCv, it.talentSkill1, it.talentSkill2, it.talentSkill3, it.talentSkill4, it.talentSkill5)
                    }
                    view?.hideProgressBar()
                    view?.addExploreList(list)
                } else {
                    view?.hideProgressBar()
                    view?.setError(result.message)
                }
            }
        }
    }

    override fun searchByLocation(location: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.filterTalentByLocation(location)
                } catch (e: HttpException) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        when {
                            e.code() == 403 -> {
                                view?.setError("Session Expired !")
                            }

                            e.code() == 404 -> {
                                view?.hideProgressBar()
                                view?.setError("Search Result Not Found !")
                            }

                            else -> {
                                view?.setError("Unknown Error, Please Try Again Later !")
                            }
                        }
                    }
                }
            }

            if (result is ExploreResponse) {
                if (result.success) {
                    val list = result.data?.map {
                        ExploreModel(it.talentID, it.accountID, it.accountName, it.accountEmail, it.accountPhone, it.talentTitle, it.talentTime, it.talentCity, it.talentDesc, it.talentImage, it.talentGithub, it.talentCv, it.talentSkill1, it.talentSkill2, it.talentSkill3, it.talentSkill4, it.talentSkill5)
                    }
                    view?.hideProgressBar()
                    view?.addExploreList(list)
                } else {
                    view?.hideProgressBar()
                    view?.setError(result.message)
                }
            }
        }
    }

    override fun searchByWorkTime(time: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.filterTalentByTimeWork(time)
                } catch (e: HttpException) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        when {
                            e.code() == 403 -> {
                                view?.setError("Session Expired !")
                            }

                            e.code() == 404 -> {
                                view?.hideProgressBar()
                                view?.setError("Search Result Not Found !")
                            }

                            else -> {
                                view?.setError("Unknown Error, Please Try Again Later !")
                            }
                        }
                    }
                }
            }

            if (result is ExploreResponse) {
                if (result.success) {
                    val list = result.data?.map {
                        ExploreModel(it.talentID, it.accountID, it.accountName, it.accountEmail, it.accountPhone, it.talentTitle, it.talentTime, it.talentCity, it.talentDesc, it.talentImage, it.talentGithub, it.talentCv, it.talentSkill1, it.talentSkill2, it.talentSkill3, it.talentSkill4, it.talentSkill5)
                    }
                    view?.hideProgressBar()
                    view?.addExploreList(list)
                } else {
                    view?.hideProgressBar()
                    view?.setError(result.message)
                }
            }
        }
    }

}