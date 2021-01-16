package com.sizdev.arkhireforcompany.homepage.item.project.showproject

interface ProjectContract {

    interface View {
        fun addProjectList(list: List<ProjectModel>)
        fun setError(error: String)
        fun hideProgressBar()
    }

    interface Presenter{
        fun bindToView(view: View)
        fun unbind()
        fun getProject(accountID: String)
    }

}