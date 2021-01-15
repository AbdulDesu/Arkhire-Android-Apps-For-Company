package com.sizdev.arkhireforcompany.homepage.item.project.showproject.detailproject.contributor

interface ProjectContributorContract {

    interface View {
        fun addListContributor(list: List<ProjectContributorModel>)
    }

    interface Presenter{
        fun bindToView(view: View)
        fun unbind()
        fun showContributorApi(projectID : String)
    }
}