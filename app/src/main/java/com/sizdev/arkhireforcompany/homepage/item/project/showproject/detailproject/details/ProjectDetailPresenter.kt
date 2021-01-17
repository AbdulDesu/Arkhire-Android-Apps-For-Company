package com.sizdev.arkhireforcompany.homepage.item.project.showproject.detailproject.details

import com.sizdev.arkhireforcompany.homepage.item.project.showproject.ProjectResponse
import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class ProjectDetailPresenter (private val coroutineScope: CoroutineScope,
                              private val service: ArkhireApiService?) : ProjectDetailContract.Presenter {

    private var view: ProjectDetailContract.View? = null

    override fun bindToView(view: ProjectDetailContract.View) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun getProject(projectID: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getProjectByIDResponse(projectID)
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

            if (result is ProjectDetailResponse) {
                if (result.success){
                    view?.hideProgressBar()
                    view?.setProjectData(result.data.projectID, result.data.projectTitle, result.data.projectDuration, result.data.projectDesc, result.data.projectSalary, result.data.projectImage, result.data.postedAt)
                }
                else {
                    view?.hideProgressBar()
                    view?.setError(result.message)
                }
            }
        }
    }

    override fun deleteProject(projectID: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.deleteProjectResponse(projectID)
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

            if (result is ProjectResponse) {
                if (result.success){
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