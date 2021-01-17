package com.sizdev.arkhireforcompany.homepage.item.project.showproject.detailproject.hiringlist


interface ProjectHiringContract {
    interface View {
        fun addListHiring(list: List<ProjectHiringModel>)
        fun setService()
        fun setError(error: String)
        fun showProgressBar()
        fun getCurrentProjectData()
        fun dataRefreshManagement()
        fun setUpRecyclerView()
        fun hideProgressBar()
        fun sessionExpiredAlert()
    }

    interface Presenter{
        fun bindToView(view: View)
        fun unbind()
        fun showHiringList(projectID : String)
    }
}