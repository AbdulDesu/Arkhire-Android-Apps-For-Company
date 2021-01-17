package com.sizdev.arkhireforcompany.homepage.item.project.showproject.detailproject.contributor

interface ProjectContributorContract {

    interface View {
        fun setService()
        fun addListContributor(list: List<ProjectContributorModel>)
        fun setUpRecyclerView()
        fun setError(error: String)
        fun showProgressBar()
        fun hideProgressBar()
        fun getCurrentProjectData()
        fun showSessionExpiredAlert()
        fun dataRefreshManagement()
    }

    interface Presenter{
        fun bindToView(view: View)
        fun unbind()
        fun showContributorApi(projectID : String)
    }
}