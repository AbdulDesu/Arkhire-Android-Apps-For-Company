package com.sizdev.arkhireforcompany.homepage.item.explore

interface ExploreContract {

    interface View {
        fun addExploreList(list: List<ExploreModel>)
        fun setError(error: String)
        fun hideProgressBar()
    }

    interface Presenter{
        fun bindToView(view: View)
        fun unbind()
        fun getTalent()
        fun searchByName(name: String)
        fun searchByTitle(title: String)
        fun searchByLocation(location: String)
        fun searchByWorkTime(time: String)
    }
}