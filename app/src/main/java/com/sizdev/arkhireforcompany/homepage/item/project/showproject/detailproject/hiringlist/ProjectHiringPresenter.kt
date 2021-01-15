package com.sizdev.arkhireforcompany.homepage.item.project.showproject.detailproject.hiringlist

import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is ProjectHiringResponse) {
                val list = result.data?.map{
                    ProjectHiringModel(it.offeringID, it.hiringStatus, it.replyMsg, it.talentID, it.talentName, it.talentTitle, it.talentImage, it.projectSalary, it.projectImage)
                }
                view?.addListHiring(list)
            }
        }
    }
}