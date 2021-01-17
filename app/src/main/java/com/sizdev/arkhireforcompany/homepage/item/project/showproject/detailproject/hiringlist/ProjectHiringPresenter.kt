package com.sizdev.arkhireforcompany.homepage.item.project.showproject.detailproject.hiringlist

import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class ProjectHiringPresenter(private val coroutineScope: CoroutineScope,
                             private val service: ArkhireApiService?) : ProjectHiringContract.Presenter {

    private var view: ProjectHiringContract.View? = null

    override fun bindToView(view: ProjectHiringContract.View) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun showHiringList(projectID: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getHiringListResponse(projectID)
                } catch (e: HttpException) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main){
                        when{
                            e.code() == 403 -> {
                                view?.setError("Session Expired !")
                            }
                            e.code() == 404 -> {
                                view?.hideProgressBar()
                                view?.setError("Data Not Found !")
                            }

                            else -> {
                                view?.setError("Unknown Error, Please Try Again Later !")
                            }
                        }
                    }
                }
            }

            if (result is ProjectHiringResponse) {
                val list = result.data?.map{
                    ProjectHiringModel(it.offeringID, it.hiringStatus, it.offeredSalary, it.replyMsg, it.talentID, it.talentName, it.talentTitle, it.talentImage, it.projectSalary, it.projectImage)
                }
                view?.addListHiring(list)
                view?.hideProgressBar()
            }
        }
    }
}