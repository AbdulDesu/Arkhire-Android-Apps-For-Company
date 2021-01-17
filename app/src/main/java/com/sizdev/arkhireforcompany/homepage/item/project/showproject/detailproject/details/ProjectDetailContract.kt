package com.sizdev.arkhireforcompany.homepage.item.project.showproject.detailproject.details


interface ProjectDetailContract {

    interface View {
        fun setError(error: String)
        fun setService()
        fun getCurrentProjectData()
        fun setProjectData(projectID: String, projectTitle: String, projectDuration: String, projectDesc: String, projectSalary: String, projectImage: String, postedAt: String)
        fun setDataRefreshManagement()
        fun showProgressBar()
        fun hideProgressBar()
        fun projectDeleteConfirmation()
        fun showSessionExpiredAlert()
    }

    interface Presenter{
        fun bindToView(view: View)
        fun unbind()
        fun getProject(projectID: String)
        fun deleteProject(projectID: String)
    }

}