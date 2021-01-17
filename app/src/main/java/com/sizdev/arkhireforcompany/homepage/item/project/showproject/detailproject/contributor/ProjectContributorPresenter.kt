package com.sizdev.arkhireforcompany.homepage.item.project.showproject.detailproject.contributor

import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class ProjectContributorPresenter (private val coroutineScope: CoroutineScope,
                                   private val service: ArkhireApiService?) : ProjectContributorContract.Presenter {

    private var view: ProjectContributorContract.View? = null

    override fun bindToView(view: ProjectContributorContract.View) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun showContributorApi(projectID: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.showContributor(projectID)
                } catch (e: HttpException) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main){
                        when{
                            e.code() == 403 -> {
                                view?.setError("Session Expired !")
                            }

                            e.code() == 404 -> {
                                view?.hideProgressBar()
                                view?.setError("Project Not Found !")
                            }

                            else -> {
                                view?.setError("Unknown Error, Please Try Again Later !")
                            }
                        }
                    }
                }
            }

            if (result is ProjectContributorResponse) {
                val list = result.data?.map{
                    ProjectContributorModel(it.accountName, it.accountTitle, it.talentImage)
                }
                view?.addListContributor(list)
                view?.hideProgressBar()
            }
        }
    }
}