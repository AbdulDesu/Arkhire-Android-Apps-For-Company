package com.sizdev.arkhireforcompany.homepage.item.project.showproject

import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class ProjectPresenter(private val coroutineScope: CoroutineScope,
                       private val service: ArkhireApiService?) : ProjectContract.Presenter {

    private var view: ProjectContract.View? = null

    override fun bindToView(view: ProjectContract.View) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun getProject(accountID: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getAllProjectResponse(accountID)
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
                    val list = result.data?.map{
                        ProjectModel(it.projectID, it.projectTitle, it.projectDuration, it.projectDesc, it.projectSalary, it.projectImage, it.postedAt)
                    }
                    view?.hideProgressBar()
                    view?.addProjectList(list)
                }
                else {
                    view?.hideProgressBar()
                    view?.setError(result.message)
                }
            }
        }
    }
}