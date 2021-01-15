package com.sizdev.arkhireforcompany.homepage.item.project.showproject.detailproject.hiringlist


interface ProjectHiringContract {
    interface View {
        fun addListHiring(list: List<ProjectHiringModel>)
    }

    interface Presenter{
        fun bindToView(view: View)
        fun unbind()
        fun showHiringList(projectID : String)
    }
}