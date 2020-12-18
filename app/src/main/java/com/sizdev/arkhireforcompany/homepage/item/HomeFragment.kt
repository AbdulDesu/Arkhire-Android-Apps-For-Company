package com.sizdev.arkhireforcompany.homepage.item

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.homepage.AndroidDeveloperAdapter
import com.sizdev.arkhireforcompany.homepage.DevOpsEngineerAdapter
import com.sizdev.arkhireforcompany.homepage.FullStackMobileAdapter
import com.sizdev.arkhireforcompany.homepage.FullStackWebAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {

    @SuppressLint("SimpleDateFormat", "WeekBasedYear", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)


        // Get Date
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM YYYY")
        val currentDate = dateFormat.format(Date())

        // Get Saved name
        val sharedPrefData = requireActivity().getSharedPreferences("fullData", Context.MODE_PRIVATE)
        val savedData = sharedPrefData.getString("fullName", null)
        val nameSplitter = savedData?.split(" ")



        view.tv_homeDate.text = currentDate
        if (nameSplitter?.size == 1){
            view.tv_userGreeting.text = "Check This out, ${savedData?.capitalize()}"
        }
        else {
            val lastName = nameSplitter?.get(1).toString()
            view.tv_userGreeting.text = "Check This out, $lastName"
        }

        view.rv_androidDeveloperTalent.adapter = AndroidDeveloperAdapter()
        view.rv_androidDeveloperTalent.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

        view.rv_devOpsEngineerTalent.adapter = DevOpsEngineerAdapter()
        view.rv_devOpsEngineerTalent.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

        view.rv_fullStackMobileTalent.adapter = FullStackMobileAdapter()
        view.rv_fullStackMobileTalent.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

        view.rv_fullStackWebTalent.adapter = FullStackWebAdapter()
        view.rv_fullStackWebTalent.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.action_bar_menu, menu)
        val search = menu?.findItem(R.id.menu_search_email)
        val searchView = search?.actionView as SearchView

        searchView.queryHint = "Search in mail"

        return super.onCreateOptionsMenu(menu, inflater)
    }

}